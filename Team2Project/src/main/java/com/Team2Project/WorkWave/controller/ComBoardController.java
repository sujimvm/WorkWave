package com.Team2Project.WorkWave.controller;


import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.Team2Project.WorkWave.model.ApplyDTO;
import com.Team2Project.WorkWave.model.CodeDTO;
import com.Team2Project.WorkWave.model.ComBoardDTO;
import com.Team2Project.WorkWave.model.ComBoardMapper;
import com.Team2Project.WorkWave.model.CompanyDTO;
import com.Team2Project.WorkWave.model.InterestDTO;
import com.Team2Project.WorkWave.model.NoticeDTO;
import com.Team2Project.WorkWave.model.Page;
import com.Team2Project.WorkWave.model.UserDTO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;



@Controller
@RequestMapping("/comBoard")
public class ComBoardController {

	@Autowired
	private ComBoardMapper mapper;

	// 한 페이지당 보여질 게시물의 수
	private final int rowsize = 5;
	// DB 상의 전체 게시물의 수
	private int totalRecord = 0;
	
	// (리스트) 페이지 이동
	@GetMapping("")
	public String goComBoardList(HttpServletRequest request, Model model) {
		if(request.getParameter("P") != null) {
			model.addAttribute("P",Integer.parseInt(request.getParameter("P")));
		}
		return "/comBoard/list";
	}
	
	// (리스트) 리스트 조회
	@PostMapping("/list")
	@ResponseBody
	public HashMap<String, Object> getComBoardList(HttpSession session, HttpServletRequest request) {
		

		int page;	// 현재 페이지 변수
		
		// 페이징 처리 작업
		if(request.getParameter("page") != null) {
			page = Integer.parseInt(request.getParameter("page"));
		}else {
			page = 1;
		}
		
		totalRecord = this.mapper.countComBoard();
		
		Page pdto = new Page(page, rowsize, totalRecord);
		
		HashMap<String, Object> map = new HashMap<>();
		map.put("list", this.mapper.getComBoardList(pdto));
		map.put("paging", pdto);

		if(session.getAttribute("user_login") == null) {
			System.out.println("getComBoardList > loginX");
		}else {
			UserDTO udto = (UserDTO)session.getAttribute("user_login");
			map.put("interestList", this.mapper.getInterestCompanyKeyList(udto.getUser_key()));
			map.put("applyList", this.mapper.getApplyList(this.mapper.selectDefaultProfile(udto.getUser_key())));
		}
		
		return map;
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
	public String goAddComBoard(HttpSession session) {
		return "/comBoard/add";
	}

	// (등록) 공고 등록
	@PostMapping("/addOk")
	public void addComBoard(ComBoardDTO dto,  HttpServletResponse response, HttpSession session) throws IOException {
		int temp_key = 0;
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out = response.getWriter();

		// 세션 기업정보로 기업키 저장
			CompanyDTO cDTO = (CompanyDTO)session.getAttribute("cDTO");
			dto.setCompany_key(cDTO.getCompany_key());
			
			// 컴퍼니 키 임시 저장
			dto.setCompany_key(2);
			
			if(this.mapper.addComBoard(dto) > 0) {
				temp_key = dto.getTemp_key();
				if(temp_key != 0) this.mapper.deleteComBoardTemp(temp_key);
				out.println("<script> alert('공고등록 성공'); location.href='/comBoard'; </script>");
			}else {
				out.println("<script> alert('공고등록 실패'); history.back(); </script>");
			}
			out.flush();
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
	
	// 관심기업 등록/해제
	@PostMapping("/interestCheck")
	@ResponseBody
	public void interestCheck(@RequestParam("check") int check,@RequestParam("company_key") int company_key, HttpServletRequest request, HttpSession session) {
		
		InterestDTO iDTO = new InterestDTO();
		// 세션 개인횡정보로 회원키 저장
		UserDTO udto = (UserDTO)session.getAttribute("uDTO");
		iDTO.setCompany_key(company_key);
		iDTO.setUser_key(udto.getUser_key());
		
		// 1 = check , 0 = uncheck
		if(check == 1) {
			this.mapper.insertInterestCheck(iDTO);
			System.out.println("insertInterestCheck");
		}else{
			this.mapper.deleteInterestCheck(iDTO);
			System.out.println("deleteInterestCheck");
		}
	}
	
	// 공고 지원
	@PostMapping("/addApply")
	@ResponseBody
	public void addApply(@RequestParam("checked") String checked, HttpServletRequest request, HttpSession session) {

		UserDTO udto = (UserDTO)session.getAttribute("uDTO");
		int profile_key = this.mapper.selectDefaultProfile(udto.getUser_key());
		
		System.out.println(checked+"checked");
		for (int i = 0; i < checked.split(",").length; i++) {
			ApplyDTO aDTO = new ApplyDTO();
			
			aDTO.setProfile_key(profile_key);
			aDTO.setCom_board_key(Integer.parseInt(checked.split(",")[i]));
			
			if(this.mapper.addApply(aDTO) > 0) System.out.println("지원성공");
			else System.out.println("지원실패");
		}
	}
	
	// (상세보기) 페이지 이동
	@GetMapping("/content")
	public String goComBoardContent(HttpServletRequest request, Model model) {
		
		int com_board_key = Integer.parseInt(request.getParameter("No"));
		int page = Integer.parseInt(request.getParameter("P"));
		
		ComBoardDTO dto = this.mapper.getComBoard(com_board_key);
		
		model.addAttribute("dto",dto).addAttribute("P",page);
		
		return "/comBoard/content";
	}
	
	// (상세보기) 페이지 이동
	@GetMapping("/update")
	public String goComBoardUpdate(HttpServletRequest request, Model model) {
		
		int com_board_key = Integer.parseInt(request.getParameter("No"));
		
		ComBoardDTO dto = this.mapper.getComBoard(com_board_key);
		
		model.addAttribute("dto",dto);
		
		return "/comBoard/update";
	}
	
	// (등록) 공고 수정
	@PostMapping("/updateOk")
	public void updateComBoard(ComBoardDTO dto,  HttpServletResponse response, HttpSession session) throws IOException {
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out = response.getWriter();
		
		System.out.println("dto.getCom_board_key()sssssss");
		System.out.println(dto.getCom_board_key()+"dto.getCom_board_key()");
		
		if(this.mapper.updateComBoard(dto) > 0) {
			out.println("<script> alert('공고수정 성공'); location.href='/comBoard'; </script>"); // 마이페이지로 변경 예정
		}else {
			out.println("<script> alert('공고수정 실패'); history.back(); </script>");
		}
		out.flush();
	}
}
