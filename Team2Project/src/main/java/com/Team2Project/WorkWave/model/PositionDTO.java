package com.Team2Project.WorkWave.model;

import lombok.Data;

@Data
public class PositionDTO {
	private String position_key;
	private String user_key;
	private String company_key;
	private String com_board_key;
	private String position_title;
	private String position_cont;
	private String position_check;
	private String position_date;

    // 생성자, Getter 및 Setter 생략 (필요시 추가)
}
