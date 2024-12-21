package com.exam.sbb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
	
	@GetMapping("/page1")
	@ResponseBody
	public String showGet() {
		
		return """
<!--				<h1>안녕하세요.</h1>							   -->
<!--				<input type="text" placeholder="입력해주세요."/> -->
				<form method="POST" action="/page2" />
					<input type="number" name="age" placeholder="나이입력" />
					<input type="submit" value="page2로 POST방식으로 이동" />
				</form>
				""";
	}
	
	@GetMapping("/page2")
	@ResponseBody
	public String showPost(@RequestParam(value="age", defaultValue = "0") int age) {
		
		return """
				<h1>입력된 나이: %d</h1>
				<h1>안녕하세요. GET방식으로 오신걸 환영합니다.</h1>
				<form method="POST" action="/page2"/>
					<input type="submit" value="page2로  POST방식으로 이동" />
				</form>
				""".formatted(age);
	}
	
	@PostMapping("/page2")
	@ResponseBody
	//이유는 모르겠지만 지금 sts에서 -parameters가 적용이 안된다... 그래서 이렇게 꼭 명시적으로 써줘야한다.
	public String showPage2Post(@RequestParam(value="age", defaultValue = "0") int age) {
	//public String showPage2Post(@RequestParam(defaultValue = "0") int age) { 
	//public String showPage2Post(@RequestParam("age") int age) {
	//public String showPage2Post(int age) {  //==>이게 sts에서는 @RequestParam 어노테이션 없이 안된다.. 이유는 잘...
		 
		return """
				<h1>입력된 나이: %d</h1>
				<h1>안녕하세요. POST방식으로 오신걸 환영합니다.</h1>
				""".formatted(age);
	}
	
}
