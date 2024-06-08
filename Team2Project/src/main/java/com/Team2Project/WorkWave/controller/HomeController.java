package com.Team2Project.WorkWave.controller;


import java.security.Principal;

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

	@GetMapping("/") // 삭제 
	public String index(Model model, HttpSession session) {
		
		return "index";
	}
	
	@GetMapping("/login")
	public String userLogin() {
		return "mainLogin";
	}
	
	@GetMapping("/main")
	public String goMain(HttpSession session) { 

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		
		if(auth != null) {
			String role = auth.getAuthorities().toString();
			String id = auth.getName();
			if(role.equals("[ROLE_COMPANY]")) {
				session.setAttribute("role", role);
				session.setAttribute("cDTO", companyMapper.companyInfo(id));
			}else {
				session.setAttribute("role", role);
				session.setAttribute("uDTO", userMapper.getUserById(id));
			}
		}
		
		return "main"; 
	}
	 
}
