package com.hc.gqgs.mybatis.po.business;

import java.util.Date;

public class SecondVisaPojo {
	private String sCode;
	private String path;
	private String data;
	private String photoPath;
	private Date time;
	private Integer state;
	public String getsCode() {
		return sCode;
	}
	public void setsCode(String sCode) {
		this.sCode = sCode;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public String getPhotoPath() {
		return photoPath;
	}
	public void setPhotoPath(String photoPath) {
		this.photoPath = photoPath;
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
		return "SecondVisaPojo [sCode=" + sCode + ", path=" + path + ", data=" + data + ", photoPath=" + photoPath
				+ ", time=" + time + ", state=" + state + "]";
	}
	
	
}
