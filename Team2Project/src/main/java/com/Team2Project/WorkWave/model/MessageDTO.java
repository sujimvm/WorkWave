package com.Team2Project.WorkWave.model;

import lombok.Data;

@Data
public class MessageDTO {
	
	private String id;
	private String phone;
	private String verify_code;
	
	public MessageDTO(String phone, String code) {
		this.phone=phone;
		this.verify_code=code;
	}

}
