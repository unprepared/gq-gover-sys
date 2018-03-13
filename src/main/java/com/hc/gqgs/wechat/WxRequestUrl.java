package com.hc.gqgs.wechat;

public class WxRequestUrl {
	
	static DefaultWxTokenRepo wxToken = DefaultWxTokenRepo.getInstance();
	
	/**
	 * 获取创建微信菜单的URL
	 * @return
	 */
	public static String getCreateUrl(){
		return "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=" + wxToken.getAccessToken();
	}
	
	/**
	 * 获取发送消息模板的URL
	 * @return
	 */
	public static String getSendUrl(){
		return "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=" + wxToken.getAccessToken();
	}
	
	/**
	 * 获取code的URL(debug模式)
	 * @return
	 */
	public static String getCodeUrl(boolean debug){
		return "https://open.weixin.qq.com/connect/oauth2/authorize?appid="+wxToken.getAppId()+"&redirect_uri=http://kingeid.nat300.top/bin8080/wx/code?debug="+debug+"&response_type=code&scope=snsapi_userinfo&state=123#wechat_redirect";
	}
	
	/**
	 * 获取code的URL
	 * @return
	 */
	public static String getCodeUrl(){
		return "https://open.weixin.qq.com/connect/oauth2/authorize?appid="+wxToken.getAppId()+"&redirect_uri=http://kingeid.nat300.top/bin8080/wx/code?response_type=code&scope=snsapi_userinfo&state=123#wechat_redirect";
	}
	
	/**
	 * 根据code获取用户openid的URL
	 * @param code
	 * @return
	 */
	public static String getOpenIdUrl(String code){
		return "https://api.weixin.qq.com/sns/oauth2/access_token?appid="+wxToken.getAppId()+"&secret="+wxToken.getSecret()+"&code="+code+"&grant_type=authorization_code";
	}
	
	/**
	 * 获取access_token
	 * @param appId
	 * @param secret
	 * @return
	 */
	public static String getAccessTokenUrl(String appId,String secret){
		return "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + appId+ "&secret=" + secret;
	}
}
