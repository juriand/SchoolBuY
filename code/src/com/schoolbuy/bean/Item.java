package com.schoolbuy.bean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

public class Item extends BmobObject
{
	private String itemName, itemIntro, owner, target;
	private float itemPrice;
	//0为交易结束，1为正在交易，2为等待交易
	private int itemStatus;
	//1为交易，2为交换，3为拍卖
	private int itemKind;
	private String[] itemTag = new String[3];
	private BmobFile itemIcon;
	
	public Item()
	{
		
	}

	public Item(String itemName, float itemPrice, int itemStatus,
			String[] itemTag, String itemIntro, int itemKind, BmobFile itemIcon) {
		super();
		this.itemName = itemName;
		this.itemPrice = itemPrice;
		this.itemStatus = itemStatus;
		this.itemTag = itemTag;
		this.itemIntro = itemIntro;
		this.itemKind = itemKind;
		this.itemIcon = itemIcon;
	}
	
	

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public int getItemKind() {
		return itemKind;
	}

	public void setItemKind(int itemKind) {
		this.itemKind = itemKind;
	}

	public BmobFile getItemIcon() {
		return itemIcon;
	}

	public void setItemIcon(BmobFile itemIcon) {
		this.itemIcon = itemIcon;
	}

	public String[] getItemTag() {
		return itemTag;
	}



	public void setItemTag(String[] itemTag) {
		this.itemTag = itemTag;
	}



	public String getItemIntro() {
		return itemIntro;
	}

	public void setItemIntro(String itemIntro) {
		this.itemIntro = itemIntro;
	}

	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public float getItemPrice() {
		return itemPrice;
	}
	public void setItemPrice(float itemPrice) {
		this.itemPrice = itemPrice;
	}
	public int getItemStatus() {
		return itemStatus;
	}
	public void setItemStatus(int itemStatus) {
		this.itemStatus = itemStatus;
	}
}
