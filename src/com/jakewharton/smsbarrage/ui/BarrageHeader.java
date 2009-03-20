package com.jakewharton.smsbarrage.ui;

public class BarrageHeader {
	private static final String TAG="BarrageHeader";
	
	public static final int STATUS_RUNNING = 0;
	public static final int STATUS_DRAFT = 1;
	public static final int STATUS_QUEUED = 2;
	public static final int STATUS_PAUSED = 3;
	
	private int mID;
	private String mName;
	private int mStatus;
	private int mCountTotal;
	private int mCountCurrent;
	
	public BarrageHeader() {}
	public BarrageHeader(String name, int status, int total, int current) {
		mName = name;
		mStatus = status;
		mCountTotal = total;
		mCountCurrent = current;
	}
	
	public int getID() {
		return mID;
	}
	public String getName() {
		return mName;
	}
	public void setName(String name) {
		mName = name;
	}
	public int getStatus() {
		return mStatus;
	}
	public void setStatus(int status) {
		//TODO: check if valid
		mStatus = status;
	}
	public int getCountTotal() {
		return mCountTotal;
	}
	public void setCountTotal(int total) {
		mCountTotal = total;
	}
	public int getCountCurrent() {
		return mCountCurrent;
	}
	public void setCountCurrent(int current) {
		mCountCurrent = current;
	}
}
