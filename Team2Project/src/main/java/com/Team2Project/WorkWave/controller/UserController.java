package com.Team2Project.WorkWave.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.Team2Project.WorkWave.model.UserMapper;



@Controller
public class UserController {

	@Autowired
	private UserMapper mapper;
	
	
}
