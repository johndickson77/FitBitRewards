package com.example.fitbit.model;

public class UserData {

	private String deviceID;
	private String userID;
	private Integer stepsCount;
	private String units;

	public String getDeviceID() {
		return deviceID;
	}

	public void setDeviceID(String deviceID) {
		this.deviceID = deviceID;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public Integer getStepsCount() {
		return stepsCount;
	}

	public void setStepsCount(Integer stepsCount) {
		this.stepsCount = stepsCount;
	}

	public String getUnits() {
		return units;
	}

	public void setUnits(String units) {
		this.units = units;
	}

	@Override
	public String toString() {
		return "UserData [deviceID=" + deviceID + ", userID=" + userID + ", stepsCount=" + stepsCount + ", units="
				+ units + "]";
	}

}
