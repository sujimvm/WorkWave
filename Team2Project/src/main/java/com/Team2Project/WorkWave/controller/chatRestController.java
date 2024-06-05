package com.Team2Project.WorkWave.controller;

import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.Team2Project.WorkWave.model.ChatMapper;
import com.Team2Project.WorkWave.model.ChatReplyDTO;
import com.Team2Project.WorkWave.model.ChatReplyMapper;

@RestController
public class chatRestController {

	
	@Autowired
	private ChatMapper mapper;
	
	@Autowired
	private ChatReplyMapper replymapper;
	
	
	@RequestMapping("/chats_like.go")
	public Map<String, String> like(@RequestParam Map<String, String> paramMap){
		
		Map<String, String> result = new HashMap<>();
		
		result.put("message", "좋아요 증가!");
		result.put("type", "like");
		int chat_key = (Integer.parseInt(paramMap.get("chat_key")));
		
		this.mapper.like(chat_key);
		
		return result;
	}
	
	@RequestMapping("/reply.go")
	public Map<String, String> reply(@RequestParam Map<String, String> paramMap){
		
		Map<String, String> result = new HashMap<>();
		
		result.put("message", "댓글 등록완료!");
		result.put("type", "reply");
		int chat_key = (Integer.parseInt(paramMap.get("chat_key")));
		String reply_cont = paramMap.get("reply_cont");
		
		ChatReplyDTO reply = new ChatReplyDTO();
		
		reply.setChat_key(chat_key);
		reply.setReply_content(reply_cont);
		
		this.replymapper.insertReply(reply);
		ChatReplyDTO replyOut = this.replymapper.getReplyById(chat_key);
		
		result.put("reply_cont", reply_cont);
		result.put("reply_date", replyOut.getReply_date().toString());
		result.put("user_name", replyOut.getUser_name());
		return result;
	}
	
	@RequestMapping("/reply_like.go")
	public Map<String, String> replylike(@RequestParam Map<String, String> paramMap){
		
		Map<String, String> result = new HashMap<>();
		
		result.put("message", "댓글 좋아요 증가!");
		result.put("type", "replylike");
		
		int reply_key = (Integer.parseInt(paramMap.get("reply_key")));
		
		this.replymapper.replylike(reply_key);
		
		return result;
	}
	
}
	