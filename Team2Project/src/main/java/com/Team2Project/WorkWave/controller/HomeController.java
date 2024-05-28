package com.Team2Project.WorkWave.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpSession;


@Controller
public class HomeController {

	@GetMapping("/")
	public String index() {
		return "index";
	}
	
	@GetMapping("user.go")
	public String userLogin() {
		return "user/login";
	}
	
	@GetMapping("company.go")
	public String companyLogin() {
		return "company/login";
	}

	@GetMapping("logout")
	public String logout(HttpSession session) {
		session.invalidate(); //세션의 모든 속성을 삭제
		return "/";
	}
	
	@GetMapping("main.go")
	public String goMain(HttpSession session) {
		String member_type = "";
		if (session.getAttribute("member_type") != null) {
			member_type = (String)session.getAttribute("member_type") + "/";
		}
		return member_type+"main";
	}
	
}
