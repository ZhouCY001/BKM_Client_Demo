package com.wizarpos.paymentrouterclient;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.util.List;

import static android.content.Context.ACTIVITY_SERVICE;

public class BroadReceiver  extends BroadcastReceiver {
    static String TAG = "BroadReceiver";

    static String packageName = "com.wizarpos.paymentrouterclient";
    static String activityName = "com.wizarpos.paymentrouterclient.MainActivity";

    public String EFD_REQUEST_ACTION = "com.wizarpos.bkm.SETTLE_REQUEST";//Start End of day
    public String EFD_START_ACTION = "com.wizarpos.bkm.EFD_STARTED";//Start End of day
    public String EFD_END_ACTION = "com.wizarpos.bkm.EFD_FINISHED";//Finished End of day

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, intent.getAction());

        if (EFD_END_ACTION.equals(intent.getAction())){
            Bundle bundle = intent.getExtras();
            for (String key: bundle.keySet())
            {
                Log.i(TAG, "Key=" + key + ", value=" +bundle.get(key));
            }

            if(bundle.containsKey("Approved") &&bundle.getBoolean("Approved")) { // end of day succeed
                //The details of the batch info
                String BatchNum = bundle.getString("BatchNum","000000");
                String Currency = bundle.getString("Currency","949");

                String OnlinePositiveCount = bundle.getString("OnlinePositiveCount","0000");
                String OnlineNegativeCount = bundle.getString("OnlineNegativeCount","0000");
                String OfflinePositiveCount = bundle.getString("OfflinePositiveCount","0000");
                String OfflineNegativeCount = bundle.getString("OfflineNegativeCount","0000");

                String OnlinePositiveAmount = bundle.getString("OnlinePositiveAmount","000000000000");
                String OnlineNegativeAmount = bundle.getString("OnlineNegativeAmount","000000000000");
                String OfflinePositiveAmount = bundle.getString("OfflinePositiveAmount","000000000000");
                String OfflineNegativeAmount = bundle.getString("OfflineNegativeAmount","000000000000");


                String MerchantName = bundle.getString("MerchantName","");
                String Address = bundle.getString("Address","");
                String MID = bundle.getString("MID","");
                String TID = bundle.getString("TID","");

                String SaleCount = bundle.getString("SaleCount","");
                String SaleAmount = bundle.getString("SaleAmount","");

                String VoidCount = bundle.getString("VoidCount","");
                String VoidAmount = bundle.getString("VoidAmount","");

                String RefundCount = bundle.getString("RefundCount","");
                String RefundAmount = bundle.getString("RefundAmount","");

                String AuthCompCount = bundle.getString("AuthCompCount","");
                String AuthCompAmount = bundle.getString("AuthCompAmount","");

                String InstallmentCount = bundle.getString("InstallmentCount","");
                String InstallmentAmount = bundle.getString("InstallmentAmount","");

                String TotalCount = bundle.getString("TotalCount","");
                String TotalAmount = bundle.getString("TotalAmount","");
            }else{
                //End of day failed
            }

        }

    }




    private boolean isMainActivityAlive(Context context, String activityName){
        ActivityManager am = (ActivityManager)context.getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(100);
        for (ActivityManager.RunningTaskInfo info : list) {
            if (info.topActivity.getClassName().equals(activityName) || info.baseActivity.getClassName().equals(activityName)) {
                Log.i(TAG,info.topActivity.getPackageName() + " info.baseActivity.getPackageName()="+info.baseActivity.getPackageName());
                return true;
            }
        }
        return false;
    }




}