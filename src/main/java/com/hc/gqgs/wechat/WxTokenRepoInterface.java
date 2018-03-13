package com.hc.gqgs.wechat;

/*
 * 存储微信AccessToken Ticket的仓库接口
 */
public interface WxTokenRepoInterface {
	// 获取AccessToken
	public String getAccessToken();

	// 获取Ticket
	public String getTicket();
}
