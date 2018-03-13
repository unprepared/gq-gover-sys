package com.hc.gqgs.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hc.gqgs.json.WebResult;
import com.hc.gqgs.tools.HttpRequestTool;
import com.hc.gqgs.wechat.DefaultWxTokenRepo;
import com.hc.gqgs.wechat.WxRequestUrl;
import com.hc.gqgs.wechat.WxSign;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/wx")
@Api(value = "WxController", tags = { "微信相关接口" })
public class WxController {

    Logger logger = LoggerFactory.getLogger(WxController.class);

    static DefaultWxTokenRepo wxToken = DefaultWxTokenRepo.getInstance();

    /**
     * @param echostr 微信加密签名，signature结合了开发者填写的token参数和请求中的timestamp参数、nonce参数。
     * @param timestamp 时间戳
     * @param nonce 随机数
     * @param echostr 随机字符串
     */
    @RequestMapping("")
    @ResponseBody
    @ApiIgnore
    public Object index(String echostr, String nonce, String signature, String timestamp) {
    	System.out.println(echostr);
        return echostr;
    }

    @RequestMapping("/sign")
    @ResponseBody
    @ApiOperation(value = "获取access_token", httpMethod = "GET", produces = "application/json")
    public Object sign() {
        Map<String,String> result = WxSign.sign(wxToken.getTicket(), "http://kingeid.nat300.top/");
        result.put("appId", wxToken.getAppId());
        return WebResult.success(result);
    }
    
    @RequestMapping("/create")
    @ResponseBody
    @ApiIgnore
    public Object create() {
    	JSONObject json = new JSONObject();
    	JSONArray button = new JSONArray();
    	JSONObject item = new JSONObject();
    	JSONArray sub_button = new JSONArray();
    	JSONObject data = new JSONObject();
    	
    	item.put("name", "共青政务");
        item.put("type", "view");
        item.put("url", WxRequestUrl.getCodeUrl());
    	/*data.put("name", "违章学习");
    	data.put("type", "view");
    	data.put("url", WxRequestUrl.getCodeUrl());
    	sub_button.add(data);
    	item.put("sub_button", sub_button);*/
    	button.add(item);
    	
    	item = new JSONObject();
    	sub_button = new JSONArray();
    	data = new JSONObject();
    	item.put("name", "项目调试");
    	data.put("name", "清空存储");
    	data.put("type", "view");
    	data.put("url", "http://kingeid.nat300.top/#/clear");
    	sub_button.add(data);
    	data = new JSONObject();
    	data.put("name", "提交日志");
    	data.put("type", "view");
    	data.put("url", "http://kingeid.nat300.top/#/debug");
    	sub_button.add(data);
    	data = new JSONObject();
    	data.put("name", "共青政务");
    	data.put("type", "view");
    	data.put("url", WxRequestUrl.getCodeUrl(true));
    	sub_button.add(data);
    	item.put("sub_button", sub_button);
    	button.add(item);
    	
    	/*item = new JSONObject();
    	item.put("name", "完善资料");
    	item.put("key", "write");
    	item.put("type", "click");
    	button.add(item);*/
    	
    	json.put("button", button);
    	JSONObject result = HttpRequestTool.doRequestJson(WxRequestUrl.getCreateUrl(), "POST", json.toJSONString());
    	logger.info("result = " + result.toJSONString());
    	return WebResult.success();
    }
    
	@RequestMapping("/code")
	@ApiOperation(value = "OAuth2.0的第二步", httpMethod = "POST", produces = "application/json")
    public Object test(HttpServletRequest request,boolean debug) {
    	Map map = new HashMap();  
        Enumeration paramNames = request.getParameterNames();  
        while (paramNames.hasMoreElements()) {  
            String paramName = (String) paramNames.nextElement();  
  
            String[] paramValues = request.getParameterValues(paramName);  
            if (paramValues.length == 1) {  
                String paramValue = paramValues[0];  
                if (paramValue.length() != 0) {  
                    map.put(paramName, paramValue);  
                }  
            }  
        }  
  
        Set<Map.Entry<String, String>> set = map.entrySet();  
        String code ="";
        for (Map.Entry entry : set) {  
//            System.out.println(entry.getKey() + ":" + entry.getValue());  
            if(entry.getKey().equals("code"))
            	code = (String) entry.getValue();
        }
        JSONObject result = HttpRequestTool.doRequestJson(WxRequestUrl.getOpenIdUrl(code),"POST","");
        logger.info("result = " + result.toJSONString());
        String time = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        if(debug)
        	return "redirect:http://kingeid.nat300.top/#/?debug=true&userOpenId="+result.getString("openid")+"&time="+time;
        else
        	return "redirect:http://kingeid.nat300.top/#/?userOpenId="+result.getString("openid")+"&time="+time;
    }

    public String getIpAddr(HttpServletRequest request) {
        String ipAddress = request.getHeader("x-forwarded-for");
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
            if (ipAddress.equals("127.0.0.1") || ipAddress.equals("0:0:0:0:0:0:0:1")) {
                //根据网卡取本机配置的IP
                InetAddress inet = null;
                try {
                    inet = InetAddress.getLocalHost();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
                ipAddress = inet.getHostAddress();
            }
        }
        //对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        if (ipAddress != null && ipAddress.length() > 15) { //"***.***.***.***".length() = 15
            if (ipAddress.indexOf(",") > 0) {
                ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
            }
        }
        return ipAddress;
    }
}
