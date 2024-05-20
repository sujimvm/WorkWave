package com.Team2Project.WorkWave.model;

import lombok.Data;

@Data
public class UserDTO {
	private String user_key;
	private String user_id;
	private String user_name;
	private String user_pwd;
	private String user_addr;
	private String user_phone;
	private String user_email;
	private String user_birth;
	private String user_join_date;
	private String user_gender;
	private String user_confirm;
    // 생성자, Getter 및 Setter 생략 (필요시 추가)
}
