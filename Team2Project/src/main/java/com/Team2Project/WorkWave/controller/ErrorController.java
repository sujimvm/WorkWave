package com.Team2Project.WorkWave.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrorController {
	
	@GetMapping("/accessDenied.go")
	public String accessDenied() {
		return "accessDenied";
	}

}
