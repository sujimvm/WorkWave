package com.Team2Project.WorkWave.model;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatReplyDTO {

    private int reply_key;
    private int chat_key;
    private int user_key;
    private String user_id;
    private Date reply_date;
    private String reply_content;
   
    
    
	
}
