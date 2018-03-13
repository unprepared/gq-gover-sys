package com.hc.gqgs.tools;

import com.alibaba.fastjson.JSONObject;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class ParamUtil {

	/*
	 * 从request流中获取参数
	 */
	public static String getString(HttpServletRequest request) {
		try {
			request.setCharacterEncoding("utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		StringBuilder sb = new StringBuilder();
		try {
			BufferedReader br = new BufferedReader(
					new InputStreamReader((ServletInputStream) request.getInputStream()));
			String line = null;

			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return sb.toString();
	}
	/*
	 * 从request流中获取参数组装为JSON
	 * 如果组装失败则URL解码后组装
	 */
	public static JSONObject getJSON(HttpServletRequest request) {
		String reqStr = getString(request);
		try {
			return JSONObject.parseObject(reqStr);
		} catch (Exception e) {
			try {
				return JSONObject.parseObject(URLDecoder.decode(reqStr, "utf8"));
			} catch (UnsupportedEncodingException ex) {
				ex.printStackTrace();
				return null;
			}
		}
	}
}
