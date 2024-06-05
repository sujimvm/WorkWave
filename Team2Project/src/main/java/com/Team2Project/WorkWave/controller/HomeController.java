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
	
	@GetMapping("/ulogin.go")
	public String userLogin() {
		return "mainLogin";
	}
	
	@GetMapping("/clogin.go")
	public String companyLogin() {
		return "company/login";
	}

	/*
	 * @GetMapping("main.go") public String goMain(HttpSession session) { String
	 * member_type = ""; if (session.getAttribute("member_type") != null) {
	 * member_type = (String)session.getAttribute("member_type") + "/"; }
	 * 
	 * System.out.println("메인 경로 >> "+member_type+"main"); return
	 * member_type+"main"; }
	 */
	
	@GetMapping("/main.go")
	public String companyMain() {
		return "company/main";
	}
	
	@GetMapping("/user_main.go")
	public String userMain() {
		return "user/main";
	}
}
