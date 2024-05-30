package com.Team2Project.WorkWave.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.Team2Project.WorkWave.model.ChatDTO;
import com.Team2Project.WorkWave.model.ChatMapper;

@Controller
public class ChatController {

	@Autowired
	private ChatMapper mapper;
	
	@GetMapping("chat.go")
	public String chat() {
		
		return "chat/main";
	}
	
	@GetMapping("chat_list.go")
	public String list(Model model) {
		
		List<ChatDTO> list = this.mapper.list();
		
		model.addAttribute("List", list);
		
		return "chat/list";
	}
}
