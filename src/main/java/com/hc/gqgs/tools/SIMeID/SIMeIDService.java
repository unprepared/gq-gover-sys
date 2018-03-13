package com.hc.gqgs.tools.SIMeID;

import com.alibaba.fastjson.JSONObject;
import com.hc.gqgs.config.Config;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SIMeIDService {

    static Logger logger = Logger.getLogger(SIMeIDService.class);
    @Autowired
    Config config;

    final static String SHOW_TIP = "共青政务系统，";

    public JSONObject smsDirect(String mobileNo, String dataToSign) {
        String SERVER_HOST = config.getSERVER_URL();
        int SERVER_PORT = config.getSERVER_PORT();
        String SERVER_CONTEXT = config.getSERVER_CONTEXT();
        String SMS_DIRECT_URL = String.format("http://%s:%s/%s/sms/direct", SERVER_HOST,
                SERVER_PORT, SERVER_CONTEXT);
        logger.info("mobileNo = " + mobileNo + " ,dataToSign = " + dataToSign + " ,showTip = "
                + SHOW_TIP);
        JSONObject reqJSON = new JSONObject();
        reqJSON.put("data_to_sign", dataToSign);
        reqJSON.put("mobile_no", mobileNo);
        reqJSON.put("show_tip", SHOW_TIP);
        JSONObject respJSON = HttpClientUtil.doRequestJson(SMS_DIRECT_URL, "POST", reqJSON.toJSONString());
        logger.info("smsDirect return " + respJSON);
        return respJSON;
    }


}
