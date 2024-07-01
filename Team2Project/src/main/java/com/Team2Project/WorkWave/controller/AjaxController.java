package com.Team2Project.WorkWave.controller;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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
   @Autowired private ProfileMapper profileMapper;
   @Autowired private UserMapper userMapper;

   @Autowired private UserService userService;
   
	// 한 페이지당 보여질 게시물의 수
	private final int rowsize = 10;
	// DB 상의 전체 게시물의 수
	private int totalRecord = 0;
	
	// 아이디 중복검사
	@PostMapping("/checkCompanyId")
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
	public List<CodeDTO> categorysub(@RequestParam("no") String no) {
		List<CodeDTO> categorysub = this.profileMapper.categorysub(no);
		return categorysub;
	}

	// 소분류 카테고리
	@PostMapping("/jobCodesub")
	public List<CodeDTO> categorystep(@RequestParam("no") String no) {
		List<CodeDTO> categorystep = this.profileMapper.categorystep(no);
		return categorystep;

	}
	// 학교구분과 학교 이름 검색 시 해당 학교 리스트 불러오기
	@PostMapping("/searchSchoolByName")
	public List<CodeDTO> schoolName(CodeDTO dto) {
		List<CodeDTO> schoolName = this.profileMapper.schoolname(dto);
		return schoolName;

	}

	// 학교구분과 전공명 검색 시 해당 전공 데이터 불러오기
	@PostMapping("/getDepartment")
	public List<CodeDTO> departmentName(CodeDTO dto) {
		List<CodeDTO> departmentName = this.profileMapper.department(dto);
		return departmentName;

	}

	// 해당 자격증 리스트 불러오기
	@PostMapping("/searchCertifications")
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
		reply.setUser_key(Integer.parseInt(paramMap.get("user_key")));
		reply.setUser_id(paramMap.get("user_id"));
	
		this.chatMapper.insertReply(reply);
		ChatReplyDTO replyOut = this.chatMapper.getReplyById(reply.getReply_key());
		
		result.put("reply_cont", reply_cont);
		result.put("reply_date", replyOut.getReply_date().toString());
		result.put("user_id", replyOut.getUser_id());
		
		/*
		 * List<ChatReplyDTO> replies = this.chatMapper.getRepliesByChatKey(chat_key);
		 * result.put("replies", replies);
		 */
	
		
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
	
	 @PostMapping("/deleteReply")
	    public Map<String, String> deleteReply(@RequestParam Map<String, String> paramMap) {
	        
	        Map<String, String> result = new HashMap<>();
	        result.put("message", "댓글이 삭제되었습니다.");
	        
	        
	        int reply_key = (Integer.parseInt(paramMap.get("reply_key")));
	        
	        this.chatMapper.deleteReply(reply_key);

	        return result;
	 }
	 
	 @PostMapping("/editReply")
	    public Map<String, String> editReply(@RequestParam Map<String, String> paramMap) {
	        Map<String, String> result = new HashMap<>();
	        try {
	            int reply_key = Integer.parseInt(paramMap.get("reply_key"));
	            String reply_content = paramMap.get("reply_content");

	            // 댓글 수정 로직 수행
	            ChatReplyDTO reply = new ChatReplyDTO();
	            reply.setReply_key(reply_key);
	            reply.setReply_content(reply_content);
	            chatMapper.updateReply(reply);

	            result.put("message", "댓글 수정 완료!");
	        } catch (Exception e) {
	            result.put("message", "댓글 수정 중 오류 발생");
	        }

	        return result;
	    }
	 
	 

	// (리스트) 리스트 조회
	@PostMapping("/comBoardList")
	public HashMap<String, Object> getComBoardList(HttpSession session, HttpServletRequest request) {
		
		HashMap<String, Object> viewMap = new HashMap<>(); //뷰페이지로 이동하는 맵 
		HashMap<String, Object> reqMapperMap = new HashMap<>(); // 매퍼로 이동하는 맵
		
		String jobCode = request.getParameter("jobCode");
		String locCode = request.getParameter("locCode");
		String eduCode = request.getParameter("eduCode");
		String careerCode = request.getParameter("careerCode");
		String typeCode = request.getParameter("typeCode");
		
		reqMapperMap.put("keyword", request.getParameter("keyword"));
		if(jobCode.equals(""))reqMapperMap.put("jobCode", null);
		else reqMapperMap.put("jobCode", jobCode.split(","));
		
		if(locCode.equals(""))reqMapperMap.put("locCode", null);
		else reqMapperMap.put("locCode", locCode.split(","));
		
		if(eduCode.equals(""))reqMapperMap.put("eduCode", null);
		else reqMapperMap.put("eduCode", eduCode.split(","));
		
		if(careerCode.equals(""))reqMapperMap.put("careerCode", null);
		else reqMapperMap.put("careerCode", careerCode.split(","));
		
		if(typeCode.equals(""))reqMapperMap.put("typeCode", null);
		else reqMapperMap.put("typeCode", typeCode.split(","));
		
		int page;	// 현재 페이지 변수
		
		// 페이징 처리 작업
		if(request.getParameter("page") != null) {
			page = Integer.parseInt(request.getParameter("page"));
		}else {
			page = 1;
		}
		
		totalRecord = this.comBoardMapper.countComBoard_list(reqMapperMap);
		
		Page pdto = new Page(page, rowsize, totalRecord);
		
		reqMapperMap.put("pdto",pdto);

		List<ComBoardDTO> list = this.comBoardMapper.getComBoardList(reqMapperMap);
		viewMap.put("list", list);
		viewMap.put("paging", pdto);

		if(session.getAttribute("uDTO") == null) {
		}else {
			UserDTO udto = (UserDTO)session.getAttribute("uDTO");
			int userKey = udto.getUser_key();
			int[] interestList = this.comBoardMapper.getInterestCompanyKeyList(userKey); // 관심기업 데이터
			viewMap.put("interestList", interestList);
			viewMap.put("applyList", this.comBoardMapper.getApplyList(userKey));
		}
		
		return viewMap;
	}
	
	// (리스트) 업종분류 조회
	@PostMapping("/jobCode")
	public Map<String, List<CodeDTO>> getJobCodeList() {
		
		Map<String, List<CodeDTO>> map = new HashMap<>();
		map.put("group", this.comBoardMapper.getJobCodeGroupList());
		map.put("sub", this.comBoardMapper.getJobCodeSubList()); 
		map.put("step", this.comBoardMapper.getJobCodeStepList());
		
		return map;
	}

	// (리스트) 지역 조회
	@PostMapping("/locationCode")
	public Map<String, List<CodeDTO>> getLocationCodeList() {
		
		Map<String, List<CodeDTO>> map = new HashMap<>();
		map.put("group", this.comBoardMapper.getLocationCodeGroupList());
		map.put("sub", this.comBoardMapper.getLocationCodeSubList());
		
		return map;
	}
	   // (등록) 공고 중간저장
	   @PostMapping("/comBoardTemp/insert")
	   public int addComBoardTemp(ComBoardDTO dto, HttpSession session, HttpServletRequest request) {
	      int temp_key = dto.getTemp_key();
	      // 세션 기업정보로 기업키 저장
	      CompanyDTO cdto = (CompanyDTO)session.getAttribute("cDTO");
	      dto.setCompany_key(cdto.getCompany_key());
	      
	      if(temp_key > 0) {
	         this.comBoardMapper.updateComBoardTemp(dto);
	      }else{
	         if(this.comBoardMapper.addComBoardTemp(dto) > 0) {
	            temp_key = this.comBoardMapper.selectTempPk(dto.getCompany_key());
	         }
	      }
	      
	      return temp_key;
	   }
	
	// 관심기업 등록/해제
	@PostMapping("/interest/action")
	public void interestCheck(@RequestParam("check") int check,@RequestParam("company_key") int company_key, HttpServletRequest request, HttpSession session) {
		
		InterestDTO iDTO = new InterestDTO();
		// 세션 개인횡정보로 회원키 저장
		UserDTO udto = (UserDTO)session.getAttribute("uDTO");
		iDTO.setCompany_key(company_key);
		iDTO.setUser_key(udto.getUser_key());
		
		// 1 = check , 0 = uncheck
		if(check == 1) {
			this.comBoardMapper.insertInterestCheck(iDTO);
		}else{
			this.comBoardMapper.deleteInterestCheck(iDTO);
		}
	}
	
	// 공고 지원 
	@PostMapping("/apply/insert")
	public void addApply(@RequestParam("checked") String checked, HttpServletRequest request, HttpSession session) {

		UserDTO udto = (UserDTO)session.getAttribute("uDTO");
		
		for (int i = 0; i < checked.split(",").length; i++) {
			ApplyDTO aDTO = new ApplyDTO();
			
			aDTO.setUser_key(udto.getUser_key());
			aDTO.setProfile_key(this.comBoardMapper.selectDefaultProfile(udto.getUser_key()));
			aDTO.setCom_board_key(Integer.parseInt(checked.split(",")[i]));
			aDTO.setCompany_key(this.comBoardMapper.selectComBoardCompanyKey(aDTO.getCom_board_key()));
			
			this.comBoardMapper.addApply(aDTO);
		}
	}
	
	//메인 공고리스트 / 최신 / 지원 많은공고 /남은기간
	@PostMapping("/mainComBoardList")
	public HashMap<String, Object> mainComBoardList() {
		HashMap<String, Object> map = new HashMap<>(); //뷰페이지로 이동하는 맵 
		map.put("new", this.comBoardMapper.getMainNewComBoardList());
		map.put("hot", this.comBoardMapper.getMainHotComBoardList());
		map.put("time", this.comBoardMapper.getMainTimeComBoardList());

		return map;	
	}
		
	// (상세보기) 추천인재 리스트 조회
	@PostMapping("/recommendList")
	public HashMap<String, Object> getRecommendList(HttpSession session, HttpServletRequest request) {
		HashMap<String, Object> viewMap = new HashMap<>(); //뷰페이지로 이동하는 맵 
		HashMap<String, Object> reqMapperMap = new HashMap<>(); // 매퍼로 이동하는 맵

		int page;	// 현재 페이지 변수
		
		// 페이징 처리 작업
		if(request.getParameter("page") != null) {
			page = Integer.parseInt(request.getParameter("page"));
		}else {
			page = 1;
		}
		
		totalRecord = this.comBoardMapper.recommendListCount(request.getParameter("code"));
		
		Page pdto = new Page(page, rowsize, totalRecord);
		
		reqMapperMap.put("pdto",pdto);
		reqMapperMap.put("code",request.getParameter("code"));

		List<ProfileDTO> list = this.comBoardMapper.getRecommendList(reqMapperMap);
		int[] successList = this.comBoardMapper.getPositionSuccessList(Integer.parseInt(request.getParameter("comBoardKey")));
		viewMap.put("list", list);
		viewMap.put("successList", successList);
		viewMap.put("paging", pdto);

		return viewMap;
	}
	
	@PostMapping("/applyCancel")
    public ResponseEntity<String> applyCancel(@RequestParam("apply_key") int apply_key) {
        try {
            // applyKey를 사용하여 지원 취소 로직 수행
            userMapper.applyCancelUp(apply_key);
            return ResponseEntity.ok("지원 취소 완료");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("지원 취소 실패");
        }
    }
	
	// 포지션제안 
	@PostMapping("/position/insert")
	public void addPosition(PositionDTO psDTO, HttpServletRequest request, HttpSession session) {
		CompanyDTO cdto = (CompanyDTO)session.getAttribute("cDTO");
		psDTO.setCompany_key(cdto.getCompany_key());
		this.comBoardMapper.insertPosition(psDTO);
	}
	
	// 포지션 제안 확인시 열람으로 바뀌는 메서드
	@PostMapping("/position/check")
	public void positionCheck(@RequestParam("check") int position_key, HttpServletRequest request, HttpSession session) {
		
		this.userMapper.positionCheck(position_key);
		
	}
	
	// 포지션 제안 수락
	@PostMapping("/positionOk")
	public void positionOk(@RequestParam("positionKey") int position_key) {
		
		this.userMapper.positionOk(position_key);
		
	}
	
	// 포지션 제안 수락
	@PostMapping("/positionNo")
	public void positionNo(@RequestParam("positionKey") int position_key) {
		
		this.userMapper.positionNo(position_key);
		
	}
	
	@GetMapping("/position/result/check")
	public String positionResult(@RequestParam("positionKey") int position_key) {
		
		PositionDTO result = this.userMapper.positionResult(position_key);
		
		return result.getPosition_check();
	}
	
	@GetMapping("/companyComBoard")
	public HashMap<String, Object> getCompanyComBoard(HttpSession session, HttpServletRequest request) {
		HashMap<String, Object> viewMap = new HashMap<>(); //뷰페이지로 이동하는 맵 
		HashMap<String, Object> reqMapperMap = new HashMap<>(); // 매퍼로 이동하는 맵
		
		CompanyDTO company = (CompanyDTO) session.getAttribute("cDTO");
		
		// 해당 기업의 공고들 가져오기
		List<ComBoardDTO> com_board_list = this.companyMapper.getComBoardList(company.getCompany_key());
		// 해당 기업의 작성중 공고 가져오기
		List<ComBoardDTO> temp_list = this.companyMapper.getComBoardTempList(company.getCompany_key());
		
		Map<Integer, Integer> apply_total_map = new HashMap<>();
	    Map<Integer, Integer> apply_non_check_map = new HashMap<>();
		
	    for (ComBoardDTO com_board : com_board_list) {
	        int apply_total = this.companyMapper.getComBoardApply(com_board.getCom_board_key());
	        apply_total_map.put(com_board.getCom_board_key(), apply_total);
	        
	        int apply_non_check = this.companyMapper.getComBoardApplyNonCheck(com_board.getCom_board_key());
	        apply_non_check_map.put(com_board.getCom_board_key(), apply_non_check);
	    }
		
		viewMap.put("comBoardList", com_board_list);
		viewMap.put("applyTotalMap", apply_total_map);
	    viewMap.put("applyNonCheckMap", apply_non_check_map);
		viewMap.put("tempList", temp_list);
		
		
		return viewMap;
	}
	
	// 프로필 이름 클릭시 해당 프로필 체크를 Y로 바꾸기
	@PostMapping("/updateProfileCheck")
	public void updateProfileCheck(@RequestParam("applyKey") int apply_key) {
		System.out.println(apply_key);
		this.companyMapper.applyProfileCheck(apply_key);
	}
	
	// 포지션제안 수정 
	@PostMapping("/position/update")
	public void updatePosition(PositionDTO psDTO, HttpServletRequest request, HttpSession session) {
		this.comBoardMapper.updatePosition(psDTO);
	}
	// 포지션제안 삭제
	@PostMapping("/position/delete")
	public void deletePosition(@RequestParam("position_key") int position_key) {
		this.comBoardMapper.deletePosition(position_key);
	}
}