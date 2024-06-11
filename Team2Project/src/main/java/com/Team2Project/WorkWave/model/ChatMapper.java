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
	
	public int modify(ChatDTO dto);
	
	public int like(int chat_key);
	
	List<ChatReplyDTO> replylist();

    void insertReply(ChatReplyDTO reply);

    ChatReplyDTO getReplyById(int reply_key);
   
    void updateReply(int replyKey, String editedContent);
    
    void deleteReply(int reply_key);
    
    public int replylike(int reply_key);
	
    public int chatCnt(int user_key);
	
}
