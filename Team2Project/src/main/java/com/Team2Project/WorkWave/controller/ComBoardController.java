package com.Team2Project.WorkWave.controller;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.Team2Project.WorkWave.model.CodeDTO;
import com.Team2Project.WorkWave.model.ComBoardDTO;
import com.Team2Project.WorkWave.model.ComBoardMapper;
import com.Team2Project.WorkWave.model.CompanyDTO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;



@Controller
@RequestMapping("/comBoard")
public class ComBoardController {

	@Autowired
	private ComBoardMapper mapper;

	// (리스트) 페이지 이동
	@GetMapping("")
	public String goComBoardList() {
		return "comBoard/list";
	}
	
	// (리스트) 리스트 조회
	@PostMapping("/list")
	@ResponseBody
	public List<ComBoardDTO> getJobList() {
		return this.mapper.getJobList();
	}
	
	// (리스트) 업종분류 조회
	@PostMapping("/jobCode")
	@ResponseBody
	public Map<String, List<CodeDTO>> getJobCodeList() {
		
		Map<String, List<CodeDTO>> map = new HashMap<>();
		map.put("group", this.mapper.getJobCodeGroupList());
		map.put("sub", this.mapper.getJobCodeSubList()); 
		map.put("step", this.mapper.getJobCodeStepList());
		
		return map;
	}

	// (리스트) 지역 조회
	@PostMapping("/locationCode")
	@ResponseBody
	public Map<String, List<CodeDTO>> getLocationCodeList() {
		
		Map<String, List<CodeDTO>> map = new HashMap<>();
		map.put("group", this.mapper.getLocationCodeGroupList());
		map.put("sub", this.mapper.getLocationCodeSubList());
		
		return map;
	}
	
	// 기업정보 조회 전송 예정
	// (등록) 페이지 이동
	@GetMapping("/add")
	public String goAddComBoard() {
		return "comBoard/add";
	}

	// (등록) 공고 등록
	@PostMapping("/addOk")
	public String addComBoard(ComBoardDTO dto, HttpSession session) {
		int temp_key = 0;
		// 세션 기업정보로 기업키 저장
//		CompanyDTO cdto = (CompanyDTO)session.getAttribute("companyInfo");
//		dto.setCompany_key(cdto.getCompany_key());
		
		// 컴퍼니 키 임시 저장
		dto.setCompany_key(2);
		
		if(this.mapper.addComBoard(dto) > 0) {
			temp_key = dto.getTemp_key();
			if(temp_key != 0) this.mapper.deleteComBoardTemp(temp_key);
			System.out.println("공고등록 성공");
		}
		return "comBoard/list";
	}
	
	// (등록) 공고 중간저장
	@PostMapping("/addTemp")
	@ResponseBody
	public int addComBoardTemp(ComBoardDTO dto, HttpSession session, HttpServletRequest request) {
		int temp_key = dto.getTemp_key();
		// 세션 기업정보로 기업키 저장
//		CompanyDTO cdto = (CompanyDTO)session.getAttribute("companyInfo");
//		dto.setCompany_key(cdto.getCompany_key());
		
		// 컴퍼니 키 임시 저장
		dto.setCompany_key(2);

		if(temp_key != 0) {
			if(1 > 0) {
				System.out.println("공고 임시저장 수정 성공");
			}
		}else{
			if(this.mapper.addComBoardTemp(dto) > 0) {
				System.out.println("공고 임시저장 등록 성공");
				temp_key = this.mapper.selectTempPk(dto.getCompany_key());
			}
		}
		
		return temp_key;
	}
}
