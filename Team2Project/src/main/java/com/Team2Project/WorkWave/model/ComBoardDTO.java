package com.Team2Project.WorkWave.model;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class ComBoardDTO {
    private int com_board_key; // Primary Key
    private int company_key; // Foreign Key referencing company_key in company table
    
    private int temp_key; // Foreign Key referencing company_key in company table
    
    private String com_board_title; // 공고명
    private String com_board_group; // 대분류
    private String com_board_sub; // 중분류
    private String com_board_step; // 소분류
    private String com_board_jobtype; // 고용형태
    private String com_board_mojib; // 모집인원
    
    private String com_board_sal; // 급여
    private String com_board_week; // 근무요일
    private String com_board_time; // 근무시간
    private String com_board_benefits; // 복리후생

    private String com_board_career; // 경력
    private String com_board_edu; // 학력
    private String com_board_conditions; // 우대조건

	@DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date com_board_start_date; // 공고시작일
	@DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date com_board_end_date; // 공고마감일

    private String com_board_cont; // 공고내용

	@DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date com_board_date; // 공고등록일

    private String com_board_mgr_name; // 담당자 이름
    private String com_board_mgr_phone; // 담당자 전화번호
    private String com_board_mgr_email; // 담당자 이메일

	//기업정보
	private String company_id;
	private String company_name;
	private String company_addr;
	private String company_phone;
	private String company_homepage;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date company_regdate;
	
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
	
}
