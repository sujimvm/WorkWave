package com.Team2Project.WorkWave.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.Team2Project.WorkWave.model.UserDTO;
import com.Team2Project.WorkWave.model.UserMapper;
import com.Team2Project.WorkWave.service.UserService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
public class UserController {

	@Autowired
	private UserMapper mapper;

	@Autowired
	private UserService userService;

	@PostMapping("/user_login.go")
	public void login(@RequestParam("user_id") String user_id, @RequestParam("user_pwd") String user_pwd,
			HttpSession session, HttpServletResponse response) throws IOException {

		response.setContentType("text/html; charset=UTF-8");

		PrintWriter out = response.getWriter();

		UserDTO user_login = this.mapper.doLogin(user_id);

		// 로그인 실패 처리
		if (user_login == null) {
			// 아이디가 존재하지 않는 경우
			out.println("<script>");
			out.println("alert('아이디가 존재하지 않습니다.')");
			out.println("history.back()");
			out.println("</script>");
			out.flush();
		} else if (!user_login.getUser_pwd().equals(user_pwd)) {
			// 비밀번호가 틀린 경우
			out.println("<script>");
			out.println("alert('비밀번호가 틀렸습니다.')");
			out.println("history.back()");
			out.println("</script>");
			out.flush();
		} else {
			// 로그인 성공 처리
			session.setAttribute("user_login", user_login);
			session.setAttribute("member_type", "user");
			out.println("<script>");
			out.println("alert('성공.')");
			out.println("location.href='main.go'");
			out.println("</script>");

		}

	}

	@GetMapping("/user_insert.go")
	public String userInsertForm(Model model) {
		model.addAttribute("user", new UserDTO());
		return "user/insert";
	}

	@PostMapping("/insertUser")
	public String insertUser(UserDTO user) {
		userService.insertUser(user);
		return "user/insert_success"; // 성공 페이지로 리디렉션
	}

	@PostMapping("/checkUserId")
	@ResponseBody
	public Map<String, String> checkUserId(@RequestBody Map<String, String> request) {
		String userId = request.get("userId");
		boolean isAvailable = userService.isUserIdAvailable(userId);
		Map<String, String> response = new HashMap<>();
		if (isAvailable) {
			response.put("status", "available");
		} else {
			response.put("status", "unavailable");
		}
		return response;
	}

	@GetMapping("/find_id.go")
	public String findIdForm() {
		return "user/find_id";
	}

	@GetMapping("/find_password.go")
	public String findPwdForm() {
		return "user/find_password";
	}

	@GetMapping("/find_id_ok.go")
	public String findUserId(@RequestParam("user_name") String userName, @RequestParam("user_email") String userEmail,
			Model model) {
		String user_id = userService.findUserId(userName, userEmail);
		model.addAttribute("userId", user_id);
		return "user/find_id_result";
	}

	@GetMapping("/find_password_ok.go")
	public String findUserPassword(@RequestParam("user_name") String userName, @RequestParam("user_id") String userId,
			@RequestParam("user_email") String userEmail, Model model) {
		// 사용자의 비밀번호를 검색
		UserDTO user_pwd = userService.findUserPassword(userName, userId, userEmail);

		// 검색된 비밀번호 정보를 모델에 추가
		model.addAttribute("user_pwd", user_pwd);

		// 비밀번호 결과를 표시할 뷰 이름을 반환
		return "user/find_password_result";
	}

	@GetMapping("/user_modify.go")
	public String modify(@RequestParam("num") String user_id, Model model) {

		UserDTO modify = this.mapper.modify(user_id);

		model.addAttribute("Modify", modify);

		return "user/modify";

	}

 
	  @PostMapping("/user_modify_ok.go")
	  public String modifyOk(@RequestParam("user_pwd") String pwd, HttpSession session, RedirectAttributes redirectAttributes) throws IOException {

	      UserDTO dto = (UserDTO) session.getAttribute("user_login");
	      UserDTO modify = this.mapper.modify(dto.getUser_id());

	      if (modify.getUser_pwd().equals(pwd)) {
	          redirectAttributes.addFlashAttribute("Modify", modify);
	          return "redirect:/update.go";
	      } else {
	          return "redirect:/user_modify.go"; 
	      }
	  }

	 @GetMapping("/update.go")
	 public String updatePage(@ModelAttribute("Modify") UserDTO modify, Model model) {
	        
		 model.addAttribute("Modify", modify);
		 
	     return "user/modify_ok"; 
	    }
	 
	 @PostMapping("/user_update.go")
	 public void updateok(UserDTO dto, HttpServletResponse response) throws IOException {
		 
		response.setContentType("text/html; charset=UTF-8");
			
		PrintWriter out = response.getWriter();
		
		int result = this.mapper.updateok(dto);
		
		
		if(result > 0) {
			out.println("<script>");
			out.println("alert('회원 수정 성공')");
			out.println("location.href='main.go'");
			out.println("</script>");
		}else {
			out.println("<script>");
			out.println("alert('회원 수정 실패')");
			out.println("history.back()");
			out.println("</script>");
		}
	}
	 
	
}
	 
	 


