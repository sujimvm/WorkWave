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
		
		System.out.println(auth.getAuthorities()+"getAuthorities");
		
		if(auth != null) {
			String role = auth.getAuthorities().toString();
			System.out.println(role);
			String id = auth.getName();
			System.out.println(role);
			if(role.equals("ROLE_COMPANY")) {
				session.setAttribute("role", role);
				session.setAttribute("dto", companyMapper.companyInfo(id));
			}else {
				session.setAttribute("role", role);
				session.setAttribute("dto", userMapper.getUserById(id));
			}
		}else {
			session.setAttribute("role", "ROLE_GUEST");
		}
		
		return "main"; 
	}
	 
}
