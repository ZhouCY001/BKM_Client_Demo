package com.wizarpos.paymentrouterclient;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.wizarpos.bkm.aidl.ICloudPay;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.Timer;
import java.util.TimerTask;

import utils.StringUtil;

public class MainActivity extends Activity implements OnClickListener {

	private String param, response,transactionNum;

	private String clientSN = "SRL000000001";

	private ICloudPay mWizarPayment;
	final ServiceConnection mConnPayment = new PaymentConnection();

	boolean IsTestEnvironment = true;

	public static Context _Context;

	public static String PRO_IP1 = "62.244.244.94";
	public static int PRO_PORT1 = 12121;
	public static String PRO_IP2 = "62.244.244.94";
	public static int PRO_PORT2 = 12121;
	public static String TEST_IP1 = "31.145.171.94";
	public static int TEST_PORT1 = 12121;
	public static String TEST_IP2 = "31.145.171.94";
	public static int TEST_PORT2 = 12121;

	public static String EFD_ACTION = "com.wizarpos.bkm.efd";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		_Context = getApplicationContext();

		int[] btnIds = { R.id.bind, R.id.unbind,R.id.setVirtualSN
			, R.id.payCash, R.id.voidSale, R.id.selectAcquirer,R.id.getAcquirerList
			, R.id.exchangeKey,R.id.downParams
			,R.id.setBinBlackList, R.id.getBinBlackList
			, R.id.printlast, R.id.settle,R.id.queryInfo
			, R.id.PreAuth, R.id.AuthCancel,R.id.AuthComp
			,R.id.getVirtualSN,R.id.setPubCert,R.id.setMMK,R.id.setParams
		};
		for (int id : btnIds) {
			findViewById(id).setOnClickListener(this);
		}

		Switch sw = (Switch) findViewById(R.id.switch_test);
		sw.setChecked(IsTestEnvironment);
		sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				IsTestEnvironment = isChecked;
			}
		});
	}

	@Override
	public void onBackPressed() {
		System.exit(0);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unbindPaymentRouter();
	}

	class PaymentConnection implements ServiceConnection {
		@Override
		public void onServiceConnected(ComponentName compName, IBinder binder) {
			Log.d("onServiceConnected", "compName: " + compName);
			mWizarPayment = ICloudPay.Stub.asInterface(binder);
			showResponse("Connect Success!");
		}

		@Override
		public void onServiceDisconnected(ComponentName compName) {
			Log.d("onServiceDisconnected", "compName: " + compName);
			mWizarPayment = null;
			showResponse("Disconnect Success!");
		}
	};

	private void bindPaymentRouter() {
		if (mWizarPayment == null) {
//			Intent intent = new Intent(ICloudPay.class.getName());
			Intent intent = new Intent("com.wizarpos.bkm.aidl.ICloudPay");
			intent.setPackage("com.wizarpos.bkm");
			bindService(intent, mConnPayment, BIND_AUTO_CREATE);
		}
	}
	private void unbindPaymentRouter() {
		if (mWizarPayment != null) {
			unbindService(mConnPayment);
			mWizarPayment = null;
		}
	}

	public void showResponse(String response) {
		this.response = response;
		showResponse();

	}

	public void showResponse() {
		setTextById(R.id.param, param);
		setTextById(R.id.result, response);
	}
	private void setTextById(int id, CharSequence text) {
		((TextView)findViewById(id)).setText(text);
	}

	@Override
	public void onClick(final View view) {
		final int btnId = view.getId();
		setTextById(R.id.method, ((TextView)view).getText());

		param = "";
		response = "";
		switch(btnId) {
		case R.id.bind:				bindPaymentRouter();    break;
		case R.id.unbind:			unbindPaymentRouter();  break;
		case R.id.setVirtualSN:		showInputDialog("Please input SN",12,btnId);		break;
		case R.id.voidSale:
		case R.id.AuthCancel:		showInputDialog("Please input Transaction Num",6,btnId);		break;
		default:
			if (mWizarPayment == null) {
				response = "Please click [ConnectPaymentRouter First]!";
			} else if(!mWizarPayment.asBinder().isBinderAlive()){
				response = "Please click [ConnectPaymentRouter First]!";
			}else if (null == (param = getParam(btnId))) {
				response = "Call parameter failed!";
			}
			if (response == "") {
				createAsyncTask().execute(btnId);
				return;
			}
			break;
		}
		showResponse();
	}

	private String getParam(int btnId) {
		JSONObject jsonObject = new JSONObject();
		try {
			switch(btnId) {
			case R.id.payCash:				setParam4PayCash(jsonObject);	break;
			case R.id.voidSale:				setParam4VoidSale(jsonObject);	break;
			case R.id.PreAuth:				setParam4PreAuth(jsonObject);	break;
			case R.id.AuthCancel:			setParam4AuthCancel(jsonObject);	break;
			case R.id.AuthComp:				setParam4AuthComp(jsonObject);	break;
			case R.id.exchangeKey:
			case R.id.downParams:
			case R.id.getAcquirerList:
			case R.id.selectAcquirer:		break;
			case R.id.printlast:			setParam4getPrintLast		(jsonObject);	break;
			case R.id.settle:				setParam4settle				(jsonObject);	break;
			case R.id.setVirtualSN:			setParam4SetVirtualSN		(jsonObject);	break;
			case R.id.getVirtualSN: 		break;
			case R.id.setPubCert:			setParam4SetPubCert			(jsonObject);	break;
			case R.id.setMMK:				setParam4SetMMK				(jsonObject);	break;
			case R.id.setParams:			setParam4SetParams			(jsonObject);	break;
			case R.id.queryInfo:			setParam4getPOSInfo			(jsonObject);	break;
			case R.id.setBinBlackList:		setParam4BlackList			(jsonObject);	break;
			}
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
		return jsonObject.toString();
	}

	@SuppressLint("StaticFieldLeak")
    private AsyncTask<Integer, Void, String> createAsyncTask() {
		return new AsyncTask<Integer, Void, String>() {
			protected void onPreExecute() {
				showResponse("...");
			}
			protected String doInBackground(Integer...btnIds) {
				Log.d("doInBackground", "Request: " + param + " mWizarPayment: " + mWizarPayment);

				String result = "Skipped";
				try {
					switch(btnIds[0]) {
					case R.id.exchangeKey:		result = mWizarPayment.exchangeKey		();	break;
					case R.id.downParams:		result = mWizarPayment.downloadParams	();	break;
					case R.id.setBinBlackList:	mWizarPayment.setBinBlackList			(param);	break;
					case R.id.getBinBlackList:	result = mWizarPayment.getBinBlackList			();	break;
					case R.id.payCash:
					case R.id.voidSale:
					case R.id.PreAuth:
					case R.id.AuthCancel:
					case R.id.AuthComp:
						result = mWizarPayment.transact			(param);	break;
					case R.id.getAcquirerList:
						String[] acqList = mWizarPayment.getAcquirerList();
						result = "AcquirerList:";
						for(String str:acqList){
							result+= str + "|";
						}
						if(!TextUtils.isEmpty(result))
							result.substring(0,result.length()-1);
						break;
					case R.id.selectAcquirer:	mWizarPayment.selectDefaultAcquirer(0);		break;
					case R.id.printlast:		result = mWizarPayment.printLast		(param);	break;
					case R.id.settle:			result = mWizarPayment.settle			(param);	break;
					case R.id.setVirtualSN:		result = mWizarPayment.setVirtualSN		(param);	break;
					case R.id.getVirtualSN:		result = mWizarPayment.getVirtualSN		();			break;
					case R.id.setPubCert:		result = mWizarPayment.setPubCert		(param);	break;
					case R.id.setMMK:			result = mWizarPayment.setMMK			(param);	break;
					case R.id.setParams:		result = mWizarPayment.setParams		(param);	break;
					case R.id.queryInfo:		result = mWizarPayment.getPOSInfo		(param);	break;
					}
				} catch (RemoteException e) {
					result = e.getMessage();
				}

				Log.d("doInBackground", "Response: " + result);

				return result;
			}
			protected void onPostExecute(String result) {
				showResponse(result);
			}
		};
	}

	private void setParam4BlackList(JSONObject jsonObject) throws JSONException{
		String pan1 = "2320120000013461";
		String pan2 = "6214623521000872179";

		JSONArray jsonArray = new JSONArray();
		jsonArray.put(pan1);
		jsonArray.put(pan2);
		jsonObject.put("BlackList",jsonArray);

	}

	private void setParam4PayCash(JSONObject jsonObject) throws JSONException {
		jsonObject.put("TransType", 1);
		jsonObject.put("TransAmount", "1");
		jsonObject.put("timeOut", 120);// 10 means timeout after 10 seconds
		// 0		: disable QR function
		// others	: enable QR function
		jsonObject.put("supportQR", 1);
		jsonObject.put("printReceipt",1);
	}


	private void setParam4VoidSale(JSONObject jsonObject) throws JSONException {
		jsonObject.put("passWord", "123456");
		jsonObject.put("TransType", 101);
		if(transactionNum!=null && transactionNum.length()!=0)
			jsonObject.put("oriTransactionNum", transactionNum);

		//2023/07/10 Add to skip the acquirer select function.
		//'0' means the 1st acquirer in acquirer list ,'1' means 2nd one.
		//You can get acquirer list by AIDL interface 'getAcquirerList()'
		jsonObject.put("acquirerIndex", "0");

		jsonObject.put("supportQR", 1);
		jsonObject.put("printReceipt",1);
	}

	private void setParam4PreAuth(JSONObject jsonObject) throws JSONException {
		jsonObject.put("TransType", 2);
		jsonObject.put("TransAmount", "1");
		jsonObject.put("timeOut", 120);// 10 means timeout after 10 seconds

		jsonObject.put("acquirerIndex", "0");

		if(transactionNum!=null && transactionNum.length()!=0)
			jsonObject.put("oriTransactionNum", transactionNum);

		// 0		: disable QR function
		// others	: enable QR function
		jsonObject.put("supportQR", 1);
		jsonObject.put("printReceipt",1);
	}

	private void setParam4AuthCancel(JSONObject jsonObject) throws JSONException {
		jsonObject.put("passWord", "123456");
		jsonObject.put("TransType", 107);
		jsonObject.put("acquirerIndex", "0");

		jsonObject.put("printReceipt",1);
	}

	private void setParam4AuthComp(JSONObject jsonObject) throws JSONException {
		jsonObject.put("passWord", "123456");
		jsonObject.put("TransType", 109);

		jsonObject.put("TransAmount", "1");

		jsonObject.put("acquirerIndex", "0");

		jsonObject.put("printReceipt",1);
	}


	private void setParam4getPOSInfo(JSONObject jsonObject) throws JSONException {
	}

	private void setParam4getPrintLast(JSONObject jsonObject) throws JSONException {
		jsonObject.put("printReceipt",1);
	}

	private void setParam4settle(JSONObject jsonObject) throws JSONException {
		jsonObject.put("TransType", 21);
		jsonObject.put("printReceipt",1);
	}

	private void setParam4SetVirtualSN(JSONObject jsonObject) throws JSONException {
		jsonObject.put("passWord", "123456");
		//Change the right SN in this place
		jsonObject.put("virtualSN", clientSN);
	}

	private void setParam4SetPubCert(JSONObject jsonObject) throws JSONException {
		byte[] bPubCert;
		if(IsTestEnvironment){
			bPubCert = readFile(_Context,"SRL_BSPK2_TEST.bin",128);
		}else
			bPubCert = readFile(_Context,"SRL_BSPK2_PRO.bin",128);

		jsonObject.put("passWord", "123456");
		jsonObject.put("pubCert", StringUtil.toHexString(bPubCert));
	}

	private void setParam4SetMMK(JSONObject jsonObject) throws JSONException {

		byte[] bMMK;
		if(IsTestEnvironment){
			bMMK = readFile(_Context,"SRL_BSMSK_TEST.txt",32);
		}else
			bMMK = readFile(_Context,"SRL_BSMSK_PRO.txt",32);

		jsonObject.put("MMK", StringUtil.toString(bMMK));
		jsonObject.put("passWord", "123456");
	}


	private void setParam4SetParams(JSONObject jsonObject) throws JSONException {
		byte[] bPubCert;
		byte[] bMMK;
		String ip1,ip2;
		int port1,port2;
		if(IsTestEnvironment){
			bPubCert = readFile(_Context,"SRL_BSPK2_TEST.bin",128);
			bMMK = readFile(_Context,"SRL_BSMSK_TEST.txt",32);
			ip1 = TEST_IP1;
			ip2 = TEST_IP2;
			port1 = TEST_PORT1;
			port2 = TEST_PORT2;
		}else{
			bPubCert = readFile(_Context,"SRL_BSPK2_PRO.bin",128);
			bMMK = readFile(_Context,"SRL_BSMSK_PRO.txt",32);
			ip1 = PRO_IP1;
			ip2 = PRO_IP2;
			port1 = PRO_PORT1;
			port2 = PRO_PORT2;
		}

		jsonObject.put("PrimaryIP",ip1);
		jsonObject.put("SecondaryIP",ip2);
		jsonObject.put("PrimaryPort",port1);
		jsonObject.put("SecondaryPort",port2);
		jsonObject.put("pubCert", StringUtil.toHexString(bPubCert));
		jsonObject.put("MMK", StringUtil.toString(bMMK));
//		jsonObject.put("virtualSN", clientSN);
		jsonObject.put("passWord", "123456");
		//Change the workSpace password (used for void/cancel/refund) , the lenngth should be 4-8
		jsonObject.put("newWorkspacePassword","0000");

		//"0" means false,
		//"1" means true, auto do the settlement in BKM app , after 'request' event timeout.
		jsonObject.put("autoSettleAfterTimeout", "1");
		//2024/04/28
		//"0": do nothing,
		//others: clear the parameter file in DB
		jsonObject.put("clearParamsFile", "1");

		//2024 06 17
		//Add supportMSR : 0---> disable MSR , others---> enable MSR
		jsonObject.put("supportMSR", "1");

		//20250520
		//Add autoSelectACQ: 0--> disable auto select ACQ function, others --> enable auto select ACQ
		jsonObject.put("autoSelectACQ","1");

		//20250523
		//Add terminalFloorLimit
		jsonObject.put("terminalFloorLimit",500); //When a currency has 2 decimal places,it means limit is 5.00
	}


	private void showInputDialog(String title,int maxInput,final int btnID) {
		clientSN = "";
		transactionNum = "";

		final EditText editText = new EditText(MainActivity.this);
		InputFilter[] filters = {new InputFilter.LengthFilter(maxInput)};
		editText.setFilters(filters);
		AlertDialog.Builder inputDialog =
			new AlertDialog.Builder(MainActivity.this);
		inputDialog.setTitle(title).setView(editText);
		inputDialog.setNegativeButton("Cancel",null);
		inputDialog.setPositiveButton("Confirm",
			new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					switch (btnID){
					case R.id.setVirtualSN: 	clientSN = editText.getText().toString(); 				break;
					case R.id.voidSale:
					case R.id.AuthCancel:
						transactionNum = editText.getText().toString(); 		break;
					}

					if (mWizarPayment == null) {
						response = "Please click [ConnectPaymentRouter First]!";
					} else if (null == (param = getParam(btnID))) {
						response = "Call parameter failed!";
					}
					if (response == "") {
						createAsyncTask().execute(btnID);
						return;
					}

					showResponse();
				}
			}).show();
	}


	private void cancelTransaction(){
		Timer timer =new Timer();
		timer.schedule(
			new TimerTask() {
				@Override
				public void run() {
					try {
						mWizarPayment.cancelTransaction();
					} catch (RemoteException e) {
						e.printStackTrace();
					}
				}
			}
			,3000);
	}



	public static byte[] readFile(Context context , String fileName, int readLength) {
		char[] bRet = null;
		int length = readLength;// BKM bin文件为128字节，对应的RSA 1024 公钥的模

		AssetManager manager = context.getResources().getAssets();
		try {
			InputStream inputStream = manager.open(fileName);
			InputStreamReader isr = new InputStreamReader(inputStream , "ISO-8859-1" );
			BufferedReader br = new BufferedReader(isr);
			bRet = new char[length];
			br.read(bRet,0,length);
			br.close();
			isr.close();
			inputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			return charToBytes(bRet);

		}
	}

	public static byte[] charToBytes(char[] chars) {
		if(chars == null)
			return null;

		Charset charset = Charset.forName("ISO-8859-1");
		CharBuffer charBuffer = CharBuffer.allocate(chars.length);
		charBuffer.put(chars);
		charBuffer.flip();
		ByteBuffer byteBuffer = charset.encode(charBuffer);
		return byteBuffer.array();
	}



	private class StandardReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent != null && intent.getAction().equals(EFD_ACTION)) {

			}
		}
	}



}
