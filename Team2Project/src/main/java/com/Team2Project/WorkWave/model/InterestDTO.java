package com.Team2Project.WorkWave.model;

import lombok.Data;

@Data
public class InterestDTO {
	private int interest_key;
	private int company_key;
	private int user_key;
	private String interest_date;

    // 생성자, Getter 및 Setter 생략 (필요시 추가)
}
