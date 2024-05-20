package com.Team2Project.WorkWave.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class HomeController {

	@GetMapping("/")
	public String index() {
		return "index";
	}
	
	@GetMapping("member.go")
	public String memberLogin() {
		return "member/login";
	}
	
	@GetMapping("company.go")
	public String companyLogin() {
		return "company/login";
	}
	
	@GetMapping("guest.go")
	public String guestLogin() {
		return "main";
	}
	
}
