package com.Team2Project.WorkWave.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.Team2Project.WorkWave.model.ChatDTO;
import com.Team2Project.WorkWave.model.ChatMapper;
import com.Team2Project.WorkWave.model.UserDTO;

import jakarta.servlet.http.HttpSession;



@Controller
public class ChatController {

	@Autowired
	private ChatMapper mapper;
	
	
	@GetMapping("chat.go")
	public String list(Model model) {
		
		List<ChatDTO> list = this.mapper.list();
			
		model.addAttribute("List", list);
		
		return "chat/list";
	}
	
	@GetMapping("chat_cont")
	public String cont(@RequestParam("no") int no, HttpSession session, Model model) {
			
		ChatDTO content = this.mapper.getContent(no);
		ChatDTO user = (ChatDTO)session.getAttribute("member_type");
		
		  // user 객체가 null인지 확인하여 출력
	    if (user == null) {
	        System.out.println("user 객체는 null입니다.");
	    } else {
	        System.out.println("user 객체는 null이 아닙니다.");
	    }
		
		model.addAttribute("cont", content);
		session.setAttribute("user", user);
		
		return "chat/content"; 
	}
	
	
	
	
	@GetMapping("chat_write")
	public String write() {
		
		return "chat/write";
	}
}
