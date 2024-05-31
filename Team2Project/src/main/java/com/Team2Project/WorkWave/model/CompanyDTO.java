package com.Team2Project.WorkWave.model;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

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
	private int company_emp_count;
	private int company_sal;
	
	// 유저가 업로드할 때의 파일 이름
	private String company_logo_name;
	private String company_number;
		
	// 저장된 후 이미지 파일 이름
	private String company_logo;
	private String company_mgr_name;
	private String company_mgr_phone;
	private String company_mgr_email;


    // 생성자, Getter 및 Setter 생략 (필요시 추가)
}
