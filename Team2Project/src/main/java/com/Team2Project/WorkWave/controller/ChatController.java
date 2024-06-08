package com.Team2Project.WorkWave.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.Team2Project.WorkWave.model.ChatDTO;
import com.Team2Project.WorkWave.model.ChatMapper;
import com.Team2Project.WorkWave.model.ChatReplyDTO;
import com.Team2Project.WorkWave.model.UserDTO;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;



@Controller
public class ChatController {

	@Autowired
	private ChatMapper chatMapper;
	

	@GetMapping("/chat")
	public String list(Model model) {
		
		List<ChatDTO> list = this.chatMapper.list();
			
		model.addAttribute("List", list);
		
		return "chat/list";
	}
	
	@GetMapping("/chat/content")
	public String cont(@RequestParam("no") int no, HttpSession session, Model model) {
		
		// 게시물 상세정보
		ChatDTO content = this.chatMapper.getContent(no);
		this.chatMapper.readcount(no);
		model.addAttribute("cont", content);
		
		// 댓글 리스트
		List<ChatReplyDTO> replylist = this.chatMapper.replylist();
		model.addAttribute("list", replylist);
		
		
		return "chat/content"; 
	}
	
	@GetMapping("/chat/insert")
	public String write() {
		
		return "chat/write";
	}
	
	@PostMapping("/profile/insertOk")
	public void writeok(ChatDTO dto, HttpServletResponse response) throws IOException {
		
		response.setContentType("text/html; charset=UTF-8");
		 
		PrintWriter out = response.getWriter();
		
		int result = this.chatMapper.add(dto);
		
		if(result > 0) {
			out.println("<script>");
			out.println("alert('게시글 추가 성공!!!')");
			out.println("location.href='/chat'");
			out.println("</script>");
		}else {
			out.println("<script>");
			out.println("alert('게시글 추가 실패')");
			out.println("history.back()");
			out.println("</script>");
		}

	}
	
	@GetMapping("/chat/delete")
	public void delete(@RequestParam("no")int chat_key, HttpServletResponse response) throws IOException {
		
		response.setContentType("text/html; charset=UTF-8");
		
		PrintWriter out = response.getWriter();
		
		int result = this.chatMapper.del(chat_key);
		
		if(result > 0) {
			
			this.chatMapper.seq(chat_key);
			
			out.println("<script>");
			out.println("alert('게시글 삭제 성공')");
			out.println("location.href='/chat'");
			out.println("</script>");
		}else {
			out.println("<script>");
			out.println("alert('게시글 삭제 실패')");
			out.println("history.back()");
			out.println("</script>");
		}
	
	}
	
	@GetMapping("/chat/update")
	public String modify(@RequestParam("no")int no, Model model) {
		
		ChatDTO content = this.chatMapper.getContent(no);
		
		model.addAttribute("modify", content);
		
		return "chat/modify";
	}
	
	@PostMapping("/chat/updateOk")
	public void modifyok(@RequestParam("chat_key") int no, ChatDTO dto, HttpServletResponse response) throws IOException {
		
		response.setContentType("text/html; charset=UTF-8");
		
		PrintWriter out = response.getWriter();
		
		int result = this.chatMapper.modify(dto);
		
		if(result > 0) {
			out.println("<script>");
			out.println("alert('게시글 수정 성공')");
			out.println("location.href='/chat'");
			out.println("</script>");
		}else {
			out.println("<script>");
			out.println("alert('게시글 수정 실패')");
			out.println("history.back()");
			out.println("</script>");
		}
	}
	
	
}
