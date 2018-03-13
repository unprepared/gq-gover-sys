package com.hc.gqgs.mybatis.po.business;

public class LoginMenu {

	private String menuName;
	private String operation;
	private String permissionUrl;
	public String getMenuName() {
		return menuName;
	}
	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}
	public String getOperation() {
		return operation;
	}
	public void setOperation(String operation) {
		this.operation = operation;
	}
	public String getPermissionUrl() {
		return permissionUrl;
	}
	public void setPermissionUrl(String permissionUrl) {
		this.permissionUrl = permissionUrl;
	}
	@Override
	public String toString() {
		return "LoginMenu [menuName=" + menuName + ", operation=" + operation + ", permissionUrl=" + permissionUrl
				+ "]";
	}
	
}
