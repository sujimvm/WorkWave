package com.Team2Project.WorkWave.controller;

import java.io.IOException;
import java.io.PrintWriter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.Team2Project.WorkWave.model.*;
import com.Team2Project.WorkWave.service.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller("/C")
public class CompanyRController {

	@Autowired
	private ComBoardMapper comBoardMapper;
	@Autowired
	private CompanyMapper companyMapper;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private UploadFileService uploadFileService;

	// 기업회원 정보 수정하기전 비밀번호 확인 페이지 이동
	@GetMapping("/update/pwdCheck")
	public String companyModify(HttpSession session, Model model) {

		model.addAttribute("companyInfo", session.getAttribute("companyInfo"));

		return "company/modify";

	}

	// 비밀번호 입력후 기업회원 정보 수정 페이지로 이동
	@PostMapping("/update/pwdCheckOk")
	public String companyModifyOk(@RequestParam("company_pwd") String pwd,
			@RequestParam("company_number") String company_no, HttpSession session, HttpServletResponse response)
			throws IOException {

		CompanyDTO companyDto = (CompanyDTO) session.getAttribute("companyInfo");
		System.out.println(companyDto);

		String str1 = company_no.substring(0, 3);
		String str2 = company_no.substring(3, 5);
		String str3 = company_no.substring(5);

		String companyNumber = str1 + "-" + str2 + "-" + str3;

		System.out.println(companyNumber.trim());

		response.setContentType("text/html; charset=UTF-8");

		PrintWriter out = response.getWriter();

		if (companyDto.getCompany_pwd().equals(pwd)) {
			if (companyDto.getCompany_number().equals(companyNumber)) {
				session.setAttribute("companyInfo", companyDto);
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

		CompanyDTO original_company_dto = (CompanyDTO) session.getAttribute("companyInfo");

		dto.setCompany_logo_name(original_company_dto.getCompany_logo_name());
		dto.setCompany_logo(original_company_dto.getCompany_logo());

		if (file.getOriginalFilename() != null) {
			if (file != null && !file.isEmpty()) {
				String logoUploadDir = "C:\\Users\\clxkd\\OneDrive\\바탕 화면\\project1\\WorkWave\\Team2Project\\src\\main\\resources\\static\\image\\logo";

				uploadFileService.deleteFile(dto.getCompany_logo(), logoUploadDir);

				dto.setCompany_logo_name(file.getOriginalFilename());

				String imageName = uploadFileService.upload(file, logoUploadDir);
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
			session.removeAttribute("companyInfo");

			CompanyDTO updatedto = this.companyMapper.companyInfo(dto.getCompany_id());

			session.setAttribute("companyInfo", updatedto);
			session.setAttribute("member_type", "company");

			out.println("<script>");
			out.println("alert('회원정보수정 완료하였습니다.')");
			out.println("location.href='/info'");
			out.println("</script>");
		} else {
			out.println("<script>");
			out.println("alert('회원정보수정 실패하였습니다.')");
			out.println("history.back()");
			out.println("</script>");
		}

	}

	// 비밀번호찾기에서 해당 유저가 존재할 시 새 비밀번호를 설정하여 업데이트
	@PostMapping("/companyPwdUpdate")
	public void companyPwdUpdate(@RequestParam("company_id") String company_id,
			@RequestParam("company_pwd") String company_pwd, HttpServletResponse response) throws IOException {

		String encordedPwd = passwordEncoder.encode(company_pwd);

		response.setContentType("text/html; charset=UTF-8");

		PrintWriter out = response.getWriter();

		CompanyDTO dto = this.companyMapper.companyInfo(company_id);

		if (encordedPwd.equals(dto.getCompany_pwd())) {
			out.println("<script>");
			out.println("alert('기존 비밀번호와 새로 입력하신 비밀번호가 같습니다.')");
			out.println("history.back()");
			out.println("</script>");
		} else {
			int result = this.companyMapper.companyPwdUpdate(company_id, encordedPwd);

			if (result == 1) {
				out.println("<script>");
				out.println("alert('비밀번호 수정에 성공했습니다.')");
				out.println("location.href='/G/login");
				out.println("</script>");
			} else {
				out.println("<script>");
				out.println("alert('오류발생!')");
				out.println("history.back()");
				out.println("</script>");
			}
		}

	}

	// 회원 삭제 페이지로 이동
	@GetMapping("/delete/pwdCheck")
	public String companyDelete(HttpSession session) {

		session.setAttribute("companyInfo", session.getAttribute("companyInfo"));

		return "company/delete";

	}

	@PostMapping("/delete/pwdCheckOk")
	public void companyDeleteOk(@RequestParam("company_pwd") String company_pwd,
			@RequestParam("company_number") String company_number, HttpSession session, HttpServletResponse response)
			throws IOException {

		response.setContentType("text/html; charset=UTF-8");

		PrintWriter out = response.getWriter();

		// 세션에 저장된 companyDTO 객체 불러오기
		CompanyDTO company_dto = (CompanyDTO) session.getAttribute("companyInfo");

		System.out.println(company_dto);

		String str1 = company_number.substring(0, 3);
		String str2 = company_number.substring(3, 5);
		String str3 = company_number.substring(5);

		String companyNumber = str1 + "-" + str2 + "-" + str3;

		if (company_dto.getCompany_pwd().equals(company_pwd) && company_dto.getCompany_number().equals(companyNumber)) {

			String logoUploadDir = "C:\\Users\\clxkd\\OneDrive\\바탕 화면\\project1\\WorkWave\\Team2Project\\src\\main\\resources\\static\\image\\logo";

			uploadFileService.deleteFile(company_dto.getCompany_logo(), logoUploadDir);

			int result = this.companyMapper.companyDelete(company_dto.getCompany_key());

			System.out.println(result);

			if (result == 1) {
				session.invalidate();

				out.println("<script>");
				out.println("alert('회원삭제에 성공했습니다.')");
				out.println("location.href='/main'");
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

		// 컴퍼니 키 임시 저장
		dto.setCompany_key(2);

		if (this.comBoardMapper.addComBoard(dto) > 0) {
			temp_key = dto.getTemp_key();
			if (temp_key != 0)
				this.comBoardMapper.deleteComBoardTemp(temp_key);
			out.println("<script> alert('공고등록 성공'); location.href='/comBoard'; </script>");
		} else {
			out.println("<script> alert('공고등록 실패'); history.back(); </script>");
		}
		out.flush();
	}

	// (등록) 공고 중간저장
	@PostMapping("/comBoardTemp/insert")
	@ResponseBody
	public int addComBoardTemp(ComBoardDTO dto, HttpSession session, HttpServletRequest request) {
		int temp_key = dto.getTemp_key();
		// 세션 기업정보로 기업키 저장
//			CompanyDTO cdto = (CompanyDTO)session.getAttribute("companyInfo");
//			dto.setCompany_key(cdto.getCompany_key());

		// 컴퍼니 키 임시 저장
		dto.setCompany_key(2);

		if (temp_key != 0) {
			if (1 > 0) {
				System.out.println("공고 임시저장 수정 성공");
			}
		} else {
			if (this.comBoardMapper.addComBoardTemp(dto) > 0) {
				System.out.println("공고 임시저장 등록 성공");
				temp_key = this.comBoardMapper.selectTempPk(dto.getCompany_key());
			}
		}

		return temp_key;
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

		System.out.println("dto.getCom_board_key()sssssss");
		System.out.println(dto.getCom_board_key() + "dto.getCom_board_key()");

		if (this.comBoardMapper.updateComBoard(dto) > 0) {
			out.println("<script> alert('공고수정 성공'); location.href='/comBoard'; </script>"); // 마이페이지로 변경 예정
		} else {
			out.println("<script> alert('공고수정 실패'); history.back(); </script>");
		}
		out.flush();
	}

}