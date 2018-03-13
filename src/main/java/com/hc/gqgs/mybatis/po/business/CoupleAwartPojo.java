package com.hc.gqgs.mybatis.po.business;

import java.util.Date;

public class CoupleAwartPojo {
	private String cCode;
	private String path;
	private String infoPath;
	private String data;
	private String hkPhotoPath;
	private String idCardPath;
	private String mcPicture;
	private Date time;
	private Integer state;
	private String picturePath;
	
	public String getPicturePath() {
		return picturePath;
	}
	public void setPicturePath(String picturePath) {
		this.picturePath = picturePath;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public String getcCode() {
		return cCode;
	}
	public void setcCode(String cCode) {
		this.cCode = cCode;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getInfoPath() {
		return infoPath;
	}
	public void setInfoPath(String infoPath) {
		this.infoPath = infoPath;
	}
	public String getHkPhotoPath() {
		return hkPhotoPath;
	}
	public void setHkPhotoPath(String hkPhotoPath) {
		this.hkPhotoPath = hkPhotoPath;
	}
	public String getIdCardPath() {
		return idCardPath;
	}
	public void setIdCardPath(String idCardPath) {
		this.idCardPath = idCardPath;
	}
	public String getMcPicture() {
		return mcPicture;
	}
	public void setMcPicture(String mcPicture) {
		this.mcPicture = mcPicture;
	}
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	@Override
	public String toString() {
		return "CoupleAwartPojo [cCode=" + cCode + ", path=" + path + ", infoPath=" + infoPath + ", data=" + data
				+ ", hkPhotoPath=" + hkPhotoPath + ", idCardPath=" + idCardPath + ", mcPicture=" + mcPicture + ", time="
				+ time + ", state=" + state + ", picturePath=" + picturePath + "]";
	}
	
}
