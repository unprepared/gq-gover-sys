package com.hc.gqgs.tools;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.springframework.web.multipart.MultipartFile;

public class Makefile {
	public static String makefile(MultipartFile file, String realPath, String filename)
			throws IllegalStateException, IOException {
		File newFile = null;
//		String originalFilename = file.getOriginalFilename();
//		String prefix = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
		// filename = filename + "." + prefix;
		filename = filename + ".png";
		File filePath = new File(realPath, filename);
		if (!filePath.getParentFile().exists()) {
			filePath.getParentFile().mkdirs();
		}
		newFile = new File(realPath + File.separator + filename);
		file.transferTo(newFile);
		return realPath + filename;
	}

	public static int getwidth(String path) throws IOException {
		BufferedImage image = ImageIO.read(new File(path));
		int width = image.getWidth();
		int heigth = image.getHeight();
		if (width > heigth)
			width = 600;
		else
			width = 450;
		return width;
//		return image.getWidth();
	}

	public static int getheight(String path) throws IOException {
		BufferedImage image = ImageIO.read(new File(path));
		int width = image.getWidth();
		int heigth = image.getHeight();
		if (width > heigth)
			heigth = 400;
		else
			heigth = 600;
		return heigth;
//		return image.getHeight();
	}

}
