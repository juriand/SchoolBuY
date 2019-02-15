package com.schoolbuy.bean;

import cn.bmob.v3.BmobObject;

public class Want extends BmobObject
{
	private String owner;
	private String name;
	private String intro;
	
	public Want(String owner, String name, String intro) {
		super();
		this.owner = owner;
		this.name = name;
		this.intro = intro;
	}
	
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIntro() {
		return intro;
	}
	public void setIntro(String intro) {
		this.intro = intro;
	}
	
	

}
