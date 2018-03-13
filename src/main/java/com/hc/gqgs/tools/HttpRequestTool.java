package com.hc.gqgs.tools;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * 类名: HttpRequestTool
 * 描述: 网络请求工具类
 */
public class HttpRequestTool {

    private static Logger logger = LoggerFactory.getLogger(HttpRequestTool.class);

    public static JSONObject doRequestJson(String requestUrl) {
        return doRequestJson(requestUrl, "GET", "");
    }

    /**
     * 发起http请求并获取结果("content-type", "application/json")
     *
     * @param requestUrl    请求地址
     * @param requestMethod 请求方式（GET、POST）
     * @param outputStr     提交的数据
     * @return JSONObject(通过JSONObject.get ( key)的方式获取json对象的属性值)
     */
    public static JSONObject doRequestJson(String requestUrl, String requestMethod, String outputStr) {

        JSONObject jsonObject = null;
        StringBuilder builder = new StringBuilder();
        InputStream inputStream = null;
        try {
            URL url = new URL(requestUrl);
            HttpURLConnection httpUrlConn = (HttpURLConnection) url.openConnection();
            httpUrlConn.setDoOutput(true);
            httpUrlConn.setDoInput(true);
            httpUrlConn.setUseCaches(false);
            httpUrlConn.setRequestProperty("content-type", "application/json");
            // 设置请求方式（GET/POST）
            httpUrlConn.setRequestMethod(requestMethod);
            if ("GET".equalsIgnoreCase(requestMethod)) {
                httpUrlConn.connect();
            }

            // 当有数据需要提交时
            if (null != outputStr) {
                OutputStream outputStream = httpUrlConn.getOutputStream();
                // 注意编码格式，防止中文乱码
                outputStream.write(outputStr.getBytes("UTF-8"));
                outputStream.close();
            }
            //将返回的输入流转换成字符串
            inputStream = httpUrlConn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String str = null;
            while ((str = bufferedReader.readLine()) != null) {
                builder.append(str);
            }
            bufferedReader.close();
            inputStreamReader.close();
            // 释放资源
            inputStream.close();
            inputStream = null;
            httpUrlConn.disconnect();
            jsonObject = JSONObject.parseObject(builder.toString());

        } catch (ConnectException ce) {

            logger.error("server connection timed out, " + ce.getMessage());

            return null;

        } catch (Exception e) {

            logger.error(e.getMessage());
            return null;

        } finally {

            try {

                if (inputStream != null) {
                    inputStream.close();
                }

            } catch (IOException e) {
                logger.error(e.getMessage());
            }

        }

        return jsonObject;
    }

}
