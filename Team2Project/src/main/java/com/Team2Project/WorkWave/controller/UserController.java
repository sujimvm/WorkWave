package com.Team2Project.WorkWave.controller;


import java.io.IOException;
import java.io.PrintWriter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.Team2Project.WorkWave.model.UserDTO;
import com.Team2Project.WorkWave.model.UserMapper;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
public class UserController {

	@Autowired
	private UserMapper mapper;
	
	@PostMapping("/user_login.go")
	public void login(@RequestParam("user_id") String user_id,
	                    @RequestParam("user_pwd") String user_pwd,
	                    HttpSession session, HttpServletResponse response) throws IOException {
	      
      response.setContentType("text/html; charset=UTF-8");
      
      PrintWriter out = response.getWriter();
      
      UserDTO user_login = this.mapper.doLogin(user_id);
      
      // 로그인 실패 처리
      if(user_login == null) {
         // 아이디가 존재하지 않는 경우
    	 out.println("<script>");
    	 out.println("alert('아이디가 존재하지 않습니다.')");
    	 out.println("history.back()");
    	 out.println("</script>");
    	 out.flush();
      }else if(!user_login.getUser_pwd().equals(user_pwd)) {
	       // 비밀번호가 틀린 경우
    	   out.println("<script>");
    	   out.println("alert('비밀번호가 틀렸습니다.')");
    	   out.println("history.back()");
           out.println("</script>");
           out.flush();
      }else {
    	  // 로그인 성공 처리
		  session.setAttribute("user_login", user_login);
		  session.setAttribute("user_type", "user");
		  out.println("<script>");
		  out.println("alert('성공.')");
		  out.println("location.href='/'");
		  out.println("</script>");
          
      }
      
      
            
   }
}
