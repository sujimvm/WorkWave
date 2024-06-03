package com.Team2Project.WorkWave.model;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ChatMapper {

	public List<ChatDTO> list();
	
	public ChatDTO getContent(int chat_key);
	
	public int add(ChatDTO dto);
	
	public int del(int no);
	
	public void seq(int no);
	
	public void readcount(int no);
	
	
}
