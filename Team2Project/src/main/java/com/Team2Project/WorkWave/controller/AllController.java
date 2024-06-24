package com.Team2Project.WorkWave.controller;


import java.util.ArrayList;
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
	@Autowired private ProfileMapper profileMapper;

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
	public String list(Model model, HttpServletRequest request, HttpSession session) {
		
		int page;	// 현재 페이지 변수

		// 페이징 처리 작업
		if(request.getParameter("page") != null) {
			page = Integer.parseInt(request.getParameter("page"));
		}else {
			page = 1;
		}

		totalRecord = this.chatMapper.countchat();

		Page pdto = new Page(page, rowsize, totalRecord);
		
		///////////////////////////////////
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String role = auth.getAuthorities().toString();
		
		if(role.equals("[ROLE_USER]")) {
			UserDTO userInfo = (UserDTO)session.getAttribute("uDTO");

			// 이력서의 이미지 사진
			List<ProfileDTO> profileList = this.profileMapper.profileList(userInfo.getUser_key());
			// 내가 작성한 게시물 갯수
			int chatCnt = this.chatMapper.chatCnt(userInfo.getUser_key());
			// 내가 작성한 게시물의 댓글 갯수
			int replyCnt = this.chatMapper.replyCnt(userInfo.getUser_key());
			
			List<ChatDTO> list = this.chatMapper.list(pdto);
			
			for (ChatDTO chat : list) {
	            int replyCount = this.chatMapper.getReplyCount(chat.getChat_key());
	            chat.setChat_reply_count(replyCount);
	        }

			model.addAttribute("List", list).addAttribute("paging", pdto);
			model.addAttribute("chatCnt", chatCnt)
				 .addAttribute("replyCnt", replyCnt)
				 .addAttribute("profileList", profileList);
		}else {
			List<ChatDTO> list = this.chatMapper.list(pdto);


			model.addAttribute("List", list).addAttribute("paging", pdto);
		}
		
		
		
			 
		

		return "chat/list";
	}

	@GetMapping("/chat/content")
	public String cont(@RequestParam("no") int no, HttpSession session, Model model) {

		// 게시물 상세정보
		ChatDTO content = this.chatMapper.getContent(no);
		this.chatMapper.readcount(no);
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String role = auth.getAuthorities().toString();
		
		if(role.equals("[ROLE_USER]")) {
			UserDTO userInfo = (UserDTO)session.getAttribute("uDTO");
			// 이력서의 이미지 사진
			List<ProfileDTO> profileList = this.profileMapper.profileList(userInfo.getUser_key());
			// 내가 작성한 게시물 갯수
			int chatCnt = this.chatMapper.chatCnt(userInfo.getUser_key());
			// 내가 작성한 게시물의 댓글 갯수
			int replyCnt = this.chatMapper.replyCnt(userInfo.getUser_key());
			
			
			
			model.addAttribute("chatCnt", chatCnt)
				 .addAttribute("replyCnt", replyCnt)
				 .addAttribute("profileList", profileList);
		}
		
		model.addAttribute("cont", content);

		// 댓글 리스트
		List<ChatReplyDTO> replylist = this.chatMapper.replylist(no);
		model.addAttribute("list", replylist);


		return "chat/content"; 
	}

	@GetMapping("/main")
	   public String goMain(HttpSession session, Model model) { 

	      int com_board_count = this.comBoardMapper.countComBoard();
	      int user_count = this.userMapper.countUser();
	      int company_count = this.companyMapper.countCompany();
	      List<NoticeDTO> main_notice_list = this.noticeMapper.mainNoticeList();

	      
	      Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	      
	         String role = auth.getAuthorities().toString();
	         String id = auth.getName();
	         if(role.equals("[ROLE_COMPANY]")) {
	            session.setAttribute("role", role);
	            session.setAttribute("cDTO", companyMapper.companyInfo(id));
	            
	            CompanyDTO companyInfo = this.companyMapper.companyInfo(id);
	              
	            int comBoarding = this.companyMapper.companyBoardingCnt(companyInfo.getCompany_key());
				int comBoardEnd =this.companyMapper.companyBoardEndCnt(companyInfo.getCompany_key());
	            int applyNoneCheckCnt = this.companyMapper.applyNoneCheckCnt(companyInfo.getCompany_key());
	            int positionCnt = this.companyMapper.positionCnt(companyInfo.getCompany_key());
	            
	            
	            model.addAttribute("comBoarding", comBoarding)
					.addAttribute("comBoardEnd", comBoardEnd) 
	                .addAttribute("applyNoneCheckCnt", applyNoneCheckCnt) 
	                .addAttribute("positionCnt", positionCnt); 
	            
	         }else if(role.equals("[ROLE_USER]")){
	            session.setAttribute("role", role);
	            session.setAttribute("uDTO", userMapper.getUserById(id));
	            
	            UserDTO userInfo = this.userMapper.getUserById(id);
	            
	            int applyCnt = this.userMapper.applyCnt(userInfo.getUser_key());
	            int applyCheckCnt = this.userMapper.applyCheckCnt(userInfo.getUser_key());
	            int positionJean = this.userMapper.positionJean(userInfo.getUser_key());
	            int interest = this.userMapper.interest(userInfo.getUser_key());
	            String profileName = this.userMapper.profileName(userInfo.getUser_key());
	            List<ProfileDTO> profileList = this.profileMapper.profileList(userInfo.getUser_key());
	                 
	            // 지원완료 갯수
	            model.addAttribute("applyCnt", applyCnt);
	            // 이력서 열람 갯수
	            model.addAttribute("applyCheckCnt", applyCheckCnt);
	            // 포지션 제안 갯수
	            model.addAttribute("positionJean", positionJean);
	            // 관심 기업 갯수
	            model.addAttribute("interest", interest);
	            // 이력서 제목
	            model.addAttribute("profileName", profileName);
	            
	            model.addAttribute("profileList", profileList);
	         }

	      // 모델에 데이터 추가
	      model.addAttribute("mainNoticeList", main_notice_list);
	      model.addAttribute("ComBoardCount", com_board_count);
	      model.addAttribute("UsersCount", user_count);
	      model.addAttribute("CompanyCount", company_count);
	         
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

		totalRecord = this.comBoardMapper.countSearchList(keyword);

		Page pdto = new Page(page, rowsize, totalRecord, keyword);

		List<ComBoardDTO> search_list = this.comBoardMapper.getUnifiedSearchList(pdto);
		
		List<CompanyDTO> search_company_list = new ArrayList<>();
		
		for(ComBoardDTO searchInfo: search_list) {
			CompanyDTO search_company = this.companyMapper.searchCompany(searchInfo.getCompany_key());
			search_company_list.add(search_company);
		}

		model.addAttribute("SearchList", search_list)
			 .addAttribute("paging", pdto)
			 .addAttribute("SearchCompanyList", search_company_list);

		return "unifiedSearch";

	}

	 
}