package com.Team2Project.WorkWave.model;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class CompanyDTO {
	private int company_key;
	private String company_id;
	private String company_name;
	private String company_pwd;
	private String company_addr;
	private String company_phone;
	private String company_homepage;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date company_regdate;
	
	private Date company_join_date;
	private String company_ceo;
	private String company_confirm;
	private int company_emp_count;
	private int company_sal;
	
	
	private String company_number;
	private String com_board_logo;
	private String company_mgr_name;
	private String company_mgr_phone;
	private String company_mgr_email;


    // 생성자, Getter 및 Setter 생략 (필요시 추가)
}
