package com.Team2Project.WorkWave.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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

   // 유저 로그인
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

   // 유저 회원가입 페이지 이동
   @GetMapping("/user_insert.go")
   public String userInsertForm(Model model) {
      model.addAttribute("user", new UserDTO());
      return "user/insert";
   }

   // 유저 회원가입 성공
   @PostMapping("/user_insert_ok.go")
   public String insertUser(UserDTO user,
		   					HttpServletResponse response) throws IOException {
	   
	  response.setContentType("text/html; charset=UTF-8");

	  PrintWriter out = response.getWriter();
	   
      userService.insertUser(user);
      
      out.println("<script>");
      out.println("alert('회원가입 성공')");
      out.println("</script>");
      
      return "index"; 
   }

   // 유저 정보 수정받기 전 비밀번호 확인 페이지로 이동
   @GetMapping("/user_modify.go")
   public String modify(HttpSession session) {
      
      UserDTO userInfo = (UserDTO)session.getAttribute("user_login");
      
      session.setAttribute("userInfo", userInfo);
      
      return "user/modify";

   }

   // 유저 수정 비밀번호 확인 성공 시 유저 수정 페이지로 이동
   @PostMapping("/user_modify_ok.go")
   public String modifyOk(@RequestParam("user_pwd") String pwd,
                     HttpSession session,
                      HttpServletResponse response) throws IOException {

      response.setContentType("text/html; charset=UTF-8");

      PrintWriter out = response.getWriter();
      
      UserDTO userInfo = (UserDTO)session.getAttribute("user_login");

      if (userInfo.getUser_pwd().equals(pwd)) {
         out.println("<script>");
         out.println("alert('비밀번호가 일치합니다.')");
         out.println("</script>");
         return "user/modify_ok";
      } else {
         out.println("<script>");
         out.println("alert('비밀번호가 틀렸습니다')");
         out.println("</script>");
         return "user/modify";
      }
   }
   
   // 유저 회원 수정 성공 시 경로
   @PostMapping("/user_update.go")
   public void updateok(UserDTO dto, HttpServletResponse response) throws IOException {

      response.setContentType("text/html; charset=UTF-8");

      PrintWriter out = response.getWriter();

      int result = this.mapper.updateok(dto);

      if (result > 0) {
         out.println("<script>");
         out.println("alert('회원 수정 성공')");
         out.println("location.href='main.go'");
         out.println("</script>");
      } else {
         out.println("<script>");
         out.println("alert('회원 수정 실패')");
         out.println("history.back()");
         out.println("</script>");
      }

   }
   
   // 유저 정보 삭제 시 삭제 페이지 이동
   @GetMapping("/user_delete.go")
   public String delete(HttpSession session) {
	   
      UserDTO delete = (UserDTO)session.getAttribute("user_login");
      
      session.setAttribute("del", delete);
      
      return "user/delete";
      
   }
   
   
   @PostMapping("/user_delete_ok.go")
   public void deleteok(@RequestParam("user_pwd") String userPwd, HttpSession session, HttpServletResponse response)
         throws IOException {
	   
      response.setContentType("text/html; charset=UTF-8");
      
      PrintWriter out = response.getWriter();

      // 세션에 저장된 유저 정보를 가져옴
      UserDTO user = (UserDTO) session.getAttribute("user_login");

      if (user != null) {
         // 입력된 비밀번호와 세션에 저장된 유저의 비밀번호가 일치하는지 확인
         if (userPwd.equals(user.getUser_pwd())) {
        	 
            int result = this.mapper.deleteok(user.getUser_key());

            if (result > 0) {

               out.println("<script>");
               out.println("alert('회원 삭제 성공')");
               out.println("location.href='user.go'");
               out.println("</script>");
            } else {
               out.println("<script>");
               out.println("alert('회원 삭제 실패')");
               out.println("history.back()");
               out.println("</script>");
            }
         } else {
            out.println("<script>");
            out.println("alert('비밀번호가 일치하지 않습니다.')");
            out.println("history.back()");
            out.println("</script>");
         }
      } else {
         out.println("<script>");
         out.println("alert('회원 정보를 찾을 수 없습니다.')");
         out.println("history.back()");
         out.println("</script>");
      }
   }

   // 아이디 유효성 검사
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

   // 아이디 찾기 페이지 이동
   @GetMapping("/find_id.go")
   public String findIdForm() {
      return "user/find_id";
   }

   // 비밀번호 찾기 페이지 이동
   @GetMapping("/find_password.go")
   public String findPwdForm() {
      return "user/find_password";
   }

   // 아이디 찾기 성공
   @GetMapping("/find_id_ok.go")
   public String findUserId(@RequestParam("user_name") String userName, @RequestParam("user_email") String userEmail,
         Model model) {
      String user_id = userService.findUserId(userName, userEmail);
      model.addAttribute("userId", user_id);
      return "user/find_id_result";
   }

   // 비밀번호 찾기 성공
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

}