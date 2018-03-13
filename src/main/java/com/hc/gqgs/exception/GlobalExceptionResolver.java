package com.hc.gqgs.exception;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.hc.gqgs.json.ERRORDetail;
import com.hc.gqgs.json.WebResult;
import org.springframework.web.servlet.ModelAndView;

/*
 * web全局异常处理器
 */
@ControllerAdvice
public class GlobalExceptionResolver {

	Logger logger = LoggerFactory.getLogger(GlobalExceptionResolver.class);


	@ExceptionHandler(value = Exception.class)
	@ResponseBody
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
										 Exception ex) {
		// TODO Auto-generated method stub
		WebResult result = null;
//		if(ex instanceof UnauthorizedException){
//			result = WebResult.error(ERRORDetail.GQ_0103002);
//		}else{
			ex.printStackTrace();
			result = WebResult.error(ERRORDetail.GQ_0401001);
//		}
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json");
		try {
			response.getWriter().write(JSONObject.toJSONString(result));
		} catch (IOException e) {
			e.printStackTrace();
		}
    	return null;
	}

}
