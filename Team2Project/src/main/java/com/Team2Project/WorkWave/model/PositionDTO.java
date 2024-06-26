package com.Team2Project.WorkWave.model;

import lombok.Data;

@Data
public class PositionDTO {
	private int position_key;
	private int user_key;
	private int company_key;
	private int com_board_key;
	private String position_title;
	private String position_cont;
	private String position_check;
	private String position_date;
	private String com_board_title;
	private String user_name;
	// 마감일
	private String expirationDate;

    // 생성자, Getter 및 Setter 생략 (필요시 추가)
}
