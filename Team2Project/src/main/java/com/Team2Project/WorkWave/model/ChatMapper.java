package com.Team2Project.WorkWave.model;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ChatMapper {

	public List<ChatDTO> list(Page pdto);
	
	public ChatDTO getContent(int chat_key);
	
	int countchat();
	
	public int add(ChatDTO dto);
	
	public int del(int no);
	
	public void seq(int no);
	
	public void readcount(int no);
	
	public int modify(ChatDTO dto);
	
	public int like(int chat_key);
	
	public int chatcnt(int no);
	
	List<ChatReplyDTO> replylist(int no);

    void insertReply(ChatReplyDTO reply);

    ChatReplyDTO getReplyById(int reply_key);
   
    void updateReply(ChatReplyDTO reply);
    
    void deleteReply(int reply_key);
    
    public int replylike(int reply_key);
	
    public int chatCnt(int user_key);
    
    List<ChatReplyDTO> getRepliesByChatKey(int chat_key);
	
}
