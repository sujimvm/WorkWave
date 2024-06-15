package com.Team2Project.WorkWave.model;

import lombok.Data;

@Data
public class ApplyDTO {
	private int apply_key;
	private int profile_key;
	private int com_board_key;
	private String apply_date;
	private String apply_profile_check;
	private String apply_check;
    private int user_key;
    private int company_key;

    // 생성자, Getter 및 Setter 생략 (필요시 추가)
}
