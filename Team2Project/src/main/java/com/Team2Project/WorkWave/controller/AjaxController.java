package com.Team2Project.WorkWave.controller;


import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.Team2Project.WorkWave.model.*;
import com.Team2Project.WorkWave.service.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;


@RestController
@RequestMapping("/ajax")
public class AjaxController {

   @Autowired private ChatMapper chatMapper;
   @Autowired private ComBoardMapper comBoardMapper;
   @Autowired private CompanyMapper companyMapper;
   @Autowired private NoticeMapper noticeMapper;
   @Autowired private ProfileMapper profileMapper;
   @Autowired private UserMapper userMapper;

   @Autowired private MessageMapper messageMapper; 
   @Autowired private TokenMapper tokenMapper; 
   @Autowired private PasswordEncoder passwordEncoder;

   @Autowired private EmailService emailService;
   @Autowired private MessageService messageService;
   @Autowired private TokenService tokenService;
   @Autowired private UploadFileService uploadFileService;
   @Autowired private UserService userService;
   
	// 한 페이지당 보여질 게시물의 수
	private final int rowsize = 5;
	// DB 상의 전체 게시물의 수
	private int totalRecord = 0;
	
	// 아이디 중복검사
	@PostMapping("/checkCompanyId")
	//@ResponseBody
	public String companyIdCheck(@RequestParam("id") String comId, HttpServletRequest request,
		HttpServletResponse response) throws IOException {
		String res = "available";
		response.setContentType("text/html; charset=UTF-8");
		CompanyDTO idCheck = this.companyMapper.companyInfo(comId);
		UserDTO userIdCheck = this.userMapper.getUserById(comId);
		if (idCheck != null || userIdCheck != null) {
			res = "unavailable";
		}
		return res;
	}
	
	@PostMapping("/companyNumberCheck")
	@ResponseBody
	public String companyNoCheck(@RequestParam("company_no") String company_no,HttpServletRequest request,
		HttpServletResponse response) {
		String res = "available";
		response.setContentType("text/html; charset=UTF-8");
		String str1 = company_no.substring(0, 3);
		String str2 = company_no.substring(3, 5);
		String str3 = company_no.substring(5);
		String newCompanyNo = str1 + "-" + str2 + "-" + str3;
		CompanyDTO idCheck = this.companyMapper.companyInfoByNo(newCompanyNo);
		if (idCheck != null) {
			res = "unavailable";
		}
		return res;
	}
	
	   // 아이디 유효성 검사
	   @PostMapping("/checkUserId")
	   //@ResponseBody
	   public Map<String, String> checkUserId(@RequestBody Map<String, String> request) {
	      String userId = request.get("userId");
	      boolean isAvailable = userService.isUserIdAvailable(userId);
	      CompanyDTO companyIdCheck = this.companyMapper.companyInfo(userId);
	      Map<String, String> response = new HashMap<>();
	      if (isAvailable && companyIdCheck == null) {
	         response.put("status", "available");
	      } else {
	         response.put("status", "unavailable");
	      }
	      return response;
	   }
	   
		// 중분류 카테고리
		@PostMapping("/jobCodeGroup")
		//@ResponseBody
		public List<CodeDTO> categorysub(@RequestParam("no") String no) {
			List<CodeDTO> categorysub = this.profileMapper.categorysub(no);
			return categorysub;
		}

		// 소분류 카테고리
		@PostMapping("/jobCodesub")
		//@ResponseBody
		public List<CodeDTO> categorystep(@RequestParam("no") String no) {
			List<CodeDTO> categorystep = this.profileMapper.categorystep(no);
			return categorystep;

		}
		// 학교구분과 학교 이름 검색 시 해당 학교 리스트 불러오기
		@PostMapping("/searchSchoolByName")
		//@ResponseBody
		public List<CodeDTO> schoolName(CodeDTO dto) {
			List<CodeDTO> schoolName = this.profileMapper.schoolname(dto);
			return schoolName;

		}

		// 학교구분과 전공명 검색 시 해당 전공 데이터 불러오기
		@PostMapping("/getDepartment")
		//@ResponseBody
		public List<CodeDTO> departmentName(CodeDTO dto) {
			List<CodeDTO> departmentName = this.profileMapper.department(dto);
			return departmentName;

		}

		// 해당 자격증 리스트 불러오기
		@PostMapping("/searchCertifications")
		//@ResponseBody
		public List<LicenseDTO> searchlicense(@RequestParam("license_name") String license_name) {
			List<LicenseDTO> licenseList = this.profileMapper.license(license_name);
			return licenseList;

		}
		@RequestMapping("/chatLike")
		public Map<String, String> like(@RequestParam Map<String, String> paramMap){
			Map<String, String> result = new HashMap<>();
			result.put("message", "좋아요 증가!");
			result.put("type", "like");
			int chat_key = (Integer.parseInt(paramMap.get("chat_key")));
			this.chatMapper.like(chat_key);
			return result;
		}
		
		@RequestMapping("/reply")
		public Map<String, String> reply(@RequestParam Map<String, String> paramMap){
			Map<String, String> result = new HashMap<>();
			result.put("message", "댓글 등록완료!");
			result.put("type", "reply");
			int chat_key = (Integer.parseInt(paramMap.get("chat_key")));
			String reply_cont = paramMap.get("reply_cont");
			ChatReplyDTO reply = new ChatReplyDTO();
			reply.setChat_key(chat_key);
			reply.setReply_content(reply_cont);
			this.chatMapper.insertReply(reply);
			ChatReplyDTO replyOut = this.chatMapper.getReplyById(chat_key);
			result.put("reply_cont", reply_cont);
			result.put("reply_date", replyOut.getReply_date().toString());
			result.put("user_name", replyOut.getUser_name());
			return result;
		}
		
		@RequestMapping("/replyLike")
		public Map<String, String> replylike(@RequestParam Map<String, String> paramMap){
			Map<String, String> result = new HashMap<>();
			result.put("message", "댓글 좋아요 증가!");
			result.put("type", "replylike");
			int reply_key = (Integer.parseInt(paramMap.get("reply_key")));
			this.chatMapper.replylike(reply_key);
			return result;
		}

		// (리스트) 리스트 조회
		@PostMapping("/comBoardList")
		//@ResponseBody
		public HashMap<String, Object> getComBoardList(HttpSession session, HttpServletRequest request) {
			

			int page;	// 현재 페이지 변수
			
			// 페이징 처리 작업
			if(request.getParameter("page") != null) {
				page = Integer.parseInt(request.getParameter("page"));
			}else {
				page = 1;
			}
			
			totalRecord = this.comBoardMapper.countComBoard();
			
			Page pdto = new Page(page, rowsize, totalRecord);
			
			HashMap<String, Object> map = new HashMap<>();
			map.put("list", this.comBoardMapper.getComBoardList(pdto));
			map.put("paging", pdto);

			if(session.getAttribute("user_login") == null) {
				System.out.println("getComBoardList > loginX");
			}else {
				UserDTO udto = (UserDTO)session.getAttribute("user_login");
				map.put("interestList", this.comBoardMapper.getInterestCompanyKeyList(udto.getUser_key()));
				map.put("applyList", this.comBoardMapper.getApplyList(this.comBoardMapper.selectDefaultProfile(udto.getUser_key())));
			}
			
			return map;
		}
		
		// (리스트) 업종분류 조회
		@PostMapping("/jobCode")
		//@ResponseBody
		public Map<String, List<CodeDTO>> getJobCodeList() {
			
			Map<String, List<CodeDTO>> map = new HashMap<>();
			map.put("group", this.comBoardMapper.getJobCodeGroupList());
			map.put("sub", this.comBoardMapper.getJobCodeSubList()); 
			map.put("step", this.comBoardMapper.getJobCodeStepList());
			
			return map;
		}

		// (리스트) 지역 조회
		@PostMapping("/locationCode")
		//@ResponseBody
		public Map<String, List<CodeDTO>> getLocationCodeList() {
			
			Map<String, List<CodeDTO>> map = new HashMap<>();
			map.put("group", this.comBoardMapper.getLocationCodeGroupList());
			map.put("sub", this.comBoardMapper.getLocationCodeSubList());
			
			return map;
		}
		// (등록) 공고 중간저장
		@PostMapping("/comBoardTemp/insert")
		//@ResponseBody
		public int addComBoardTemp(ComBoardDTO dto, HttpSession session, HttpServletRequest request) {
			int temp_key = dto.getTemp_key();
			// 세션 기업정보로 기업키 저장
//			CompanyDTO cdto = (CompanyDTO)session.getAttribute("companyInfo");
//			dto.setCompany_key(cdto.getCompany_key());
			
			// 컴퍼니 키 임시 저장
			dto.setCompany_key(2);

			if(temp_key != 0) {
				if(1 > 0) {
					System.out.println("공고 임시저장 수정 성공");
				}
			}else{
				if(this.comBoardMapper.addComBoardTemp(dto) > 0) {
					System.out.println("공고 임시저장 등록 성공");
					temp_key = this.comBoardMapper.selectTempPk(dto.getCompany_key());
				}
			}
			
			return temp_key;
		}
		
		// 관심기업 등록/해제
		@PostMapping("/interest/action")
		//@ResponseBody
		public void interestCheck(@RequestParam("check") int check,@RequestParam("company_key") int company_key, HttpServletRequest request, HttpSession session) {
			
			InterestDTO iDTO = new InterestDTO();
			// 세션 개인횡정보로 회원키 저장
			UserDTO udto = (UserDTO)session.getAttribute("uDTO");
			iDTO.setCompany_key(company_key);
			iDTO.setUser_key(udto.getUser_key());
			
			// 1 = check , 0 = uncheck
			if(check == 1) {
				this.comBoardMapper.insertInterestCheck(iDTO);
				System.out.println("insertInterestCheck");
			}else{
				this.comBoardMapper.deleteInterestCheck(iDTO);
				System.out.println("deleteInterestCheck");
			}
		}
		
		// 공고 지원
		@PostMapping("/apply/insert")
		//@ResponseBody
		public void addApply(@RequestParam("checked") String checked, HttpServletRequest request, HttpSession session) {

			UserDTO udto = (UserDTO)session.getAttribute("uDTO");
			int profile_key = this.comBoardMapper.selectDefaultProfile(udto.getUser_key());
			
			System.out.println(checked+"checked");
			for (int i = 0; i < checked.split(",").length; i++) {
				ApplyDTO aDTO = new ApplyDTO();
				
				aDTO.setProfile_key(profile_key);
				aDTO.setCom_board_key(Integer.parseInt(checked.split(",")[i]));
				
				if(this.comBoardMapper.addApply(aDTO) > 0) System.out.println("지원성공");
				else System.out.println("지원실패");
			}
		}
}