package com.schoolbuy.bean;

import java.util.ArrayList;

import cn.bmob.im.bean.BmobChatUser;
import cn.bmob.v3.datatype.BmobFile;

public class Member extends BmobChatUser
{
	//信用度为0-10
	private int buyCredit;
	private int sellCredit;
	private ArrayList<String> like = new ArrayList<String>();
	private ArrayList<String> itemList = new ArrayList<String>();
	private BmobFile icon;

	public Member(int buyCredit, int sellCredit, ArrayList<String> like,
			ArrayList<String> itemList, BmobFile icon) {
		super();
		this.buyCredit = buyCredit;
		this.sellCredit = sellCredit;
		this.like = like;
		this.itemList = itemList;
		this.icon = icon;
	}

	public Member()
	{
		
	}
	
	public void addItem(String newItemId)
	{
		itemList.add(newItemId);
	}
	
	public ArrayList<String> getItemList() {
		return itemList;
	}

	public void setItemList(ArrayList<String> itemList) {
		this.itemList = itemList;
	}

	public ArrayList<String> getLike() {
		return like;
	}
	public void setLike(ArrayList<String> like) {
		this.like = like;
	}

	public BmobFile getIcon() {
		return icon;
	}
	public void setIcon(BmobFile icon) {
		this.icon = icon;
	}

	public int getBuyCredit() {
		return buyCredit;
	}

	public void setBuyCredit(int buyCredit) {
		if(buyCredit<0)
		{
			this.buyCredit = 0;
		}
		else
		{
			this.buyCredit = buyCredit;
		}
		
	}

	public int getSellCredit() {
		return sellCredit;
	}

	public void setSellCredit(int sellCredit) {
		if(sellCredit<0)
		{
			this.sellCredit = 0;
		}
		else
		{
			this.sellCredit = sellCredit;
		}
	}

	@Override
	public String toString() {
		return getUsername();
	}
	
	
	
}
