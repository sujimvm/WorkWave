package com.Team2Project.WorkWave.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

	@GetMapping("/login")
    public String userLogin() {
       return "mainLogin";
    }
	
	/*
	 * @GetMapping("/") public String main() { return "main"; }
	 */
	
}
