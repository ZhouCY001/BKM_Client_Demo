package com.wizarpos.bkm.aidl;

interface ICloudPay{
	String getPOSInfo(String jsonData);
	String transact(String jsonData);
	String settle(String jsonData);
	String[] getAcquirerList();
	void selectDefaultAcquirer(int index);
	String printLast(String jsonData);
	String setPubCert(String jsonData);
	String setVirtualSN(String jsonData);
	String getVirtualSN();
	String setMMK(String jsonData);
	String exchangeKey();
	String downloadParams();
	String setParams(String jsonData); //support change MMK/PubCert/Ip/Port
	void cancelTransaction();
	void setBinBlackList(String binList);
	String getBinBlackList();
	String getAIDFloorLimit();
}