package com.Team2Project.WorkWave.model;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import io.lettuce.core.dynamic.annotation.Param;

@Mapper
public interface ChatReplyMapper {
	
	List<ChatReplyDTO> replylist();

    void insertReply(ChatReplyDTO reply);

    ChatReplyDTO getReplyById(int reply_key);
   
    void updateReply(ChatReplyDTO reply);
    
    void deleteReply(int reply_key);
    
    public int replylike(int reply_key);
}
