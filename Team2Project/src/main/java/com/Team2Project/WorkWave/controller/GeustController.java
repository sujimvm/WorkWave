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
import org.springframework.web.multipart.MultipartFile;

import com.Team2Project.WorkWave.model.*;
import com.Team2Project.WorkWave.service.*;

import jakarta.servlet.http.HttpServletResponse;


@Controller("/G")
public class GeustController {

    @Autowired private CompanyMapper companyMapper;

    @Autowired private PasswordEncoder passwordEncoder;

    @Autowired private UploadFileService uploadFileService;
    @Autowired private UserService userService;
   
    // 기업회원가입 페이지로 이동
	@GetMapping("/companyJoin")
	public String signUpForm(Model model) {
		model.addAttribute("company", new CompanyDTO());
		return "company/insert";
	}
	
	// 유저 회원가입 페이지 이동
	@GetMapping("/userJoin")
	public String userInsertForm(Model model) {
		model.addAttribute("user", new UserDTO());
		return "user/insert";
	}
	
	// 회원가입 완료
	@PostMapping("/companyJoinOk")
	public void companySignUp(@RequestParam("logo") MultipartFile file,
							  CompanyDTO dto, 
							  HttpServletResponse response)
			throws IOException {
		
		String encordedPwd = this.passwordEncoder.encode(dto.getCompany_pwd());
		
		dto.setCompany_pwd(encordedPwd);
		dto.setRole("ROLE_COMPANY");
		
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

				String imageName = this.uploadFileService.upload(file, logoUploadDir);
				dto.setCompany_logo(imageName);
			}

		}

		System.out.println(dto.getCompany_logo_name() + "t");

		int res = this.companyMapper.insertCompany(dto);

		response.setContentType("text/html; charset=UTF-8");

		PrintWriter out = response.getWriter();

		if (res == 1) {
			out.println("<script>");
			out.println("alert('회원가입을 완료하였습니다.')");
			out.println("location.href='/login'");
			out.println("</script>");
		} else {
			out.println("<script>");
			out.println("alert('회원가입을 실패하였습니다.')");
			out.println("history.back()");
			out.println("</script>");
		}
	}
	
	// 유저 회원가입 성공
	@PostMapping("/userJoinOk")
	public String insertUser(UserDTO user,
                        HttpServletResponse response) throws IOException {
	   
		String encordedPwd = passwordEncoder.encode(user.getUser_pwd());
		
		user.setUser_pwd(encordedPwd);
		user.setRole("ROLE_USER");
      
		response.setContentType("text/html; charset=UTF-8");

		PrintWriter out = response.getWriter();
      
		userService.insertUser(user);
      
		out.println("<script>");
		out.println("alert('회원가입 성공')");
		out.println("</script>");
      
		return "index"; 
	}
	
	// 아이디 찾기 페이지 이동
	@GetMapping("/findId")
	public String findIdForm() {
		return "user/find_id";
	}
	
	// 비밀번호 찾기 페이지 이동
	@GetMapping("/findPwd")
	public String findPwdForm() {
		return "user/find_password";
	}
	
	// 기업 아이디 찾기 성공
	@GetMapping("/companyFindId")
	public void findCompanyId(@RequestParam("company_mgr_name") String company_mgr_name,
								@RequestParam("company_number") String company_number, 
								Model model, 
								HttpServletResponse response)
			throws IOException {

		response.setContentType("text/html; charset=UTF-8");
		
		String str1 = company_number.substring(0, 3);
		String str2 = company_number.substring(3, 5);
		String str3 = company_number.substring(5);

		String companyNumber = str1 + "-" + str2 + "-" + str3;

		String company_id = this.companyMapper.findCompanyId(company_mgr_name, companyNumber);
		
		System.out.println(company_id);

		PrintWriter out = response.getWriter();

		if (company_id == null) {
			out.println("<script>");
			out.println("alert('입력하신 정보에 해당하는 회원정보가 없습니다.')");
			out.println("location.href='/G/findId'");
			out.println("</script>");
	
		} else {

			model.addAttribute("companyId", company_id);

		}

	}
	
	// 개인 아이디 찾기 성공
	@GetMapping("/userFindIdOk")
	public String findUserId(@RequestParam("user_name") String userName, @RequestParam("user_email") String userEmail,
			Model model) {
		String user_id = userService.findUserId(userName, userEmail);
		model.addAttribute("userId", user_id);
		return "user/find_id_result";
	}
	
	// 기업 비밀번호 찾기 성공	 
	@GetMapping("/companyFindPwdOk") 
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
		
		String company_pwd = this.companyMapper.findCompanyPwd(company_mgr_name, companyNumber, company_id);
		
		System.out.println(company_pwd);

		PrintWriter out = response.getWriter();
		
		if (company_pwd == null) {
			out.println("<script>");
			out.println("alert('입력하신 정보에 해당하는 회원정보가 없습니다.')");
			out.println("</script>");
			return "user/find_pwd";
		} else {

			model.addAttribute("companyId", company_id);
			return "company/find_pwd_result";

		}
	
	}
	
	// 개인 비밀번호 찾기 성공
	@GetMapping("/userFindPwdOk")
	public String findUserPassword(@RequestParam("user_name") String userName, @RequestParam("user_id") String userId,
			@RequestParam("user_email") String userEmail, Model model) {
		// 사용자의 비밀번호를 검색
		UserDTO user_pwd = userService.findUserPassword(userName, userId, userEmail);

		// 검색된 비밀번호 정보를 모델에 추가
		model.addAttribute("user_pwd", user_pwd);

		// 비밀번호 결과를 표시할 뷰 이름을 반환
		return "user/find_password_result";
	}
}