package com.Team2Project.WorkWave.config;

import java.io.IOException;
import java.net.URLEncoder;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CustomLoginFailureHandler implements AuthenticationFailureHandler{

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
		
		String errorMessage;
		
		
		if(exception instanceof BadCredentialsException) {
			errorMessage = "아이디 또는 비밀번호가 잘못되었습니다.";
		}else if(exception instanceof UsernameNotFoundException) {
			errorMessage = "아이디가 존재하지 않습니다.";
		}else {
			errorMessage = "로그인에 실패하였습니다. 관리자에게 문의하여 주십시오.";
		}
		
		System.out.println(errorMessage);
		errorMessage = URLEncoder.encode(errorMessage, "UTF-8");
		String redirectUrl = request.getContextPath() + "/login?error=" + errorMessage;
        response.sendRedirect(redirectUrl);
		
	}

}
