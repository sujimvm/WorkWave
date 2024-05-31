package com.Team2Project.WorkWave.controller;

import java.io.IOException;
import java.io.PrintWriter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.Team2Project.WorkWave.model.CompanyDTO;
import com.Team2Project.WorkWave.model.CompanyMapper;
import com.Team2Project.WorkWave.model.ProfileDTO;
import com.Team2Project.WorkWave.model.UserDTO;
import com.Team2Project.WorkWave.service.UploadFileService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class CompanyController {

	@Autowired
	private CompanyMapper mapper;

	@Autowired
	UploadFileService uploadFileService;

	// 기업회원 로그인
	@PostMapping("/company_login.go")
	public void login(@RequestParam("company_id") String company_id, @RequestParam("company_pwd") String company_pwd,
			HttpSession session, HttpServletResponse response) throws IOException {

		response.setContentType("text/html; charset=UTF-8");

		PrintWriter out = response.getWriter();

		CompanyDTO company_login = this.mapper.companyInfo(company_id);

		// 로그인 실패 처리
		if (company_login == null) {
			// 아이디가 존재하지 않는 경우
			out.println("<script>");
			out.println("alert('아이디가 존재하지 않습니다.')");
			out.println("history.back()");
			out.println("</script>");
			out.flush();
		} else if (!company_login.getCompany_pwd().equals(company_pwd)) {
			// 비밀번호가 틀린 경우
			out.println("<script>");
			out.println("alert('비밀번호가 틀렸습니다.')");
			out.println("history.back()");
			out.println("</script>");
			out.flush();
		} else {
			// 로그인 성공 처리
			session.setAttribute("companyInfo", company_login);
			session.setAttribute("member_type", "company");
			out.println("<script>");
			out.println("alert('성공.')");
			out.println("location.href='/main.go'");
			out.println("</script>");

		}

	}

	// 기업회원가입 페이지로 이동
	@GetMapping("company_insert.go")
	public String signUpForm(Model model) {
		model.addAttribute("company", new CompanyDTO());
		return "company/insert";
	}

	// 회원가입 완료
	@PostMapping("/company_insert_ok.go")
	public void companySignUp(@RequestParam("logo") MultipartFile file, CompanyDTO dto, HttpServletResponse response)
			throws IOException {
		String str1 = dto.getCompany_number().substring(0, 3);
		String str2 = dto.getCompany_number().substring(3, 5);
		String str3 = dto.getCompany_number().substring(5);

		dto.setCompany_number(str1 + "-" + str2 + "-" + str3);

		String com_mgr_phone1 = dto.getCompany_mgr_phone().substring(0, 3);
		String com_mgr_phone2 = dto.getCompany_mgr_phone().substring(3, 7);
		String com_mgr_phone3 = dto.getCompany_mgr_phone().substring(7);

		dto.setCompany_mgr_phone(com_mgr_phone1 + "-" + com_mgr_phone2 + "-" + com_mgr_phone3);

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

		dto.setCompany_homepage("http://" + dto.getCompany_homepage());
		System.out.println(file.getOriginalFilename().isEmpty());
		// 파일을 저장할 디렉토리 설정

		dto.setCompany_logo_name("");

		dto.setCompany_logo("");

		System.out.println(dto.getCompany_logo() + "g");
		System.out.println(dto.getCompany_logo_name() + "d");

		if (file.getOriginalFilename() != null) {
			if (file != null && !file.isEmpty()) {
				String logoUploadDir = "C:\\Users\\clxkd\\OneDrive\\바탕 화면\\project1\\WorkWave\\Team2Project\\src\\main\\resources\\static\\image\\logo";

				dto.setCompany_logo_name(file.getOriginalFilename());

				String imageName = uploadFileService.upload(file, logoUploadDir);
				dto.setCompany_logo(imageName);
			}

		}

		System.out.println(dto.getCompany_logo_name() + "t");

		int res = mapper.insertCompany(dto);

		response.setContentType("text/html; charset=UTF-8");

		PrintWriter out = response.getWriter();

		if (res == 1) {
			out.println("<script>");
			out.println("alert('회원가입을 완료하였습니다.')");
			out.println("location.href='company.go'");
			out.println("</script>");
		} else {
			out.println("<script>");
			out.println("alert('회원가입을 실패하였습니다.')");
			out.println("history.back()");
			out.println("</script>");
		}
	}

	// 기업회원 정보 수정하기전 비밀번호 확인 페이지 이동
	@GetMapping("/company_modify.go")
	public String companyModify(HttpSession session, Model model) {

		model.addAttribute("companyInfo", session.getAttribute("companyInfo"));

		return "company/modify";

	}

	// 비밀번호 입력후 기업회원 정보 수정 페이지로 이동
	@PostMapping("/company_modify_ok.go")
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
	@PostMapping("/comapany_update.go")
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

		int result = this.mapper.companyUpdate(dto);

		response.setContentType("text/html; charset=UTF-8");

		PrintWriter out = response.getWriter();

		if (result == 1) {
			session.removeAttribute("companyInfo");

			CompanyDTO updatedto = this.mapper.companyInfo(dto.getCompany_id());

			session.setAttribute("companyInfo", updatedto);
			session.setAttribute("member_type", "company");

			out.println("<script>");
			out.println("alert('회원정보수정 완료하였습니다.')");
			out.println("location.href='/main.go'");
			out.println("</script>");
		} else {
			out.println("<script>");
			out.println("alert('회원정보수정 완료하였습니다.')");
			out.println("history.back()");
			out.println("</script>");
		}

	}

	// 아이디 중복검사
	@PostMapping("/idcheck.go")
	@ResponseBody
	public String companyIdCheck(@RequestParam("id") String comId, HttpServletRequest request,
			HttpServletResponse response) throws IOException {

		String res = "사용가능한 아이디입니다.";

		response.setContentType("text/html; charset=UTF-8");

		CompanyDTO idCheck = this.mapper.companyInfo(comId);

		if (idCheck != null) {
			res = "이미 등록된 아이디입니다.";
		}

		return res;

	}

	// 아이디 찾기 성공
	@GetMapping("/comapny_find_id_ok.go")
	public String findCompanyId(@RequestParam("company_mgr_name") String company_mgr_name,
								@RequestParam("company_number") String company_number, 
								Model model, 
								HttpServletResponse response)
			throws IOException {

		response.setContentType("text/html; charset=UTF-8");
		
		String str1 = company_number.substring(0, 3);
		String str2 = company_number.substring(3, 5);
		String str3 = company_number.substring(5);

		String companyNumber = str1 + "-" + str2 + "-" + str3;

		String company_id = this.mapper.findCompanyId(company_mgr_name, companyNumber);
		
		System.out.println(company_id);

		PrintWriter out = response.getWriter();

		if (company_id == null) {
			out.println("<script>");
			out.println("alert('입력하신 정보에 해당하는 회원정보가 없습니다.')");
			out.println("location.href='/find_id.go'");
			out.println("</script>");
			
			return "user/find_id";
		} else {

			model.addAttribute("companyId", company_id);

			return "company/find_id_result";
		}

	}

	 // 비밀번호 찾기 성공	 
	@GetMapping("/company_find_password_ok.go") 
	public String findCompanyPwd(@RequestParam("company_mgr_name") String company_mgr_name,
			 					 @RequestParam("company_number") String company_number,
			 					 @RequestParam("company_id") String company_id, 
			 					 Model model,
			 					 HttpServletResponse response) throws IOException {
		 
		response.setContentType("text/html; charset=UTF-8");
		 
		String str1 = company_number.substring(0, 3);
		String str2 = company_number.substring(3, 5);
		String str3 = company_number.substring(5);

		String companyNumber = str1 + "-" + str2 + "-" + str3;
		
		String company_pwd = this.mapper.findCompanyPwd(company_mgr_name, companyNumber, company_id);
		
		System.out.println(company_pwd);

		PrintWriter out = response.getWriter();
		
		if (company_pwd == null) {
			out.println("<script>");
			out.println("alert('입력하신 정보에 해당하는 회원정보가 없습니다.')");
			out.println("location.href='/find_password.go'");
			out.println("</script>");
			
			return "user/find_password";
		} else {

			model.addAttribute("companyId", company_id);

			return "company/find_pwd_result";
		}
	
	}
	
	// 비밀번호찾기에서 해당 유저가 존재할 시 새 비밀번호를 설정하여 업데이트
	@PostMapping("/company_pwd_update")
	public void companyPwdUpdate(@RequestParam("company_id") String company_id ,
								 @RequestParam("company_pwd") String company_pwd,
								 HttpServletResponse response) throws IOException {		
		
		response.setContentType("text/html; charset=UTF-8");
		
		PrintWriter out = response.getWriter();
		
		CompanyDTO dto = this.mapper.companyInfo(company_id);
		
		if(company_pwd.equals(dto.getCompany_pwd())) {
			out.println("<script>");
			out.println("alert('기존 비밀번호와 새로 입력하신 비밀번호가 같습니다.')");
			out.println("history.back()");
			out.println("</script>");
		}else {
			int result = this.mapper.companyPwdUpdate(company_id,company_pwd);
			
			if(result == 1) {
				out.println("<script>");
				out.println("alert('비밀번호 수정에 성공했습니다.')");
				out.println("location.href='/login.go'");
				out.println("</script>");
			}else {
				out.println("<script>");
				out.println("alert('오류발생!')");
				out.println("history.back()");
				out.println("</script>");
			}
		}
		
	}
	
	// 회원 삭제 페이지로 이동
	@GetMapping("/company_delete.go")
	public String companyDelete(HttpSession session) {

		session.setAttribute("companyInfo", session.getAttribute("companyInfo"));

		return "company/delete";

	}
	
	@PostMapping("/company_delete_ok.go")
	public void companyDeleteOk(@RequestParam("company_pwd") String company_pwd,
								@RequestParam("company_number") String company_number,
								HttpSession session,
								HttpServletResponse response) throws IOException {
		
		response.setContentType("text/html; charset=UTF-8");
	      
	    PrintWriter out = response.getWriter();
		
	    // 세션에 저장된 companyDTO 객체 불러오기
	    CompanyDTO company_dto = (CompanyDTO) session.getAttribute("companyInfo");
	    
	    System.out.println(company_dto);
	    
	    String str1 = company_number.substring(0, 3);
		String str2 = company_number.substring(3, 5);
		String str3 = company_number.substring(5);

		String companyNumber = str1 + "-" + str2 + "-" + str3;
	    
	    if(company_dto.getCompany_pwd().equals(company_pwd)&&company_dto.getCompany_number().equals(companyNumber)) {
	    	
	    	String logoUploadDir = "C:\\Users\\clxkd\\OneDrive\\바탕 화면\\project1\\WorkWave\\Team2Project\\src\\main\\resources\\static\\image\\logo";

			uploadFileService.deleteFile(company_dto.getCompany_logo(), logoUploadDir);
	    	
	    	int result = this.mapper.companyDelete(company_dto.getCompany_key());
	    	
	    	System.out.println(result);
	    	
	    	if(result == 1) {
	    		session.invalidate();
	    		
	    		out.println("<script>");
				out.println("alert('회원삭제에 성공했습니다.')");
				out.println("location.href='/'");
				out.println("</script>");
	    	}else {
	    		out.println("<script>");
				out.println("alert('등록된 회원정보와 입력하신 정보가 다릅니다.')");
				out.println("history.back()");
				out.println("</script>");
	    	}
	    }
	}
	
	 
}
