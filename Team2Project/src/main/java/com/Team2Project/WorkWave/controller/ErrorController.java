package com.Team2Project.WorkWave.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/error")
public class ErrorController {

	@ExceptionHandler(Exception.class)
	public String handleGlobalExceptions(HttpServletRequest request) {

		return "accessDenied";
	}

	@GetMapping("/404")
	public String error404() {
		
		return "accessDenied";
	}

	@GetMapping("/403")
	public String error403() {
		
		return "accessDenied";
	}
}
