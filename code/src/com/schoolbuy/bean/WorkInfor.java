package com.schoolbuy.bean;

public class WorkInfor 
{
	private String title;
	private String body;
	private String startTime;
	private String endTime;
	
	public WorkInfor()
	{
		
	}

	public WorkInfor(String title, String body, String startTime, String endTime) {
		super();
		this.title = title;
		this.body = body;
		this.startTime = startTime;
		this.endTime = endTime;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
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
	
	

}
