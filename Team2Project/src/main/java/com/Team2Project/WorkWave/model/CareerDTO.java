package com.Team2Project.WorkWave.model;

import lombok.Data;

@Data
public class CareerDTO {
	private int career_key;
	private int profile_key;
	private String career_start_date;
	private String career_end_date;
	private String career_cont;
	private String career_loc;
	private String career_position;
	private String career_bye;
	private String career_company;


    // 생성자, Getter 및 Setter 생략 (필요시 추가)
}
