package com.exam.sbb.controller;

import java.io.IOException;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
public class MainController {
	
	private int increaseNo = -1; //==> 전역변수로 둬야한다. 맨위로 이동해야 한다. 원래는
	
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
	
	
	@GetMapping("/plus")
	@ResponseBody
	public String showPlus(@RequestParam(value="a") int a, @RequestParam(value="b") int b) {
		
		return """
				<h1>입력된 숫자: %d</h1>
				""".formatted(a+b);
	}
	
	
	@GetMapping("/plus2")
	@ResponseBody
	public int showPlus2(@RequestParam(value="a") int a, @RequestParam(value="b") int b) {
		
		return a + b;
	}
	
	
	
	//jsp 에서 배웠던 서블릿 방식 다 수동이다... 스프링부트는 이렇게 안해도 된다.....
	@GetMapping("/plus3")
	@ResponseBody
	public void showPlus3(HttpServletRequest req, HttpServletResponse resp)throws IOException {
		//쿼리스트링 형태의 데이터는 오브젝트 형태로 들어오기 때문에 무조건 형변환해줘야한다.
		//따라서 오브젝트 형태인 /plus3?a=1&b=5 라는 값을 parseInt로 int형태로 형변환 해준다.
		int a = Integer.parseInt(req.getParameter("a"));
		int b = Integer.parseInt(req.getParameter("b"));

		//서블릿 내장객체
		resp.getWriter().append(a + b + "");
	}
	
	
	
	@GetMapping("/minus")
	@ResponseBody
	public String showMinus(@RequestParam(value="a") int a, @RequestParam(value="b") int b) {
		
		return """
				<h1>입력된 숫자: %d</h1>
				""".formatted(a-b);
	}
	
	
	@GetMapping("/minus2")
	@ResponseBody
	public int showMinus2(@RequestParam(value="a") int a, @RequestParam(value="b") int b) {
		
		return a-b;
	}

	 
	@GetMapping("/increase")
	@ResponseBody
	public int showIncrease() {
		//int increaseNo = -1; 전역변수로 선언해야 계속 증가 된다.
		increaseNo++;
		return increaseNo;
	}
	
	
	
	@GetMapping("/gugudan")
	@ResponseBody
	public String showGugudan(@RequestParam(value = "dan") int dan, @RequestParam(value = "limit") int limit) {
		String rs = "";
		
		for (int i=1; i <= limit; i++) {
			rs += "%d * %d = %d<br>\n".formatted(dan, i, dan * i);
		}
		
		return rs;
	}
	
	
	@GetMapping("/gugudan2")
	@ResponseBody
	public String showGugudan2(@RequestParam(value = "dan") Integer dan, @RequestParam(value = "limit") Integer limit) {
		if(dan == null) {
			dan = 9;
		}
		
		if(limit == null) {
			limit = 9;
		}
		
		final Integer finalDan = dan;
		return IntStream.rangeClosed(1, limit) //반복문, 1부터 리미트까지 반복문
				.mapToObj(i -> "%d * %d = %d".formatted(finalDan, i, finalDan * i))
				.collect(Collectors.joining("<br>\n"));
	}
	
	
	
	//쿼리스트링 @RequestParam 과 다른 방식.
	@GetMapping("/mbti/{name}")
	@ResponseBody
	public String showMbti(@PathVariable("name") String name) {
		//자바 높은 버전에서는 break문 생략가능
		String rs = switch (name) {
			case "홍길동" -> "INFP";
			case "홍길순" -> "ENFP";
			case "임꺽정" -> "ESFJ";
			case "송준호" -> "ISFJ";
			default -> "모름";
		};
		return rs;
	}
	
	
	@GetMapping("/mbti2/{name}")
	@ResponseBody
	public String showMbti2(@PathVariable("name") String name) {
		//자바 높은 버전에서는 break문 생략가능
		return switch (name) {
		//자바 최신문법
			case "홍길순" -> {
				char j = 'P';
				yield "ENF"+j; //yield는 return과 같은 문법
			}
			case "임꺽정" -> "ESFJ";
			case "홍길동", "박상원" -> "INFP";
			default -> "모름";
		};
		//return rs;
	}
	
	
	@GetMapping("/saveSession/{name}/{value}")
	@ResponseBody
	public String saveSession(@PathVariable("name") String name, 
								@PathVariable("value") String value, 
								HttpServletRequest req) {
		
		HttpSession session = req.getSession();
		session.setAttribute(name, value);
		return "세션변수 %s의 값이 %s(으)로 설정되었습니다.".formatted(name, value);
	}
	
	
	
	//먼저 위에 /saveSession/{name}/{value} 을 한번 실행 후에 /getSession/{name} 을 치면 value가 
	//쿠키에 저장되어서 나오는데
	//껐다켜거나 아니면 쿠키를 삭제 해주면 /getSession/{name} value값이 null 나온다
	//개발자도구 application에 storage에서 쿠키 영역에 로컬영역 URL을 누르면 JSESSIONID 값이 나오는데 이값이 유지되면 
	//바로 getSession의 value값이 나오고 클리어되거나 날아가면 getSession값의 value값이 null로 나오기 때문에 saveSession을 다시 해줘야한다.
	@GetMapping("/getSession/{name}")
	@ResponseBody
	public String getSession(@PathVariable("name") String name, HttpSession session) {
		//req => 쿠키 => JSESSIONID => 세션을 얻을 수 있다.
		//근데 그냥 HttpServletRequest req 가 아니라 스프링 부트는 HttpSession session 이렇게 선언해서 바로 써도된다.
		//따라서 HttpServletRequest req 선언하고 HttpSession session = req.getSession(); 이렇게 할 필요없이
		//아래 처럼 쓰면되는데 session.getAttribute() 가 오브젝트라 String으로 형변환 해준다.
		String value = (String) session.getAttribute(name); 
		
		return "세션변수 %s의 값은 %s 입니다.".formatted(name, value);
	}
	
	
	
}













