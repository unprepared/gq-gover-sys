package com.hc.gqgs.wechat;

import com.alibaba.fastjson.JSONObject;
import com.hc.gqgs.tools.HttpRequestTool;

public class DefaultWxTokenRepo implements WxTokenRepoInterface {
	// 存储
	ExpiryMap repo = ExpiryMap.getInstance();

	static final String APPID = "wx7f3b46a68d0a6098";
	static final String SECRET = "fe512a0e4bfb5e46f3ee9a6755ea38e0";

	static final String ACCESS_TOKEN_KEY = "access_token";
	static final String TICKET_KEY = "ticket";

	private static class SingletonHolder {
		public static DefaultWxTokenRepo instance = new DefaultWxTokenRepo();
	}

	public static DefaultWxTokenRepo getInstance() {
		return SingletonHolder.instance;
	}

	@Override
	public String getAccessToken() {
		// TODO Auto-generated method stub
		if (repo.get(ACCESS_TOKEN_KEY) == null) {
			JSONObject tokenResult = HttpRequestTool.doRequestJson(WxRequestUrl.getAccessTokenUrl(APPID, SECRET));
			repo.put(ACCESS_TOKEN_KEY, tokenResult.getString("access_token"));
		}
		return (String) repo.get(ACCESS_TOKEN_KEY);
	}

	@Override
	public String getTicket() {
		// TODO Auto-generated method stub
		if (repo.get(TICKET_KEY) == null) {
			JSONObject ticketResult = HttpRequestTool
					.doRequestJson("https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=" + getAccessToken()
							+ "&type=jsapi");
			repo.put(TICKET_KEY, ticketResult.getString("ticket"));
		}
		return (String) repo.get(TICKET_KEY);
	}

	public String getAppId() {
		return APPID;
	}
	
	public String getSecret() {
		return SECRET;
	}

}
