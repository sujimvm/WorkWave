package com.Team2Project.WorkWave.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.Team2Project.WorkWave.model.CompanyDTO;
import com.Team2Project.WorkWave.model.CompanyMapper;
import com.Team2Project.WorkWave.model.UserDTO;
import com.Team2Project.WorkWave.model.UserMapper;

import jakarta.servlet.http.HttpSession;


@Controller
public class HomeController {
	
	@Autowired
	private CompanyMapper companyMapper;
	
	@Autowired
	private UserMapper userMapper;

	@GetMapping("/")
	public String index(Model model, HttpSession session) {
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		
		String user = auth.getName();
		
		session.setAttribute("user_session", user);
		
		return "index";
	}
	
	@GetMapping("/login.go")
	public String userLogin() {
		return "mainLogin";
	}
	
	@GetMapping("/clogin.go")
	public String companyLogin() {
		return "company/login";
	}

	
	@GetMapping("/main.go")
	public String goMain(HttpSession session) { 
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		System.out.println("안녕");
		System.out.println(auth.toString());
		
		String user = auth.getName();
		
		CompanyDTO cdto = this.companyMapper.companyInfo(user);
		
		if(cdto == null) {
			
			session.setAttribute("User_session", user);
			
			return "user/main";
		}
		
		session.setAttribute("Company_session", user);
	
		return "company/main"; 
	}
	 
}
