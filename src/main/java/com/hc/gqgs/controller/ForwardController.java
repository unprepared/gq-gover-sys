package com.hc.gqgs.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import springfox.documentation.annotations.ApiIgnore;

@Controller
@ApiIgnore()
public class ForwardController {
	// 跳转夫妻投靠页面
	@RequestMapping("/CoupleAwartJsp")
	public String callCoupleAwart() {
		return "CoupleAwart";
	}

	// 跳转夫妻投靠成功页面
	@RequestMapping("/CoupleSkipJsp")
	public String callCoupleSkip() {
		return "CoupleSkip";
	}

	// 跳转二次签注页面
	@RequestMapping("/SecondVisaJsp")
	public String callSecondVisa() {
		return "SecondVisa";
	}

	// 跳转二次签注选择首页
	@RequestMapping("/SecondSelectJsp")
	public String callSecondSelect() {
		return "SecondSelect";
	}

	// 跳转二次签注成功页面
	@RequestMapping("/SecondSkipJsp")
	public String callSecondSkip() {
		return "SecondSkip";
	}

	// 跳转二次签注成功页面
	@RequestMapping("/GqPolice")
	public String GqPolice() {
		return "GqPolice";
	}
}
