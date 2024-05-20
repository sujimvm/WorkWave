package com.Team2Project.WorkWave.model;

import lombok.Data;

@Data
public class ProfileDTO {
	private String profile_key;
	private String user_key;
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

    // 생성자, Getter 및 Setter 생략 (필요시 추가)
}
