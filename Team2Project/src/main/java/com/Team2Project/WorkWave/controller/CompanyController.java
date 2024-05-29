package com.Team2Project.WorkWave.controller;


import java.io.IOException;
import java.io.PrintWriter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.Team2Project.WorkWave.model.CompanyDTO;
import com.Team2Project.WorkWave.model.CompanyMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;


@Controller
public class CompanyController {

	@Autowired
	private CompanyMapper mapper;
	
	@PostMapping("/company_login.go")
	public void login(@RequestParam("company_id") String company_id,
	                    @RequestParam("company_pwd") String company_pwd,
	                    HttpSession session, HttpServletResponse response) throws IOException {
	      
      response.setContentType("text/html; charset=UTF-8");
      
      PrintWriter out = response.getWriter();
      
      CompanyDTO company_login = this.mapper.docompanyLogin(company_id);
      
      // 로그인 실패 처리
      if(company_login == null) {
         // 아이디가 존재하지 않는 경우
    	 out.println("<script>");
    	 out.println("alert('아이디가 존재하지 않습니다.')");
    	 out.println("history.back()");
    	 out.println("</script>");
    	 out.flush();
      }else if(!company_login.getCompany_pwd().equals(company_pwd)) {
	       // 비밀번호가 틀린 경우
    	   out.println("<script>");
    	   out.println("alert('비밀번호가 틀렸습니다.')");
    	   out.println("history.back()");
           out.println("</script>");
           out.flush();
      }else {
    	  // 로그인 성공 처리
		  session.setAttribute("company_login", company_login);
		  session.setAttribute("member_type", "company");
		  out.println("<script>");
		  out.println("alert('성공.')");
		  out.println("location.href='/main.go'");
		  out.println("</script>");
          
      }
      
	}
	
	@GetMapping("company_insert.go")
	public String signUpForm(Model model) {
		model.addAttribute("company", new CompanyDTO());
		return "company/insert";
	}
	
	@GetMapping("/idcheck.go")
	@ResponseBody
	public String companyIdCheck(@RequestParam("id") String comId, HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		String res = "사용가능한 아이디입니다.";
		
		response.setContentType("text/html; charset=UTF-8");
		
		CompanyDTO idCheck = this.mapper.companyIdCheck(comId);
		
		System.out.println(idCheck);
		
		if(idCheck != null) {
			res = "이미 등록된 아이디입니다.";
		}
		
		return res;
		
	}
	
	@PostMapping("/company_insert_ok.go")
	public void companySignUp(CompanyDTO dto, HttpServletResponse response) throws IOException {
		String str1 = dto.getCompany_number().substring(0, 3);
		String str2 = dto.getCompany_number().substring(3, 5);
		String str3 = dto.getCompany_number().substring(5);
		
		dto.setCompany_number(str1+"-"+str2+"-"+str3);
		
		String com_mgr_phone1 = dto.getCompany_mgr_phone().substring(0, 3);
		String com_mgr_phone2 = dto.getCompany_mgr_phone().substring(3, 7);
		String com_mgr_phone3 = dto.getCompany_mgr_phone().substring(7);
		
		dto.setCompany_mgr_phone(com_mgr_phone1+"-"+com_mgr_phone2+"-"+com_mgr_phone3);
		
		if(dto.getCompany_phone().length()==8) {
			String com_phone1 = dto.getCompany_phone().substring(0, 4);
			String com_phone2 = dto.getCompany_phone().substring(4);
			
			dto.setCompany_phone(com_phone1+"-"+com_phone2);
		}else if(dto.getCompany_phone().length()==9) {
			String com_phone1 = dto.getCompany_phone().substring(0, 2);
			String com_phone2 = dto.getCompany_phone().substring(2, 5);
			String com_phone3 = dto.getCompany_phone().substring(5);
			
			dto.setCompany_phone(com_phone1+"-"+com_phone2+"-"+com_phone3);
		}else if(dto.getCompany_phone().length()==10) {
			if(dto.getCompany_phone().substring(0,2)=="02") {
				String com_phone1 = dto.getCompany_phone().substring(0, 2);
				String com_phone2 = dto.getCompany_phone().substring(2, 6);
				String com_phone3 = dto.getCompany_phone().substring(6);
				
				dto.setCompany_phone(com_phone1+"-"+com_phone2+"-"+com_phone3);
			}else {
				String com_phone1 = dto.getCompany_phone().substring(0, 3);
				String com_phone2 = dto.getCompany_phone().substring(3, 6);
				String com_phone3 = dto.getCompany_phone().substring(6);
				
				dto.setCompany_phone(com_phone1+"-"+com_phone2+"-"+com_phone3);
			}
		}else {
			String com_phone1 = dto.getCompany_phone().substring(0, 3);
			String com_phone2 = dto.getCompany_phone().substring(3, 7);
			String com_phone3 = dto.getCompany_phone().substring(7);
			
			dto.setCompany_phone(com_phone1+"-"+com_phone2+"-"+com_phone3);
		}
		
		int res = mapper.insertCompany(dto);
		
		response.setContentType("text/html; charset=UTF-8");
		
		PrintWriter out = response.getWriter();
		
		if(res == 1) {
			out.println("<script>");
			out.println("alert('회원가입을 완료하였습니다.')");
			out.println("location.href=company.go");
			out.println("</script>");
		}else {
			out.println("<script>");
			out.println("alert('회원가입을 실패하였습니다.')");
			out.println("history.back()");
			out.println("</script>");
		}
	}
	
	
	
	
}
