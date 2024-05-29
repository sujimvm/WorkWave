package com.Team2Project.WorkWave.model;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class ProfileDTO {
	
	private String profile_ppt_name; 
	private String profile_image_name;
	private int profile_key;
	private int user_key;
	private String profile_name;
	private String profile_image;
	private String profile_job;
	private String profile_group1;
	private String profile_sub1;
	private String profile_step1;
	private String profile_group2;
	private String profile_sub2;
	private String profile_step2;
	private String profile_default;
	private String profile_pay;
	private String profile_ppt;
	private String profile_check;
	private String profile_history;
	private String profile_apply;
	private String profile_character;
	private String profile_plan;

	private String user_name;
	private String user_addr;
	private String user_phone;
	private String user_email;
	private String user_birth;
	private String user_gender;

    // 생성자, Getter 및 Setter 생략 (필요시 추가)
}
