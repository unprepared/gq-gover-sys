package com.hc.gqgs.tools;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.tomcat.util.codec.binary.Base64;


public class Base64Image {
	public static void main(String[] args) {
		// 测试从Base64编码转换为图片文件
//		String strImg = "";
//		String path = GenerateImage(strImg, "C:\\Users\\Administrator\\Desktop\\wangyc.jpg");
//		System.out.println(path);
		// 测试从图片文件转换为Base64编码
		// System.out.println(GetImageStr("C:\\Users\\Administrator\\Desktop\\wangyc.jpg"));
	}

	public static byte[] GetImageStr(String imgFilePath) {// 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
		byte[] data = null;

		// 读取图片字节数组
		try {
			InputStream in = new FileInputStream(imgFilePath);
			data = new byte[in.available()];
			in.read(data);
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// 对字节数组Base64编码
		return Base64.encodeBase64(data);
//		BASE64Encoder encoder = new BASE64Encoder();
//		return encoder.encode(data);// 返回Base64编码过的字节数组字符串
	}

	public static String GenerateImage(String imgStr, String imgFilePath) {// 对字节数组字符串进行Base64解码并生成图片
		if (imgStr == null) // 图像数据为空
			return "10001";
//		BASE64Decoder decoder = new BASE64Decoder();
		
		try {
			// Base64解码
			byte[] bytes = Base64.decodeBase64(imgStr);
//			byte[] bytes = decoder.decodeBuffer(imgStr);
			for (int i = 0; i < bytes.length; ++i) {
				if (bytes[i] < 0) {// 调整异常数据
					bytes[i] += 256;
				}
			}
			// 生成jpeg图片
			OutputStream out = new FileOutputStream(imgFilePath);
			out.write(bytes);
			out.flush();
			out.close();
			return imgFilePath;
		} catch (Exception e) {
			e.printStackTrace();
			return "10002";
		}
	}
}