package com.hc.gqgs.tools;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;


import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.data.PictureRenderData;
import com.deepoove.poi.render.RenderAPI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ToWord {
	static Logger logger = LoggerFactory.getLogger(ToWord.class);

	private static ToWord ourInstance = new ToWord();

	public static ToWord getInstance() {
		return ourInstance;
	}

	private ToWord() {
	}

	/*s*/

	public void toWord(Map<String, Object> input, String realPath, String toPath) throws IOException {
//		InputStream stream = getClass().getClassLoader().getResourceAsStream("template/" + fileName);
		XWPFTemplate doc = XWPFTemplate.compile(realPath);
//		XWPFTemplate doc = XWPFTemplate.compile(stream);
		RenderAPI.render(doc, input);
		FileOutputStream out = new FileOutputStream(toPath);
		doc.write(out);
		out.flush();
		out.close();
	}
	
	public static PictureRenderData toPicture(String photoPath) throws IOException{
		return new PictureRenderData(Makefile.getwidth(photoPath), Makefile.getheight(photoPath), photoPath);
	}
}
