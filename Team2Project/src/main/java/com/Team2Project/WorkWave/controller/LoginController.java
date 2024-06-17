package com.Team2Project.WorkWave.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.Team2Project.WorkWave.model.ChatMapper;
import com.Team2Project.WorkWave.model.ComBoardMapper;
import com.Team2Project.WorkWave.model.CompanyMapper;
import com.Team2Project.WorkWave.model.NoticeDTO;
import com.Team2Project.WorkWave.model.NoticeMapper;
import com.Team2Project.WorkWave.model.UserMapper;

import jakarta.servlet.http.HttpSession;

@Controller
public class LoginController {
	

	@Autowired private ChatMapper chatMapper;
	@Autowired private ComBoardMapper comBoardMapper;
	@Autowired private CompanyMapper companyMapper;
	@Autowired private NoticeMapper noticeMapper;
	@Autowired private UserMapper userMapper;

	@GetMapping("/login")
    public String userLogin() {
       return "mainLogin";
    }
	
	@GetMapping("/")
	public String goMain(HttpSession session, Model model) { 

		int com_board_count = this.comBoardMapper.countComBoard();
		int user_count = this.userMapper.countUser();
		int company_count = this.companyMapper.countCompany();
		List<NoticeDTO> main_notice_list = this.noticeMapper.mainNoticeList();

		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		System.out.println(auth +"gd");
		
			String role = auth.getAuthorities().toString();
			String id = auth.getName();
			if(role.equals("[ROLE_COMPANY]")) {
				session.setAttribute("role", role);
				session.setAttribute("cDTO", companyMapper.companyInfo(id));
			}else if(role.equals("[ROLE_USER]")){
				session.setAttribute("role", role);
				session.setAttribute("uDTO", userMapper.getUserById(id));
			}

		// 모델에 데이터 추가
		model.addAttribute("mainNoticeList", main_notice_list);
        model.addAttribute("ComBoardCount", com_board_count);
		model.addAttribute("UsersCount", user_count);
        model.addAttribute("CompanyCount", company_count);
			
		return "main"; 
	}
	
	
}
