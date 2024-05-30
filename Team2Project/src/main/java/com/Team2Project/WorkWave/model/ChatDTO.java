package com.Team2Project.WorkWave.model;

import lombok.Data;

@Data
public class ChatDTO {

	private String chat_key;
	private String user_key;
	private String title;
	private String cont;
	private Integer hit;
	private String date;
	private String update;
	private Integer like;
	private String tag;
}
