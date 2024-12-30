package com.exam.sbb.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

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
	
	//private List<Article> articles = new ArrayList<>();
	
	private List<Article> articles = new ArrayList<>(
				Arrays.asList( //asList만 쓰면 수정불가능한 불변 데이터 지만  ArrayList<> 로 감싸면 수정가능
						new Article("제목", "내용"), 
						new Article("제목", "내용")
						)
				);
	
	
	@GetMapping("/addArticle")
	@ResponseBody
	public String getSession(@RequestParam("title") String title, @RequestParam("body") String body) {
		//int id = 1;
		//Article article = new Article(id, title, body); 
		Article article = new Article(title, body); 	
		articles.add(article);
		
		//return "%d번 게시물이 생성되었습니다.".formatted(id);
		return "%d번 게시물이 생성되었습니다.".formatted(article.getId());
	}
	
	@GetMapping("/article/{id}")
	@ResponseBody 
	public Article getArticle(@PathVariable("id") int id) {

		Article article = articles //id가 1번인 게시물이 앞에서 3번째
				.stream()						// 1. 스트림 생성
				.filter(a -> a.getId() == id)	// 2. 특정 id와 일치하는 요소 필터링
				.findFirst()					// 3. 조건을 만족하는 첫 번째 요소 찾기
				//.get(); 						// 4. Optional에서 실제 값 가져오기
				.orElse(null);
		
		return article;
	}
	
	@GetMapping("/modifyArticle/{id}")
	@ResponseBody 
	public String modifyArticle(@PathVariable("id") int id, 
								@RequestParam("title") String title, 
								@RequestParam("body") String body) {

		Article article = articles //id가 1번인 게시물이 앞에서 3번째
				.stream()						// 1. 스트림 생성
				.filter(a -> a.getId() == id)	// 2. 특정 id와 일치하는 요소 필터링
				.findFirst()					// 3. 조건을 만족하는 첫 번째 요소 찾기
				.orElse(null); //id매핑 되는 것이 있으면 article에 넣고 아니면 null을 저장

		if(article == null) {
			return "%d번 게시물은 존재하지 않습니다.".formatted(id);
		}
		
		article.setTitle(title);
		article.setBody(body);
		
		return "%d번 게시물을 수정하였습니다..".formatted(article.getId());
	}
	
	
	
	
	
	
	@GetMapping("/deleteArticle/{id}")
	@ResponseBody 
	public String deleteArticle(@PathVariable("id") int id) {

		Article article = articles //id가 1번인 게시물이 앞에서 3번째
				.stream()						// 1. 스트림 생성
				.filter(a -> a.getId() == id)	// 2. 특정 id와 일치하는 요소 필터링
				.findFirst()					// 3. 조건을 만족하는 첫 번째 요소 찾기
				.orElse(null); //id매핑 되는 것이 있으면 article에 넣고 아니면 null을 저장

		if(article == null) {
			return "%d번 게시물은 존재하지 않습니다.".formatted(id);
		}
		
		articles.remove(article);
		
		return "%d번 게시물을 삭제하였습니다..".formatted(article.getId());
	}
	
	
	
	
	
	
	
	@AllArgsConstructor
	@Getter
	@Setter
	class Article {
		private static int lastId = 0;
		//static 변수나 클래스는 프로그램이 실행되면서 딱 한번 실행된다.
		
		private int id;
		private String title;
		private String body;
		
		
		public Article(String title, String body) {
			this(++lastId, title, body); // 다른 생성자 호출 @AllArgsConstructor 가 있어서 모든 매개변수 생성자 존재
		}
		
	}
	
	
	
}













