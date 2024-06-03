package com.Team2Project.WorkWave.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.Team2Project.WorkWave.model.ChatDTO;
import com.Team2Project.WorkWave.model.ChatMapper;

import jakarta.servlet.http.HttpServletResponse;
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
	public String cont(@RequestParam("no") int no, Model model) {
			
		ChatDTO content = this.mapper.getContent(no);
		/*
		 * System.out.println("ChatDTO content: " + content); if (content == null) {
		 * model.addAttribute("errorMessage", "해당 게시글을 찾을 수 없습니다."); } else {
		 * model.addAttribute("cont", content); }
		 */		
		
		model.addAttribute("cont", content);
		
		return "chat/content"; 
	}
	
	@GetMapping("chat_write")
	public String write() {
		
		return "chat/write";
	}
	
	@GetMapping("chat_write_ok.go")
	public void writeok(ChatDTO dto, HttpServletResponse response) throws IOException {
		
		response.setContentType("text/html; charset=UTF-8");
		
		PrintWriter out = response.getWriter();
		
		int result = this.mapper.add(dto);
		
		if(result > 0) {
			out.println("<script>");
			out.println("alert('게시글 추가 성공!!!')");
			out.println("location.href='chat.go'");
			out.println("</script>");
		}else {
			out.println("<script>");
			out.println("alert('게시글 추가 실패')");
			out.println("history.back()");
			out.println("</script>");
		}

	}
	
	@GetMapping("chat_delete")
	public void delete(@RequestParam("no")int chat_key, HttpServletResponse response) throws IOException {
		
		response.setContentType("text/html; charset=UTF-8");
		
		PrintWriter out = response.getWriter();
		
		int result = this.mapper.del(chat_key);
		
		if(result > 0) {
			
			this.mapper.seq(chat_key);
			
			out.println("<script>");
			out.println("alert('게시글 삭제 성공')");
			out.println("location.href='chat.go'");
			out.println("</script>");
		}else {
			out.println("<script>");
			out.println("alert('게시글 삭제 실패')");
			out.println("history.back()");
			out.println("</script>");
		}
	
	}
	
	@GetMapping("chat_modify")
	public String modify(@RequestParam("no")int no, Model model) {
		
		ChatDTO content = this.mapper.getContent(no);
		
		model.addAttribute("modify", content);
		
		return "user/modify";
		
	}
}
