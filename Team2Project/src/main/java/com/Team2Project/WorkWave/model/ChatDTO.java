package com.Team2Project.WorkWave.model;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class ChatDTO {

	private int chat_key;
	private int user_key;
	private String chat_title;
	private String chat_cont;
	private int chat_hit;
	private String chat_date;
	private String chat_update;
	private int chat_like;
	private String chat_tag;
	
	// user 정보
    private String user_id;
    private String user_pwd;
    private String user_name;
    private String user_phone;
    private String user_email;
    private String user_addr;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date user_birth;
    private String user_gender;
    private Date user_join_date;
    private String user_confirm;
}
