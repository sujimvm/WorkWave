package com.Team2Project.WorkWave.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.Team2Project.WorkWave.model.NoticeDTO;
import com.Team2Project.WorkWave.model.NoticeMapper;
import com.Team2Project.WorkWave.model.Page;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class NoticeController {

	@Autowired
	private NoticeMapper noticeMapper;
	
	// 한 페이지당 보여질 게시물의 수
	private final int rowsize = 10;
	
	// DB 상의 전체 게시물의 수
	private int totalRecord = 0;
	
	// 개인회원 FAQ로 이동
	@GetMapping("/userFAQ")
	public String userFAQ() {
		
		return "notice/userFAQ";
	}
	
	// 기업회원 FAQ로 이동
	@GetMapping("/companyFAQ")
	public String companyFAQ() {
		
		return "notice/companyFAQ";
	}
	
	// 공지사항 리스트 페이지 이동 
	@GetMapping("/notice")
	public String noticeList(HttpServletRequest request, Model model) {
		
		int page;	// 현재 페이지 변수
		
		// 페이징 처리 작업
		if(request.getParameter("page") != null) {
			page = Integer.parseInt(request.getParameter("page"));
		}else {
			page = 1;
		}
		
		totalRecord = this.noticeMapper.countNotices();
		
		Page pdto = new Page(page, rowsize, totalRecord);
		
		// 현재 페이지에 해당하는 공지사항을 가져오는 메서드 호출.
		List<NoticeDTO> notice_list = this.noticeMapper.noticeList(pdto);
		
		model.addAttribute("nList", notice_list)
			 .addAttribute("paging", pdto);
		
		return "notice/list";
	}
	
	// 공지사항 상세정보 페이지 이동
	@GetMapping("/noticeCont")
	public String noticeCont(@RequestParam("no") int no, Model model) {
		
		// 공지사항 상세정보 호출
		NoticeDTO notice_cont = this.noticeMapper.noticeCont(no);
		
		model.addAttribute("nCont", notice_cont);
		
		return "notice/cont";
	}
	
	
	
	
}
