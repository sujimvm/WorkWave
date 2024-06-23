package com.Team2Project.WorkWave.controller;


import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.Team2Project.WorkWave.model.*;
import com.Team2Project.WorkWave.service.*;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;


@Controller
@RequestMapping("/U")
public class UserController {

   @Autowired private ChatMapper chatMapper;
   @Autowired private ProfileMapper profileMapper;
   @Autowired private UserMapper userMapper;
   @Autowired private UploadFileService uploadFileService;
   @Autowired private PasswordEncoder passwordEncoder;
   
   
   // 유저 상세정보를 보여주는 마이페이지로 이동
   @GetMapping("/info")
   public String content(HttpSession session, Model model) {
      
      UserDTO userInfo = (UserDTO)session.getAttribute("uDTO");
	   
      int applyCnt = this.userMapper.applyCnt(userInfo.getUser_key());
      int applyCheckCnt = this.userMapper.applyCheckCnt(userInfo.getUser_key());
      int positionJean = this.userMapper.positionJean(userInfo.getUser_key());
      int interest = this.userMapper.interest(userInfo.getUser_key());
      String profileName = this.userMapper.profileName(userInfo.getUser_key());
         
      // 지원완료 갯수
      model.addAttribute("applyCnt", applyCnt);
      // 이력서 열람 갯수
      model.addAttribute("applyCheckCnt", applyCheckCnt);
      // 포지션 제안 갯수
      model.addAttribute("positionJean", positionJean);
      // 관심 기업 갯수
      model.addAttribute("interest", interest);
      // 이력서 제목
      model.addAttribute("profileName", profileName);
      
      return "user/cont";
   }
   
    @GetMapping("/applyList")
	public String userApply(HttpSession session, Model model) {
	   
	  UserDTO userInfo = (UserDTO)session.getAttribute("uDTO");
	  
	  // applyInfo -> 지원일
	  List<ApplyDTO> applyInfoList = this.userMapper.applyInfo(userInfo.getUser_key());
	  // 지원한 공고 이름
	  List<ComBoardDTO> applyBoardList = new ArrayList<>();
	  // 지원한 공고를 등록한 기업 이름
	  List<CompanyDTO> applyComList = new ArrayList<>();
	  
	  for(ApplyDTO applyInfo : applyInfoList) {
		  ComBoardDTO applyBoard = this.userMapper.applyBoard(applyInfo.getCom_board_key());
		  applyBoardList.add(applyBoard);
		  
		  CompanyDTO applyCom = this.userMapper.applyCom(applyBoard.getCompany_key());
		  applyComList.add(applyCom);
	  }
	  
	  int applyCnt = this.userMapper.applyCnt(userInfo.getUser_key()); 
	  int applyCheckCnt = this.userMapper.applyCheckCnt(userInfo.getUser_key());
	  int UapplyNonCheckCnt = this.userMapper.UapplyNonCheckCnt(userInfo.getUser_key());
	  int applyCancel = this.userMapper.applyCancel(userInfo.getUser_key());
		  
	  // 지원일
	  model.addAttribute("applyInfoList", applyInfoList);
	  // 지원한 공고
	  model.addAttribute("applyBoardList", applyBoardList);
	  // 지원한 공고를 등록한 기업
	  model.addAttribute("applyComList", applyComList);
	  // 지원완료 갯수
	  model.addAttribute("applyCnt", applyCnt);
	  // 이력서 열람 갯수
	  model.addAttribute("applyCheckCnt", applyCheckCnt);
      // 이력서 미열람 갯수
	  model.addAttribute("UapplyNonCheckCnt", UapplyNonCheckCnt);
      // 지원취소 갯수
	  model.addAttribute("applyCancel", applyCancel);
	   
	  return "user/applyList";
	}
    
    // 유저 기업으로부터 포지션제안 받은 포지션리스트
    @GetMapping("/userPosition")
    public String userPosition(HttpSession session, Model model) {
    	
    	UserDTO userInfo = (UserDTO)session.getAttribute("uDTO");
    	
    	// 유저가 받은 포지션 제안 정보
    	List<PositionDTO> positionInfoList = this.userMapper.positionInfo(userInfo.getUser_key());
    	// 포지션 제안을 보낸 기업 이름
  	    List<CompanyDTO> positionComList = new ArrayList<>();
  	    
  	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    	
    	for(PositionDTO positionInfo : positionInfoList) {
    		CompanyDTO positionCom = this.userMapper.positionCompany(positionInfo.getCompany_key());
    		positionComList.add(positionCom);
    		
    		LocalDate positionDate = LocalDate.parse(positionInfo.getPosition_date().substring(0, 10), formatter);
            LocalDate expirationDate = positionDate.plusDays(7);
            positionInfo.setExpirationDate(expirationDate.format(formatter));
    	}
    	
    	
    	
    	// 유저가 받은 포지션 제안 정보
    	model.addAttribute("positionInfoList", positionInfoList)
    	// 포지션 제안을 보낸 기업 이름
    		 .addAttribute("positionComList", positionComList);
    	
    	return "user/positionList";
    }
   

   // 유저 정보 수정받기 전 비밀번호 확인 페이지로 이동
   @GetMapping("/update/userPwdCheck")
   public String modify(HttpSession session) {
      
      UserDTO userInfo = (UserDTO)session.getAttribute("user_login");
      
      
      session.setAttribute("userInfo", userInfo);
      
      return "user/modify";

   }

   // 유저 수정 비밀번호 확인 성공 시 유저 수정 페이지로 이동
   @PostMapping("/update/userPwdCheckOk")
   public String modifyOk(@RequestParam("user_pwd") String pwd,
                     HttpSession session,
                      HttpServletResponse response) throws IOException {

      response.setContentType("text/html; charset=UTF-8");

      PrintWriter out = response.getWriter();
      
      UserDTO userInfo = (UserDTO)session.getAttribute("uDTO");

      if (passwordEncoder.matches(pwd, userInfo.getUser_pwd())) {
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
   
   @PostMapping("/update")
   public void userInfoUpdate(UserDTO dto, HttpServletResponse response) throws IOException {
	   
	   int result = this.userMapper.updateok(dto);

	   response.setContentType("text/html; charset=UTF-8");

	   PrintWriter out = response.getWriter();
	   
	   if(result == 1) {
		   out.println("<script>");
		   out.println("alert('회원정보수정 완료하였습니다.')");
		   out.println("location.href='/U/info'");
		   out.println("</script>");
	   }else {
		   out.println("<script>");
		   out.println("alert('회원정보수정 실패하였습니다.')");
		   out.println("history.back()");
		   out.println("</script>");
	   }
   }
   
   // 유저 정보 삭제 시 삭제 페이지 이동
   @GetMapping("/delete/userPwdCheck")
   public String delete(HttpSession session) {
      
      UserDTO delete = (UserDTO)session.getAttribute("user_login");
      
      session.setAttribute("del", delete);
      
      return "user/delete";
      
   }
   
   // 유저 정보 삭제 페이지에서 비밀번호 확인 후 유저 마이페이지로 이동
   @PostMapping("/delete/userPwdCheckOk")
   public void deleteok(@RequestParam("user_pwd") String userPwd, HttpSession session, HttpServletResponse response)
         throws IOException {
      
      response.setContentType("text/html; charset=UTF-8");
      
      PrintWriter out = response.getWriter();

      // 세션에 저장된 유저 정보를 가져옴
      UserDTO user = (UserDTO) session.getAttribute("user_login");

      if (user != null) {
         // 입력된 비밀번호와 세션에 저장된 유저의 비밀번호가 일치하는지 확인
         if (userPwd.equals(user.getUser_pwd())) {
            
            int result = this.userMapper.deleteok(user.getUser_key());

            if (result > 0) {

               out.println("<script>");
               out.println("alert('회원 삭제 성공')");
               out.println("location.href='/info'");
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
   
// 이력서 추가 메서드
	@PostMapping("/profile/insertOk")
	public void profileInsert(ProfileDTO dto, HttpServletResponse response, HttpSession session, 
			@ModelAttribute(value = "CodeListDTO") CodeListDTO codelistDTO,
			@RequestParam("profile_ppt_input") MultipartFile ppt_file,
			@RequestParam("profile_image_input")MultipartFile image_file
			) throws IOException {
		
		//String imageUploadDir = "C:\\Users\\BSH\\git\\WorkWave\\Team2Project\\src\\main\\resources\\static\\image\\profile";
		
		String userDir = System.getProperty("user.dir");
	      String imageUploadDir = userDir+"\\src\\main\\resources\\static\\image\\profile";
		
		//String pptUploadDir = "C:\\Users\\BSH\\git\\WorkWave\\Team2Project\\src\\main\\resources\\static\\ppt\\profile";
		
		
	      String pptUploadDir = userDir+"\\src\\main\\resources\\static\\ppt\\profile";

		if (image_file.getOriginalFilename() != null) {
			if (image_file != null && !image_file.isEmpty()) {
				String imageName = uploadFileService.upload(image_file, imageUploadDir);
				dto.setProfile_image(imageName);
				dto.setProfile_image_name(image_file.getOriginalFilename());
			}
		}

		dto.setProfile_ppt("");
		dto.setProfile_ppt_name("");
		
		if (ppt_file.getOriginalFilename() != null) {
			if (ppt_file != null && !ppt_file.isEmpty()) {
				String pptName = uploadFileService.uploadOriName(ppt_file, pptUploadDir);
				dto.setProfile_ppt(pptName);
				dto.setProfile_ppt_name(ppt_file.getOriginalFilename());
			}
		}
		
		
		UserDTO userdto = (UserDTO) session.getAttribute("uDTO");

	
		dto.setUser_key(userdto.getUser_key());

		
		// 넘어온 키 값의 이력서가 있는지 확인
		if (this.profileMapper.profileCkeck(dto.getUser_key()) > 0) {
			dto.setProfile_default("N");
		} else {
			dto.setProfile_default("Y");
		}

	
		int check = this.profileMapper.profileInsert(dto);
		

	    int nowInsertProfileKey = dto.getProfile_key();
		
		//학력 데이터 저장
		EduDTO edtoArr = codelistDTO.getEDtoList().get(0);
		for(int i=0; i < edtoArr.getEdu_name().split(",").length; i++) {
			EduDTO edto = new EduDTO();
			
			edto.setProfile_key(nowInsertProfileKey);
			edto.setEdu_kind(edtoArr.getEdu_kind().split(",")[i]);
			edto.setEdu_name(edtoArr.getEdu_name().split(",")[i]);
			edto.setEdu_start_date(edtoArr.getEdu_start_date().split(",")[i]);
			edto.setEdu_end_date(edtoArr.getEdu_end_date().split(",")[i]);
			edto.setEdu_major(edtoArr.getEdu_major().split(",")[i]);
			edto.setEdu_status(edtoArr.getEdu_status().split(",")[i]);
			  this.profileMapper.EduInsert(edto); 
			
		}
		
		 
		
	
		  //경력 데이터 저장 
		CareerDTO crdtoArr = codelistDTO.getCrDtoList().get(0); 
		for(int i= 0; i < crdtoArr.getCareer_company().split(",").length; i++) {
		  CareerDTO crdto = new CareerDTO();
		  
		  crdto.setProfile_key(nowInsertProfileKey);
		  crdto.setCareer_company(crdtoArr.getCareer_company().split(",")[i]);
		  crdto.setCareer_start_date(crdtoArr.getCareer_start_date().split(",")[i]);
		  crdto.setCareer_end_date(crdtoArr.getCareer_end_date().split(",")[i]);
		  crdto.setCareer_cont(crdtoArr.getCareer_cont().split(",")[i]);
		  crdto.setCareer_position(crdtoArr.getCareer_position().split(",")[i]);
		  crdto.setCareer_bye(crdtoArr.getCareer_bye().split(",")[i]);
		  
		  this.profileMapper.CareerInsert(crdto);
		  
		  }
		  
		  //자격증 데이터 저장 
		LicenseDTO ldtoArr = codelistDTO.getLDtoList().get(0);
		  
		  for(int i=0; i < ldtoArr.getLicense_name().split(",").length; i++) {
		  
			  LicenseDTO ldto = new LicenseDTO();
			  
			  ldto.setProfile_key(nowInsertProfileKey);
			  ldto.setLicense_name(ldtoArr.getLicense_name().split(",")[i]);
			  ldto.setLicense_company(ldtoArr.getLicense_company().split(",")[i]);
			  ldto.setLicense_date(ldtoArr.getLicense_date().split(",")[i]);
			  
			  this.profileMapper.LicenseInsert(ldto); 
		  }
			response.setContentType("text/html; charset=UTF-8");
			PrintWriter out = response.getWriter();
			if (check > 0) {
				out.println("<script>");
				out.println("alert('이력서 작성을 성공하였습니다.')");
				out.println("location.href='/U/profile'");
				out.println("</script>");
			} else {
				out.println("<script>");
				out.println("alert('이력서 작성을 실패하였습니다.')");
				out.println("history.back()");
				out.println("</script>");
			}
	}
	
	// 이력서 작성 폼
		@GetMapping("/profile/insert")
		public String categorygrouptest(Model model,HttpSession session) {
			
		UserDTO user = (UserDTO) session.getAttribute("uDTO");
		
		int userKey = user.getUser_key();
			
			
			UserDTO userDto = this.profileMapper.fromProfileUserInfo(userKey);

			List<CodeDTO> category = this.profileMapper.category();

			model.addAttribute("categories", category);
			
			model.addAttribute("userDto", userDto);

			return "profile/profile";
		}
		
		// 이력서 리스트로 이동
		@GetMapping("/profile")
		public String profileList(Model model, HttpSession session) {

			UserDTO user = (UserDTO) session.getAttribute("uDTO");
			
			int userKey = user.getUser_key();
			
			
			List<ProfileDTO> profileList = this.profileMapper.profileList(userKey);
			
			model.addAttribute("ProfileList", profileList);
			

			return "profile/profile_List";
		}
		
		// 기본 이력서로 변경
		@GetMapping("/profile/default")
		public void profileDefault(@RequestParam("no") int defaultKey, HttpServletResponse response) throws IOException {

			// 기본 이력서로 변경
			this.profileMapper.defaultChangeN();
			// 대표 이력서로 변경
			int check = this.profileMapper.defaultChangeY(defaultKey);

			response.setContentType("text/html; charset=UTF-8");
			PrintWriter out = response.getWriter();

			if (check > 0) {
				out.println("<script>");
				out.println("alert('대표 이력서로 변경하였습니다.')");
				out.println("location.href='/U/profile'");
				out.println("</script>");
			} else {
				out.println("<script>");
				out.println("alert('대표 이력서로 변경 실패하였습니다.')");
				out.println("history.back()");
				out.println("</script>");
			}

		}

		
		//이력서 삭제
		@GetMapping("/profile/delect")
		public void profile_delect(@RequestParam("no") int pro_key,HttpServletResponse response) throws IOException {
			
			 int checkCareer = this.profileMapper.deleteCareerByProfileKey(pro_key);
			 int checkEdu = this.profileMapper.deleteEduByProfileKey(pro_key);
			 int checkLicense = this.profileMapper.deleteLicenseByProfileKey(pro_key);

			
			
			int check = this.profileMapper.profileDelect(pro_key);
			
			response.setContentType("text/html; charset=UTF-8");
			PrintWriter out = response.getWriter();

			if (check > 0) {
				out.println("<script>");
				out.println("alert('이력서를 삭제 하였습니다.')");
				out.println("location.href='/U/profile'");
				out.println("</script>");
			} else {
				out.println("<script>");
				out.println("alert('이력서 삭제를 실패 하였습니다.')");
				out.println("history.back()");
				out.println("</script>");
			}
			
		}
		
		//이력서 수정
		@GetMapping("/profile/update")
		public String profile_modify(@RequestParam("no") int no,Model model) {
			
			ProfileDTO content = this.profileMapper.profileinfo(no);
			List<CareerDTO> carMody = this.profileMapper.careerList(no);
			List<LicenseDTO> licenMody = this.profileMapper.licenseList(no);
			List<EduDTO> eduMody = this.profileMapper.eduList(no);
			
			model.addAttribute("Modify", content);
			model.addAttribute("carMody", carMody);
			model.addAttribute("licenMody", licenMody);
			model.addAttribute("eduMody", eduMody);
			
			return "profile/profile_modify";
		}
		
		//이력서 수정 업데이트
		@PostMapping("/profile/updateOk")
		public void profile_modify_ok(@RequestParam("profile_key") int pro_key,@ModelAttribute(value = "CodeListDTO") CodeListDTO codelistDTO,
		        ProfileDTO profileDto,HttpServletResponse response, HttpSession session,
		        @RequestParam("profile_image_add")MultipartFile profile_image,
		        @RequestParam("profile_ppt_add")MultipartFile profile_ppt) throws IOException {
		    
			ProfileDTO original_profile_dto = this.profileMapper.profileinfo(pro_key);
			
			profileDto.setProfile_image_name(original_profile_dto.getProfile_image_name());
			profileDto.setProfile_image(original_profile_dto.getProfile_image());
			//새로운 파일
			if(profile_image.getOriginalFilename() != null) {
				
				if(profile_image != null && !profile_image.isEmpty()) {

					String userDir = System.getProperty("user.dir");
				      String imageUploadDir = userDir+"\\src\\main\\resources\\static\\image\\profile";
				      System.out.println(imageUploadDir+"userDir");
				      
					//String imageUploadDir ="C:\\Users\\BSH\\git\\WorkWave\\Team2Project\\src\\main\\resources\\static\\image\\profile";
					
					uploadFileService.deleteFile(profileDto.getProfile_image(), imageUploadDir);
					
					
					String imageName = uploadFileService.upload(profile_image, imageUploadDir);
					profileDto.setProfile_image_name(profile_image.getOriginalFilename());
					profileDto.setProfile_image(imageName);
				}
			}
			
			 // 프로필 PPT 파일 수정
			
			profileDto.setProfile_ppt_name(original_profile_dto.getProfile_ppt_name());
			profileDto.setProfile_ppt(original_profile_dto.getProfile_ppt());
			
			if(profile_ppt.getOriginalFilename() != null) {	
				if (profile_ppt != null && !profile_ppt.isEmpty()) {
					

					String userDir = System.getProperty("user.dir");
				      String pptUploadDir = userDir+"\\src\\main\\resources\\static\\ppt\\profile";
				      System.out.println(pptUploadDir+"userDir");
				      
					
			        //String pptUploadDir = "C:\\Users\\BSH\\git\\WorkWave\\Team2Project\\src\\main\\resources\\static\\ppt\\profile";
		
			        uploadFileService.deleteFile(original_profile_dto.getProfile_ppt(), pptUploadDir);
		
			        String pptName = uploadFileService.uploadOriName(profile_ppt, pptUploadDir);
			        profileDto.setProfile_ppt_name(profile_ppt.getOriginalFilename());
			        profileDto.setProfile_ppt(pptName);
			    }
			}
			try {
					//경력 수정
					int size= codelistDTO.getCrDtoList().size(); 
					for(int i= 0; i < size; i++) {
						CareerDTO careerDto = codelistDTO.getCrDtoList().get(i); 
					  this.profileMapper.updateCareer(careerDto);
					}
					
					//학력 수정
					int esize = codelistDTO.getEDtoList().size();
					System.out.println("esize>>>"+codelistDTO.getEDtoList());
					System.out.println("esize>>>"+esize);
					
					for(int i=0; i < esize; i++) {
						EduDTO eduDto = codelistDTO.getEDtoList().get(i);
						this.profileMapper.updateEdu(eduDto);
					}
				
			
					// 자격증 수정
					int lsize= codelistDTO.getLDtoList().size();
					  for(int i=0; i < lsize; i++) {
					  
						  LicenseDTO ldto = codelistDTO.getLDtoList().get(i);
						  
						  
						  this.profileMapper.updateLicense(ldto); 
					  
					  }
					  
					  profileDto.setProfile_key(pro_key);
					  
					  this.profileMapper.updateProfile(profileDto);
					  
					  
					  response.setContentType("text/html; charset=UTF-8");
				       PrintWriter out = response.getWriter();
				        
				           out.println("<script>");
				           out.println("alert('이력서를 수정했습니다.')");
				           out.println("location.href='/CU/profile/content?no="+profileDto.getProfile_key()+"'");
				           out.println("</script>");
				        
					} catch (Exception e) {
						e.printStackTrace();
						response.setContentType("text/html; charset=UTF-8");
						 PrintWriter out = response.getWriter();
						out.println("<script>");
			            out.println("alert('이력서 수정을 실패했습니다.')");
			            out.println("history.back()");
			            out.println("</script>");
					
					}
				}
   
		@GetMapping("/chat/insert")
		public String write(HttpSession session, Model model) {
			
			UserDTO userInfo = (UserDTO)session.getAttribute("uDTO");
			// 이력서의 이미지 사진
			List<ProfileDTO> profileList = this.profileMapper.profileList(userInfo.getUser_key());
			// 내가 작성한 게시물 갯수
			int chatCnt = this.chatMapper.chatCnt(userInfo.getUser_key());
			// 내가 작성한 게시물의 댓글 갯수
			int replyCnt = this.chatMapper.replyCnt(userInfo.getUser_key());
			
			model.addAttribute("chatCnt", chatCnt)
				 .addAttribute("replyCnt", replyCnt)
				 .addAttribute("profileList", profileList);
			
			return "chat/write";
		}
		
		@PostMapping("/chat/insertOk")
		public void writeok(ChatDTO dto, HttpServletResponse response) throws IOException {
			
			response.setContentType("text/html; charset=UTF-8");
			 
			PrintWriter out = response.getWriter();
			
			int result = this.chatMapper.add(dto);
			
			if(result > 0) {
				out.println("<script>");
				out.println("alert('게시글 추가 성공!!!')");
				out.println("location.href='/A/chat'");
				out.println("</script>");
			}else {
				out.println("<script>");
				out.println("alert('게시글 추가 실패')");
				out.println("history.back()");
				out.println("</script>");
			}

		}
		
		@GetMapping("/chat/delete")
		public void delete(@RequestParam("no")int chat_key, HttpServletResponse response) throws IOException {
			
			response.setContentType("text/html; charset=UTF-8");
			
			PrintWriter out = response.getWriter();
			
			int result = this.chatMapper.del(chat_key);
			
			if(result > 0) {
				
				this.chatMapper.seq(chat_key);
				
				out.println("<script>");
				out.println("alert('게시글 삭제 성공')");
				out.println("location.href='/A/chat'");
				out.println("</script>");
			}else {
				out.println("<script>");
				out.println("alert('게시글 삭제 실패')");
				out.println("history.back()");
				out.println("</script>");
			}
		
		}
		
		@GetMapping("/chat/update")
		public String modify(@RequestParam("no")int no, Model model, HttpSession session) {
			
			ChatDTO content = this.chatMapper.getContent(no);
			
			UserDTO userInfo = (UserDTO)session.getAttribute("uDTO");
			// 이력서의 이미지 사진
			List<ProfileDTO> profileList = this.profileMapper.profileList(userInfo.getUser_key());
			// 내가 작성한 게시물 갯수
			int chatCnt = this.chatMapper.chatCnt(userInfo.getUser_key());
			// 내가 작성한 게시물의 댓글 갯수
			int replyCnt = this.chatMapper.replyCnt(userInfo.getUser_key());
			
			model.addAttribute("chatCnt", chatCnt)
				 .addAttribute("replyCnt", replyCnt)
				 .addAttribute("profileList", profileList);
			
			model.addAttribute("modify", content);
			
			return "chat/modify";
		}
		
		@PostMapping("/chat/updateOk")
		public void modifyok(@RequestParam("chat_key") int no, ChatDTO dto, HttpServletResponse response) throws IOException {
			
			response.setContentType("text/html; charset=UTF-8");
			
			PrintWriter out = response.getWriter();
			
			int result = this.chatMapper.modify(dto);
			
			if(result > 0) {
				out.println("<script>");
				out.println("alert('게시글 수정 성공')");
				out.println("location.href='/U/chat/content?no="+dto.getChat_key()+"'");
				out.println("</script>");
			}else {
				out.println("<script>");
				out.println("alert('게시글 수정 실패')");
				out.println("history.back()");
				out.println("</script>");
			}
		}
		
		// 일반회원 비밀번호 변경 페이지로 이동하는 매핑
		@GetMapping("/update/pwd/companyPwdCheck")
		public String companyPwdUpdate() {

			return "user/pwdUpdate";

		}
		
		@PostMapping("/update/pwd")
		public void companyPwdUpdateCheck(@RequestParam("ori_user_pwd") String ori_user_pwd,
										  @RequestParam("new_user_pwd") String new_user_pwd,
										  HttpSession session,
										  HttpServletResponse response) throws IOException {
			
			UserDTO userDto = (UserDTO) session.getAttribute("uDTO");
			
			response.setContentType("text/html; charset=UTF-8");

			PrintWriter out = response.getWriter();
			
			if(passwordEncoder.matches(ori_user_pwd, userDto.getUser_pwd())) {
				//입력된 비밀번호와 기존 비밀번호가 같을때
				if(passwordEncoder.matches(new_user_pwd, userDto.getUser_pwd())) {
					// 기존 비밀번호와 새 비밀번호가 같다면 다시 비밀번호 변경 창으로
					out.println("<script>");
					out.println("alert('기존 비밀번호와 새 비밀번호가 같습니다.')");
					out.println("history.back()");
					out.println("</script>");
				}else {
					// 기존 비밀번호와 새 비밀번호가 다르다면 새 비밀번호로 다시 저장
					String new_company_pwd_encorded = this.passwordEncoder.encode(new_user_pwd);
					
					session.removeAttribute("uDTO");
					
					this.userMapper.userUpdatePwd(userDto.getUser_id(), new_company_pwd_encorded);
					
					UserDTO updatedto = this.userMapper.getUserById(userDto.getUser_id());
					
					session.setAttribute("uDTO", updatedto);
					
					out.println("<script>");
					out.println("alert('비밀번호 수정에 성공했습니다.')");
					out.println("location.href='/U/info'");
					out.println("</script>");
				}
			}else {
				//입력된 비밀번호와 기존 비밀번호가 다를때
				out.println("<script>");
				out.println("alert('입력하신 비밀번호와 기존 비밀번호가 다릅니다.')");
				out.println("history.back()");
				out.println("</script>");
			}
			
		}
		
		
		
		
		
		
		
		
		
}