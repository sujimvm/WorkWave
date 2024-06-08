package com.Team2Project.WorkWave.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.Team2Project.WorkWave.model.CareerDTO;
import com.Team2Project.WorkWave.model.CodeDTO;
import com.Team2Project.WorkWave.model.CodeListDTO;
import com.Team2Project.WorkWave.model.CompanyDTO;
import com.Team2Project.WorkWave.model.EduDTO;
import com.Team2Project.WorkWave.model.LicenseDTO;
import com.Team2Project.WorkWave.model.ProfileDTO;
import com.Team2Project.WorkWave.model.ProfileMapper;
import com.Team2Project.WorkWave.model.UserDTO;
import com.Team2Project.WorkWave.service.UploadFileService;

import jakarta.mail.Session;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class ProfileController {

	@Autowired
	UploadFileService uploadFileService;

	@Autowired
	private ProfileMapper profileMapper;
	
	private final String pptUploadDir = "C:\\Users\\BSH\\git\\WorkWave\\Team2Project\\src\\main\\resources\\static\\ppt\\profile";

	//다운
    @GetMapping("/profile/download/ppt/{fileName:.+}")
    public void downloadPPT(@PathVariable("fileName") String fileName, HttpServletResponse response) throws Exception {
        File file = new File(pptUploadDir + File.separator + fileName);

        if (!file.exists()) {
            String errorMessage = "죄송합니다. 요청하신 파일을 찾을 수 없습니다.";
            ServletOutputStream outputStream = response.getOutputStream();
            outputStream.write(errorMessage.getBytes(Charset.forName("UTF-8")));
            outputStream.close();
            return;
        }

        String mimeType = "application/vnd.ms-powerpoint";
        response.setContentType(mimeType);
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

        response.setContentLength((int) file.length());

        try (BufferedInputStream inStream = new BufferedInputStream(new FileInputStream(file));
             ServletOutputStream outStream = response.getOutputStream()) {

            byte[] buffer = new byte[1024];
            int bytesRead = -1;

            while ((bytesRead = inStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, bytesRead);
            }
        }
    }


	// 이력서 추가 메서드
	@PostMapping("/profile/insertOk")
	public void profileInsert(ProfileDTO dto, HttpServletResponse response, HttpSession session, 
			@ModelAttribute(value = "CodeListDTO") CodeListDTO codelistDTO,
			@RequestParam("profile_ppt_input") MultipartFile ppt_file,
			@RequestParam("profile_image_input")MultipartFile image_file
			) throws IOException {
		
		String imageUploadDir = "C:\\Users\\BSH\\git\\WorkWave\\Team2Project\\src\\main\\resources\\static\\image\\profile";
		
		String pptUploadDir = "C:\\Users\\BSH\\git\\WorkWave\\Team2Project\\src\\main\\resources\\static\\ppt\\profile";
		

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
		
//		
//		UserDTO userdto = (UserDTO) session.getAttribute("user_login");
//
//		if (userdto == null) {
//			response.sendRedirect("/user.go"); 
//			return; 
//		}
//		dto.setUser_key(userdto.getUser_key());

		dto.setUser_key(9);
		
		// 넘어온 키 값의 이력서가 있는지 확인
		if (this.profileMapper.profileCkeck(dto.getUser_key()) > 0) {
			dto.setProfile_default("N");
		} else {
			dto.setProfile_default("Y");
		}

	
		int check = this.profileMapper.profileInsert(dto);
		
		// 유저키의 프로필키(max) 
		int nowInsertProfileKey = this.profileMapper.nowInsertProfileKey(dto.getUser_key());
		
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
		  crdto.setCareer_cont(crdtoArr.getCareer_cont().split(",")[i]);
		  crdto.setCareer_position(crdtoArr.getCareer_position().split(",")[i]);
		  crdto.setCareer_bye(crdtoArr.getCareer_bye().split(",")[i]);
		  crdto.setCareer_start_date(crdtoArr.getCareer_start_date().split(",")[i]);
		  crdto.setCareer_end_date(crdtoArr.getCareer_end_date().split(",")[i]);
		  
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
				out.println("location.href='profile_list'");
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
		
//		UserDTO user = (UserDTO) session.getAttribute("user_login");
//		
//		int userKey = user.getUser_key();
		

		int userKey =9;
		
		
		ProfileDTO userDto = this.profileMapper.profileinfo(userKey);

		List<CodeDTO> category = this.profileMapper.category();

		model.addAttribute("categories", category);
		
		model.addAttribute("userDto", userDto);

		return "profile/profile";
	}

	// 대분류 카테고리
	@PostMapping("/jobCodeGroup")
	@ResponseBody
	public List<CodeDTO> categorysub(@RequestParam("no") String no) {

		List<CodeDTO> categorysub = this.profileMapper.categorysub(no);

		return categorysub;
	}

	// 중분류 카테고리
	@PostMapping("/jobCodesub")
	@ResponseBody
	public List<CodeDTO> categorystep(@RequestParam("no") String no) {

		List<CodeDTO> categorystep = this.profileMapper.categorystep(no);

		return categorystep;

	}

	// 학교구분과 학교 이름 검색 시 해당 학교 리스트 불러오기
	@PostMapping("/searchSchoolByName")
	@ResponseBody
	public List<CodeDTO> schoolName(CodeDTO dto) {

		List<CodeDTO> schoolName = this.profileMapper.schoolname(dto);

		return schoolName;

	}

	// 학교구분과 전공명 검색 시 해당 전공 데이터 불러오기
	@PostMapping("/getDepartment")
	@ResponseBody
	public List<CodeDTO> departmentName(CodeDTO dto) {

		List<CodeDTO> departmentName = this.profileMapper.department(dto);

		return departmentName;

	}

	// 해당 자격증 리스트 불러오기
	@PostMapping("/searchCertifications")
	@ResponseBody
	public List<LicenseDTO> searchlicense(@RequestParam("license_name") String license_name) {

		List<LicenseDTO> licenseList = this.profileMapper.license(license_name);

		return licenseList;

	}

	// 이력서 리스트로 이동
	@GetMapping("")
	public String profileList(Model model, HttpSession session) {

//		UserDTO user = (UserDTO) session.getAttribute("user_login");
//		
//		int userKey = user.getUser_key();
		

		int userKey =9;
		
		List<ProfileDTO> profileList = this.profileMapper.profileList(userKey);
		
		model.addAttribute("ProfileList", profileList);
		

		return "profile/profile_List";
	}

	// 해당 프로필 키 이력서 상세보기
	@GetMapping("/profile/content")
	public String profileContent(@RequestParam("no") int no, Model model) {

		ProfileDTO dto = this.profileMapper.profileinfo(no);

		List<EduDTO> eduList = this.profileMapper.eduList(no);
		List<CareerDTO> careerList = this.profileMapper.careerList(no);
		List<LicenseDTO> licenseList = this.profileMapper.licenseList(no);

		model.addAttribute("Content", dto).addAttribute("EduList", eduList).addAttribute("CareerList", careerList)
				.addAttribute("License", licenseList);

		return "profile/profile_content";

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
			out.println("location.href='profile_list'");
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
			out.println("location.href='profile_list'");
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
				
				String imageUploadDir ="C:\\Users\\BSH\\git\\WorkWave\\Team2Project\\src\\main\\resources\\static\\image\\profile";
				
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
				
		        String pptUploadDir = "C:\\Users\\BSH\\git\\WorkWave\\Team2Project\\src\\main\\resources\\static\\ppt\\profile";
	
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
				int esize = codelistDTO.getLDtoList().size();
				for(int i=0; i < esize; i++) {
					EduDTO eduDto = codelistDTO.getEDtoList().get(i);
					this.profileMapper.updateEdu(eduDto);
				}
			
		
				// 자격증 수정
				int lsize= codelistDTO.getLDtoList().size();
				System.out.println("lsize>>"+lsize);
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
			           out.println("location.href='profile_list'");
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
		}

	    

