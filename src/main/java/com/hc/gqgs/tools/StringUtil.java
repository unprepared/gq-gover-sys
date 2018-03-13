package com.hc.gqgs.tools;

import com.alibaba.fastjson.JSONObject;

public class StringUtil {

	public static Boolean isEmptyOrNull(String s) {
		return (s == null || s.length() <= 0);
	}

	public static Boolean isEmptyOrNull(Integer i) {
		return (i == null || i == 0);
	}

	public static Boolean isEmptyOrNull(JSONObject s) {
		return (s == null || s.size() <= 0);
	}

	public static boolean isBlank(final CharSequence cs) {
		int strLen;
		if (cs == null || (strLen = cs.length()) == 0) {
			return true;
		}
		for (int i = 0; i < strLen; i++) {
			if (Character.isWhitespace(cs.charAt(i)) == false) {
				return false;
			}
		}
		return true;
	}

	public static String reverse(final String str) {
		if (str == null) {
			return null;
		}
		return new StringBuilder(str).reverse().toString();
	}

	public static String toStr(String[] strs) {
		String str = "";
		for (String s : strs) {
			str = str + s + ",";
		}
		if (str.length() > 0)
			str = str.substring(0, str.length() - 1);
		return str;
	}
}
