package com.hc.gqgs.tools.SIMeID;

import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;

import java.io.*;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/*
 * http请求
 */
public class HttpClientUtil {
	private static Logger logger = Logger.getLogger(HttpClientUtil.class);

	public static JSONObject doRequestJson(String requestUrl, String requestMethod, String outputStr) {
		
		try {
			outputStr = URLEncoder.encode(outputStr, "utf-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return null;
		}
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
			if ("GET".equalsIgnoreCase(requestMethod))
				httpUrlConn.connect();

			// 当有数据需要提交时
			if (null != outputStr) {
				OutputStream outputStream = httpUrlConn.getOutputStream();
				// 注意编码格式，防止中文乱码
				outputStream.write(outputStr.getBytes("UTF-8"));
				outputStream.close();
			}
			// 将返回的输入流转换成字符串
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
			logger.info("HttpRequestTool - result " + builder.toString());
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
				return null;
			}

		}

		return jsonObject;
	}

}
