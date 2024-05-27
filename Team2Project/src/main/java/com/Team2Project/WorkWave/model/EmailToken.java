package com.Team2Project.WorkWave.model;

import lombok.Data;

@Data
public class EmailToken {

	private String id;
    private String email;
    private String token;
    private String verificationCode;

    public EmailToken(String email, String token, String verificationCode) {
        this.email = email;
        this.token = token;
        this.verificationCode = verificationCode;
    }
	
}
