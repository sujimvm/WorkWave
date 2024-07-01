package com.Team2Project.WorkWave.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.Team2Project.WorkWave.model.*;
import com.Team2Project.WorkWave.service.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kotlinx.serialization.descriptors.StructureKind.LIST;
import kotlinx.serialization.descriptors.StructureKind.MAP;

@Controller
@RequestMapping("/C")
public class CompanyController {

	@Autowired
	private ComBoardMapper comBoardMapper;
	@Autowired
	private CompanyMapper companyMapper;
	@Autowired
	private ProfileMapper profileMapper;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private UploadFileService uploadFileService;

	// 기업회원 정보 수정하기전 비밀번호 확인 페이지 이동
	@GetMapping("/update/companyPwdCheck")
	public String companyModify() {

		return "company/modify";

	}

	// 비밀번호 입력후 기업회원 정보 수정 페이지로 이동
	@PostMapping("/update/companyPwdCheckOk")
	public String companyModifyOk(@RequestParam("company_pwd") String pwd,
			@RequestParam("company_number") String company_no, HttpSession session, HttpServletResponse response)
			throws IOException {

		CompanyDTO companyDto = (CompanyDTO) session.getAttribute("cDTO");
		System.out.println(companyDto);

		String str1 = company_no.substring(0, 3);
		String str2 = company_no.substring(3, 5);
		String str3 = company_no.substring(5);

		String companyNumber = str1 + "-" + str2 + "-" + str3;

		System.out.println(companyNumber.trim());

		response.setContentType("text/html; charset=UTF-8");

		PrintWriter out = response.getWriter();

		if (passwordEncoder.matches(pwd, companyDto.getCompany_pwd())) {
			if (companyDto.getCompany_number().equals(companyNumber)) {
				return "company/modify_ok";
			} else {
				out.println("<script>");
				out.println("alert('사업자등록번호가 틀립니다. 다시한번 확인해 주십시오.')");
				out.println("</script>");
				return "company/modify";
			}
		} else {
			out.println("<script>");
			out.println("alert('비밀번호가 틀립니다. 다시한번 확인해 주십시오.')");
			out.println("</script>");
			return "company/modify";
		}

	}

	// 회원정보를 수정 확정하는 메서드
	@PostMapping("/update")
	@ResponseBody
	public void companyInfoUpdate(@RequestParam("logo") MultipartFile file, CompanyDTO dto,
			HttpServletResponse response, HttpSession session) throws IOException {

		CompanyDTO original_company_dto = (CompanyDTO) session.getAttribute("cDTO");

		dto.setCompany_logo_name(original_company_dto.getCompany_logo_name());
		dto.setCompany_logo(original_company_dto.getCompany_logo());

		if (file.getOriginalFilename() != null) {
			if (file != null && !file.isEmpty()) {
				/*
				 * String logoUploadDir =
				 * "C:\\Users\\clxkd\\OneDrive\\바탕 화면\\project1\\WorkWave\\Team2Project\\src\\main\\resources\\static\\image\\logo"
				 * ;
				 */
				
				String userDir = System.getProperty("user.dir");
			    String logoUploadDir = userDir+"\\src\\main\\resources\\static\\image\\logo";
			    System.out.println(logoUploadDir+"userDir");
				uploadFileService.deleteFile(dto.getCompany_logo(), logoUploadDir);

				dto.setCompany_logo_name(file.getOriginalFilename());

				String imageName = this.uploadFileService.uploadOriName(file, logoUploadDir);
				dto.setCompany_logo(imageName);
			}
		}

		if (!original_company_dto.getCompany_phone().equals(dto.getCompany_phone())) {
			if (dto.getCompany_phone().length() == 8) {
				String com_phone1 = dto.getCompany_phone().substring(0, 4);
				String com_phone2 = dto.getCompany_phone().substring(4);

				dto.setCompany_phone(com_phone1 + "-" + com_phone2);
			} else if (dto.getCompany_phone().length() == 9) {
				String com_phone1 = dto.getCompany_phone().substring(0, 2);
				String com_phone2 = dto.getCompany_phone().substring(2, 5);
				String com_phone3 = dto.getCompany_phone().substring(5);

				dto.setCompany_phone(com_phone1 + "-" + com_phone2 + "-" + com_phone3);
			} else if (dto.getCompany_phone().length() == 10) {
				if (dto.getCompany_phone().substring(0, 2) == "02") {
					String com_phone1 = dto.getCompany_phone().substring(0, 2);
					String com_phone2 = dto.getCompany_phone().substring(2, 6);
					String com_phone3 = dto.getCompany_phone().substring(6);

					dto.setCompany_phone(com_phone1 + "-" + com_phone2 + "-" + com_phone3);
				} else {
					String com_phone1 = dto.getCompany_phone().substring(0, 3);
					String com_phone2 = dto.getCompany_phone().substring(3, 6);
					String com_phone3 = dto.getCompany_phone().substring(6);

					dto.setCompany_phone(com_phone1 + "-" + com_phone2 + "-" + com_phone3);
				}
			} else {
				String com_phone1 = dto.getCompany_phone().substring(0, 3);
				String com_phone2 = dto.getCompany_phone().substring(3, 7);
				String com_phone3 = dto.getCompany_phone().substring(7);

				dto.setCompany_phone(com_phone1 + "-" + com_phone2 + "-" + com_phone3);
			}

		}

		if (!original_company_dto.getCompany_mgr_phone().equals(dto.getCompany_mgr_phone())) {
			String com_mgr_phone1 = dto.getCompany_mgr_phone().substring(0, 3);
			String com_mgr_phone2 = dto.getCompany_mgr_phone().substring(3, 7);
			String com_mgr_phone3 = dto.getCompany_mgr_phone().substring(7);

			dto.setCompany_mgr_phone(com_mgr_phone1 + "-" + com_mgr_phone2 + "-" + com_mgr_phone3);
		}

		if (!original_company_dto.getCompany_homepage().equals(dto.getCompany_homepage())) {
			dto.setCompany_homepage("http://" + dto.getCompany_homepage());
		}

		int result = this.companyMapper.companyUpdate(dto);

		response.setContentType("text/html; charset=UTF-8");

		PrintWriter out = response.getWriter();

		if (result == 1) {
			session.removeAttribute("cDTO");

			CompanyDTO updatedto = this.companyMapper.companyInfo(dto.getCompany_id());

			session.setAttribute("cDTO", updatedto);

			out.println("<script>");
			out.println("alert('회원정보수정 완료하였습니다.')");
			out.println("location.href='/C/info'");
			out.println("</script>");
		} else {
			out.println("<script>");
			out.println("alert('회원정보수정 실패하였습니다.')");
			out.println("history.back()");
			out.println("</script>");
		}

	}
	
	// 기업회원 비밀번호 수정하기전 비밀번호 확인 페이지 이동
	@GetMapping("/update/pwd/companyPwdCheck")
	public String companyPwdUpdate() {

		return "company/pwdUpdate";

	}
	
	@PostMapping("/update/pwd")
	public void companyPwdUpdateCheck(@RequestParam("ori_company_pwd") String ori_company_pwd,
									  @RequestParam("new_company_pwd") String new_company_pwd,
									  HttpSession session,
									  HttpServletResponse response) throws IOException {
		
		CompanyDTO companyDto = (CompanyDTO) session.getAttribute("cDTO");
		
		response.setContentType("text/html; charset=UTF-8");

		PrintWriter out = response.getWriter();
		
		if(passwordEncoder.matches(ori_company_pwd, companyDto.getCompany_pwd())) {
			//입력된 비밀번호와 기존 비밀번호가 같을때
			if(passwordEncoder.matches(new_company_pwd, companyDto.getCompany_pwd())) {
				// 기존 비밀번호와 새 비밀번호가 같다면 다시 비밀번호 변경 창으로
				out.println("<script>");
				out.println("alert('기존 비밀번호와 새 비밀번호가 같습니다.')");
				out.println("history.back()");
				out.println("</script>");
			}else {
				// 기존 비밀번호와 새 비밀번호가 다르다면 새 비밀번호로 다시 저장
				String new_company_pwd_encorded = this.passwordEncoder.encode(new_company_pwd);
				
				session.removeAttribute("cDTO");
				
				this.companyMapper.companyPwdUpdate(companyDto.getCompany_id(), new_company_pwd_encorded);
				
				CompanyDTO updatedto = this.companyMapper.companyInfo(companyDto.getCompany_id());
				
				session.setAttribute("cDTO", updatedto);
				
				out.println("<script>");
				out.println("alert('비밀번호 수정에 성공했습니다.')");
				out.println("location.href='/C/info'");
				out.println("</script>");
			}
		}else {
			//입력된 비밀번호와 기존 비밀번호가 다를때
			out.println("<script>");
			out.println("alert('입력하신 비밀번호와 기존 비밀번호가 다릅니다.')");
			out.println("history.back()");
			out.println("</script>");
		}
		
	}

	// 회원 삭제 페이지로 이동
	@GetMapping("/delete/companyPwdCheck")
	public String companyDelete(HttpSession session) {

		session.setAttribute("companyInfo", session.getAttribute("companyInfo"));

		return "company/delete";

	}

	// 비밀번호 확인 후 회원 삭제
	@PostMapping("/delete/companyPwdCheckOk")
	public void companyDeleteOk(@RequestParam("company_pwd") String company_pwd,
			@RequestParam("company_number") String company_number, HttpSession session, HttpServletResponse response)
			throws IOException {

		response.setContentType("text/html; charset=UTF-8");

		PrintWriter out = response.getWriter();

		// 세션에 저장된 companyDTO 객체 불러오기
		CompanyDTO company_dto = (CompanyDTO) session.getAttribute("cDTO");

		String str1 = company_number.substring(0, 3);
		String str2 = company_number.substring(3, 5);
		String str3 = company_number.substring(5);

		String companyNumber = str1 + "-" + str2 + "-" + str3;

		if (passwordEncoder.matches(company_pwd, company_dto.getCompany_pwd()) && company_dto.getCompany_number().equals(companyNumber)) {

			String userDir = System.getProperty("user.dir");
		    String logoUploadDir = userDir+"\\src\\main\\resources\\static\\image\\logo";
		    System.out.println(logoUploadDir+"userDir");
			uploadFileService.deleteFile(company_dto.getCompany_logo(), logoUploadDir);

			int result = this.companyMapper.companyDelete(company_dto.getCompany_key());

			System.out.println(result);

			if (result == 1) {
				session.invalidate();

				out.println("<script>");
				out.println("alert('회원삭제에 성공했습니다.')");
				out.println("location.href='/'");
				out.println("</script>");
			} else {
				out.println("<script>");
				out.println("alert('등록된 회원정보와 입력하신 정보가 다릅니다.')");
				out.println("history.back()");
				out.println("</script>");
			}
		}
	}

	// 기업정보 조회 전송 예정
	// (등록) 페이지 이동
	@GetMapping("/comBoard/insert")
	public String goAddComBoard(HttpSession session) {
		return "/comBoard/add";
	}

	// (등록) 공고 등록
	@PostMapping("/comBoard/insertOk")
	public void addComBoard(ComBoardDTO dto, HttpServletResponse response, HttpSession session) throws IOException {
		int temp_key = 0;
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out = response.getWriter();

		// 세션 기업정보로 기업키 저장
		CompanyDTO cDTO = (CompanyDTO) session.getAttribute("cDTO");
		dto.setCompany_key(cDTO.getCompany_key());

		if (this.comBoardMapper.addComBoard(dto) > 0) {
			temp_key = dto.getTemp_key();
			if (temp_key != 0)
				this.comBoardMapper.deleteComBoardTemp(temp_key);
			out.println("<script> alert('공고를 성공적으로 등록하였습니다'); location.href='/C/comBoardList'; </script>");
		} else {
			out.println("<script> alert('공고등록에 실패하엿습니다'); history.back(); </script>");
		}
		out.flush();
	}

	// (상세보기) 페이지 이동
	@GetMapping("/comBoard/update")
	public String goComBoardUpdate(HttpServletRequest request, Model model) {

		int com_board_key = Integer.parseInt(request.getParameter("No"));

		ComBoardDTO dto = this.comBoardMapper.getComBoard(com_board_key);

		model.addAttribute("dto", dto);

		return "/comBoard/update";
	}

	// (등록) 공고 수정
	@PostMapping("/comBoard/updateOk")
	public void updateComBoard(ComBoardDTO dto, HttpServletResponse response, HttpSession session) throws IOException {
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out = response.getWriter();

		int loginCom = ((CompanyDTO)session.getAttribute("cDTO")).getCompany_key();
		
		if(dto.getCompany_key() == loginCom) {
			if (this.comBoardMapper.updateComBoard(dto) > 0) {
				out.println("<script> alert('공고를 성공적으로 수정하였습니다'); location.href='/C/comBoardCont?No="+dto.getCom_board_key()+"'; </script>"); 
			} else {
				out.println("<script> alert('공고수정에 실패했습니다'); history.back(); </script>");
			}
		}else {
			out.println("<script> alert('잘못된 접근입니다.'); history.back(); </script>");
		}
		
		out.flush();
	}
	
	@GetMapping("/info")
	public String companyInfo(HttpSession session, Model model) {
		
		CompanyDTO companyInfo = (CompanyDTO) session.getAttribute("cDTO");
		  
		int comBoarding = this.companyMapper.companyBoardingCnt(companyInfo.getCompany_key());
		int comBoardEnd = this.companyMapper.companyBoardEndCnt(companyInfo.getCompany_key());
		int applyNoneCheckCnt = this.companyMapper.applyNoneCheckCnt(companyInfo.getCompany_key());
		int positionCnt = this.companyMapper.positionCnt(companyInfo.getCompany_key());
		
		
		model.addAttribute("comBoarding", comBoarding)
			 .addAttribute("comBoardEnd", comBoardEnd)
			 .addAttribute("applyNoneCheckCnt", applyNoneCheckCnt) 
			 .addAttribute("positionCnt", positionCnt); 
		
		return "company/cont";
	}
	
	// 해당 기업의 공고 리스트 페이지로 이동
	@GetMapping("/comBoardList")
	public String companyBoardList(HttpSession session, Model model) {
		
		return "company/comBoardList";
	}
	
	
	// (상세보기) 페이지 이동
	@GetMapping("/comBoardCont") 
	public String goComBoardContent(HttpServletRequest request, Model model) {

		int com_board_key = Integer.parseInt(request.getParameter("No"));

		HashMap<String, Object> map = new HashMap<>(); 
		map.put("dto", this.comBoardMapper.getComBoard(com_board_key));
		map.put("applyTotal", this.comBoardMapper.getApplyCount(com_board_key));
		map.put("avgAge", this.comBoardMapper.getApplyAvgAge(com_board_key));
		map.put("avgGender", this.comBoardMapper.getApplyAvgGender(com_board_key));
		map.put("avgEdu", this.comBoardMapper.getApplyAvgEdu(com_board_key));

		model.addAttribute("map",map);

		return "/company/comBoardCont"; 

	}
	
	// (상세보기) 임시공고 수정
	@GetMapping("/comBoard/temp") 
	public String goComBoardContent(HttpSession session, HttpServletRequest request, Model model) {

		int temp_key = Integer.parseInt(request.getParameter("No"));

		CompanyDTO companyInfo = (CompanyDTO) session.getAttribute("cDTO");
		
		model.addAttribute("dto",this.comBoardMapper.getComBoardTemp(temp_key));
		return "/comBoard/temp"; 
	}
	
	
	// 공고리스트 해당 공고에 총 지원자 리스트 페이지
	@GetMapping("/totalApply")
	public String totalApply(@RequestParam("no") int com_board_key, Model model) {
		
		List<ApplyDTO> totalApplyList = this.companyMapper.totalApplyDTO(com_board_key);
		
		List<ProfileDTO> totalApplyProfile = new ArrayList<>();
		
		List<UserDTO> userList = new ArrayList<>();
		
		for(ApplyDTO totalApply : totalApplyList ) {
			ProfileDTO applyProfile = this.companyMapper.applyProfile(totalApply.getApply_key());
			totalApplyProfile.add(applyProfile);
			
			UserDTO userInfo = this.companyMapper.userList(totalApply.getUser_key());
			userList.add(userInfo);
		}
		
		model.addAttribute("totalApplyList", totalApplyList)
			 .addAttribute("totalApplyProfile", totalApplyProfile)
			 .addAttribute("userList", userList);
		
		return "company/totalApply";
	}
	
	// 해당 기업의 공고 리스트 페이지로 이동
	@GetMapping("/positionList")
	public String positionList(HttpSession session, Model model) {
		CompanyDTO cDTO = (CompanyDTO) session.getAttribute("cDTO");
		model.addAttribute("list",this.companyMapper.getPositionList(cDTO.getCompany_key()))
		.addAttribute("count",this.companyMapper.getPositionListCount(cDTO.getCompany_key()));
		return "/company/positionList";
	}
	
	// 프로필로 이동
	@GetMapping("/viewProfile")
	public void viewProfile(@RequestParam("No") int user_key, HttpServletResponse response) throws IOException {
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out = response.getWriter();
		
		int key = this.comBoardMapper.selectDefaultProfile(user_key);
		
		out.println("<script> location.href='/CU/profile/content?no="+key+"'; </script>");
	}
	
	// (등록) 공고 수정
	@GetMapping("/comBoard/delete")
	public void deleteComBoard(@RequestParam("No") int com_board_key, HttpServletResponse response, HttpSession session) throws IOException {
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out = response.getWriter();

		HashMap<String, Object> map = new HashMap<>(); 
		map.put("com_board_key", com_board_key);
		map.put("company_key", ((CompanyDTO)session.getAttribute("cDTO")).getCompany_key());
		
		System.out.println(map.get("company_key"));
		
		if (this.comBoardMapper.deleteComBoard(map) > 0) {
			out.println("<script> alert('공고를 성공적으로 삭제하였습니다'); location.href='/C/comBoardList'; </script>"); // 마이페이지로 변경 예정
		} else {
			out.println("<script> alert('공고삭제에 실패했습니다'); history.back(); </script>");
		}
		
		out.flush();
	}
	@GetMapping("/comBoard/tempDelete")
	public void deleteTempComBoard(@RequestParam("No") int temp_key, HttpServletResponse response, HttpSession session) throws IOException {
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out = response.getWriter();
		
		HashMap<String, Object> map = new HashMap<>(); 
		map.put("temp_key", temp_key);
		map.put("company_key", ((CompanyDTO)session.getAttribute("cDTO")).getCompany_key());
		
		if (this.comBoardMapper.deleteTempComBoard(map) > 0) {
			out.println("<script> alert('공고를 성공적으로 삭제하였습니다'); location.href='/C/comBoardList'; </script>"); // 마이페이지로 변경 예정
		} else {
			out.println("<script> alert('공고삭제에 실패했습니다'); history.back(); </script>");
		}
		
		out.flush();
	}
}