package com.Team2Project.WorkWave.controller;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.Team2Project.WorkWave.model.*;
import com.Team2Project.WorkWave.service.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;


@Controller
@RequestMapping("/A")
public class AllController {

	@Autowired private ChatMapper chatMapper;
	@Autowired private ComBoardMapper comBoardMapper;
	@Autowired private CompanyMapper companyMapper;
	@Autowired private NoticeMapper noticeMapper;
	@Autowired private UserMapper userMapper;

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

	@GetMapping("/chat")
	public String list(Model model, HttpServletRequest request) {
		
		int page;	// 현재 페이지 변수

		// 페이징 처리 작업
		if(request.getParameter("page") != null) {
			page = Integer.parseInt(request.getParameter("page"));
		}else {
			page = 1;
		}

		totalRecord = this.chatMapper.countchat();

		Page pdto = new Page(page, rowsize, totalRecord);

		List<ChatDTO> list = this.chatMapper.list(pdto);

		model.addAttribute("List", list).addAttribute("paging", pdto);

		return "chat/list";
	}

	@GetMapping("/chat/content")
	public String cont(@RequestParam("no") int no, HttpSession session, Model model) {

		// 게시물 상세정보
		ChatDTO content = this.chatMapper.getContent(no);
		this.chatMapper.readcount(no);
		model.addAttribute("cont", content);

		// 댓글 리스트
		List<ChatReplyDTO> replylist = this.chatMapper.replylist(no);
		model.addAttribute("list", replylist);


		return "chat/content"; 
	}

	@GetMapping("/main")
	public String goMain(HttpSession session) { 

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		System.out.println(auth +"gd");
		
			String role = auth.getAuthorities().toString();
			String id = auth.getName();
			if(role.equals("[ROLE_COMPANY]")) {
				session.setAttribute("role", role);
				session.setAttribute("cDTO", companyMapper.companyInfo(id));
			}else if(role.equals("[ROLE_USER]")){
				session.setAttribute("role", role);
				session.setAttribute("uDTO", userMapper.getUserById(id));
			}

		return "main"; 
	}

	// (리스트) 페이지 이동
	@GetMapping("/comBoard")
	public String goComBoardList(HttpServletRequest request, Model model) {
		if(request.getParameter("P") != null) {
			model.addAttribute("P",Integer.parseInt(request.getParameter("P")));
		}
		return "/comBoard/list";
	}

	
	// (상세보기) 페이지 이동

	@GetMapping("/comBoard/content") 
	public String goComBoardContent(HttpServletRequest request, Model model) {

		int com_board_key = Integer.parseInt(request.getParameter("No"));

		HashMap<String, Object> map = new HashMap<>(); 
		map.put("dto", this.comBoardMapper.getComBoard(com_board_key));
		map.put("P", Integer.parseInt(request.getParameter("P")));
		map.put("applyTotal", this.comBoardMapper.getApplyCount(com_board_key));
		map.put("avgAge", this.comBoardMapper.getApplyAvgAge(com_board_key));
		map.put("avgGender", this.comBoardMapper.getApplyAvgGender(com_board_key));
		map.put("avgEdu", this.comBoardMapper.getApplyAvgEdu(com_board_key));

		model.addAttribute("map",map);

		return "/comBoard/content"; 

	}

	// 키워드로 검색하면 통합검색 페이지로 이동하여 키워드 포함한 공고 리스트 출력
	@GetMapping("/unifiedSearchList")
	public String getUnifiedSearchList(@RequestParam("keyword") String keyword,
			HttpServletRequest request,
			Model model) {

		int page;	// 현재 페이지 변수

		// 페이징 처리 작업
		if(request.getParameter("page") != null) {
			page = Integer.parseInt(request.getParameter("page"));
		}else {
			page = 1;
		}

		totalRecord = this.chatMapper.countchat();

		Page pdto = new Page(page, rowsize, totalRecord, keyword);

		List<ComBoardDTO> search_list = this.comBoardMapper.getUnifiedSearchList(pdto);

		model.addAttribute("SearchList", search_list).addAttribute("paging", pdto);

		return "unifiedSearch";

	}

	 
}