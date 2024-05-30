package com.Team2Project.WorkWave.model;

import lombok.Data;

@Data
public class ChatDTO {

	private String chat_key;
	private String user_key;
	private String chat_title;
	private String chat_cont;
	private int chat_hit;
	private String chat_date;
	private String chat_update;
	private int chat_like;
	private String chat_tag;
}
