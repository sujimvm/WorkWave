package com.Team2Project.WorkWave.config;

import java.io.IOException;
import java.io.PrintWriter;

import org.springframework.security.web.session.SessionInformationExpiredEvent;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomSessionExpiredHandler implements SessionInformationExpiredStrategy{

	@Override
	public void onExpiredSessionDetected(SessionInformationExpiredEvent event) throws IOException, ServletException {
	
		HttpServletResponse response = event.getResponse();
		
	}

}
