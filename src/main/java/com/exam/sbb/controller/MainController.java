package com.exam.sbb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MainController {
	@RequestMapping("/sbb")
	
	//아래 함수의 리턴값을 그대로 브라우저에 표시
	//아래 함수의 리턴값을 문자열화해서 브라우저 응답을 바디에 담는다.
	@ResponseBody//우리가 이런식으로 쓴 요청을 바디에다가 출력을 해줘(HTML안에 HEAD영역과 바디영역이 있음)
	public String index() {
		//System.out.println("첫시작");
		return "안녕하세요.";
	}
}
