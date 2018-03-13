package com.hc.gqgs.wechat;

import com.alibaba.fastjson.JSONObject;
import com.hc.gqgs.tools.HttpRequestTool;

import java.text.SimpleDateFormat;
import java.util.Date;

public class WxUtils {
	
    static DefaultWxTokenRepo wxToken = DefaultWxTokenRepo.getInstance();
	/**
	 * 发送模板消息给管理员
	 * @param userName 申请人
	 * @param bizId 业务流水号
     * @param business 业务名称
	 */
	public static void sendToM(String Url,String userName,String bizId,String verifyOpenId,String business){
		JSONObject json = new JSONObject();
        json.put("touser", verifyOpenId);
        json.put("template_id", "lj5t88hryo-Z0y7yQaqjOgnUJpX-T6zjMwJ0SBL7Bbo");
//        json.put("url", "http://kingeid.nat300.top/#/verify?verifyOpenId="+managerOpenId+"&bizId="+bizId);
        json.put("url", Url+verifyOpenId+"&bizId="+bizId);
        JSONObject data = new JSONObject();
        JSONObject item = new JSONObject();
        item.put("value", "您好，有待办事件需处理如下");
        item.put("color", "#173177");
        data.put("first", item);
        item = new JSONObject();
        item.put("value", business + "审核");
        item.put("color", "#173177");
        data.put("keyword1", item);
        item = new JSONObject();
        item.put("value", new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date()));
        item.put("color", "#173177");
        data.put("keyword2", item);
        item = new JSONObject();
        item.put("value", userName);
        item.put("color", "#173177");
        data.put("keyword3", item);
        item = new JSONObject();
        item.put("value", "希望您尽快处理");
        item.put("color", "#173177");
        data.put("remark", item);
        json.put("data", data);
        JSONObject result = HttpRequestTool.doRequestJson(WxRequestUrl.getSendUrl(), "POST", json.toJSONString());
	}
	/**
	 * 发送审核通过消息模板给用户
	 * @param openId 用户微信号
	 * @param bizId 业务流水号
     * @param business 业务名称
	 */
	public static void sendToUT(String Url,String openId,String bizId,String business) {
        JSONObject json = new JSONObject();
        json.put("touser", openId);
        json.put("template_id", "tlwzj0cWg8JpSl7Mn1tMGPWXwB2bXtaBqWh_sRQHQks");
//        json.put("url", "http://kingeid.nat300.top/#/trafficmain?"+"bizId="+bizId);
        json.put("url", Url+"bizId="+bizId+"&userOpenId="+openId);
        JSONObject data = new JSONObject();
        JSONObject item = new JSONObject();
        item.put("value", "您好，您的"+ business +"已审核");
        item.put("color", "#173177");
        data.put("first", item);
        item = new JSONObject();
        item.put("value", "通过");
        item.put("color", "#173177");
        data.put("keyword1", item);
        item = new JSONObject();
        item.put("value", new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date()));
        item.put("color", "#173177");
        data.put("keyword2", item);
        item = new JSONObject();
        item.put("value", "祝您工作顺利");
        item.put("color", "#173177");
        data.put("remark", item);
        json.put("data", data);
        JSONObject result = HttpRequestTool.doRequestJson(WxRequestUrl.getSendUrl(), "POST", json.toJSONString());
    }
	/**
	 * 发送审核不通过消息模板给用户
	 * @param openId 用户微信号
	 * @param failMsg 未通过原因
	 * @param bizId 业务流水号
     * @param business 业务名称
	 */
	public static void sendToUF(String Url,String openId,String failMsg,String bizId,String business) {
        JSONObject json = new JSONObject();
        json.put("touser", openId);
        json.put("template_id", "tfVhW-yeE44c025rfwZEallTgL1HLKf-XoY25amId4A");
//        json.put("url", "http://kingeid.nat300.top/#/trafficmain?"+"bizId="+bizId);
        json.put("url", Url+"bizId="+bizId+"&userOpenId="+openId);
        JSONObject data = new JSONObject();
        JSONObject item = new JSONObject();
        item.put("value", "您好，您的"+business+"已审核");
        item.put("color", "#173177");
        data.put("first", item);
        item = new JSONObject();
        item.put("value", "未通过");
        item.put("color", "#173177");
        data.put("keyword1", item);
        item = new JSONObject();
        item.put("value", new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date()));
        item.put("color", "#173177");
        data.put("keyword2", item);
        item = new JSONObject();
        item.put("value", failMsg);
        item.put("color", "#173177");
        data.put("keyword3", item);
        item = new JSONObject();
        item.put("value", "祝您工作顺利");
        item.put("color", "#173177");
        data.put("remark", item);
        json.put("data", data);
        JSONObject result = HttpRequestTool.doRequestJson(WxRequestUrl.getSendUrl(), "POST", json.toJSONString());
    }
}
