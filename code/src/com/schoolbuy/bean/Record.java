package com.schoolbuy.bean;

import cn.bmob.v3.BmobObject;

public class Record extends BmobObject
{
	private String itemID;
	private String recordID;
	private String buyerID;
	private String sellerID;
	
	private String startTime;
	private String endTime;
	
	//0为交易取消，1为交易成功，2为卖家未确认，3为买家未确认，4为双方均未确认
	private int recordStatus;
	
	public Record()
	{
		
	}
	
	public Record(String itemID, String buyerID, String sellerID,
			String startTime, int recordStatus) {
		super();
		this.itemID = itemID;
		this.buyerID = buyerID;
		this.sellerID = sellerID;
		this.startTime = startTime;
		this.endTime = "";
		this.recordStatus = recordStatus;
	}
	
	public String getItemID() {
		return itemID;
	}
	public void setItemID(String itemID) {
		this.itemID = itemID;
	}
	public String getRecordID() {
		return recordID;
	}
	public void setRecordID(String recordID) {
		this.recordID = recordID;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public int getRecordStatus() {
		return recordStatus;
	}
	public void setRecordStatus(int recordStatus) {
		this.recordStatus = recordStatus;
	}

	public String getBuyerID() {
		return buyerID;
	}

	public void setBuyerID(String buyerID) {
		this.buyerID = buyerID;
	}

	public String getSellerID() {
		return sellerID;
	}

	public void setSellerID(String sellerID) {
		this.sellerID = sellerID;
	}

}
