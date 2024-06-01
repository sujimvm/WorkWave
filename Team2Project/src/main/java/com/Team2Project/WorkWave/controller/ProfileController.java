package com.Team2Project.WorkWave.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
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
import com.Team2Project.WorkWave.model.EduDTO;
import com.Team2Project.WorkWave.model.LicenseDTO;
import com.Team2Project.WorkWave.model.ProfileDTO;
import com.Team2Project.WorkWave.model.ProfileMapper;
import com.Team2Project.WorkWave.model.UserDTO;
import com.Team2Project.WorkWave.service.UploadFileService;

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
	private ProfileMapper mapper;
	
	private final String pptUploadDir = "C:\\Users\\BSH\\git\\WorkWave\\Team2Project\\src\\main\\resources\\static\\ppt\\profile";

    @GetMapping("/download/ppt/{fileName:.+}")
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
	@PostMapping("profile_insert")
	public void profileInsert(ProfileDTO dto, HttpServletResponse response, HttpSession session, 
			@ModelAttribute(value = "CodeListDTO") CodeListDTO codelistDTO,
			@RequestParam("profile_ppt_input") MultipartFile ppt_file,
			@RequestParam("profile_image_input")MultipartFile image_file,EduDTO edu
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
		
		
		UserDTO userdto = (UserDTO) session.getAttribute("user_login");

		if (userdto == null) {
			response.sendRedirect("/user.go"); 
			return; 
		}
		dto.setUser_key(userdto.getUser_key());

		// 넘어온 키 값의 이력서가 있는지 확인
		if (this.mapper.profileCkeck(dto.getUser_key()) > 0) {
			dto.setProfile_default("N");
		} else {
			dto.setProfile_default("Y");
		}

	
		int check = this.mapper.profileInsert(dto);
		
		// 유저키의 프로필키(max) 
		int nowInsertProfileKey = this.mapper.nowInsertProfileKey(dto.getUser_key());
		
		/*
		 * //학력 데이터 저장 EduDTO eduDto = new EduDTO();
		 * 
		 * edu.setProfile_key(nowInsertProfileKey);
		 * 
		 * this.mapper.EduInsert(edu);
		 */
		 
		
	
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
		  
		  this.mapper.CareerInsert(crdto);
		  
		  }
		  
		  //자격증 데이터 저장 
		LicenseDTO ldtoArr = codelistDTO.getLDtoList().get(0);
		  
		  for(int i=0; i < ldtoArr.getLicense_name().split(",").length; i++) {
		  
		  LicenseDTO ldto = new LicenseDTO();
		  
		  ldto.setProfile_key(nowInsertProfileKey);
		  ldto.setLicense_name(ldtoArr.getLicense_name().split(",")[i]);
		  ldto.setLicense_barhang(ldtoArr.getLicense_barhang().split(",")[i]);
		  ldto.setLicense_date(ldtoArr.getLicense_date().split(",")[i]);
		  
		  this.mapper.LicenseInsert(ldto); }
		  
		 
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
	
	// 이력서 임시저장
	@PostMapping("profile_Temp")
	public void profileTemp(ProfileDTO dto,HttpServletResponse response,HttpSession session,
			@ModelAttribute(value = "CodeListDTO") CodeListDTO codelistDTO,
			@RequestParam("profile_ppt_input") MultipartFile ppt_file,
			@RequestParam("profile_image_input") MultipartFile image_file) throws IOException {
		
		String imageUploadDir = "C:\\Users\\BSH\\git\\WorkWave\\Team2Project\\src\\main\\resources\\static\\image\\profile";
		
		String pptUploadDir = "C:\\Users\\BSH\\git\\WorkWave\\Team2Project\\src\\main\\resources\\static\\ppt\\profile";
		
		dto.setProfile_image("");
		dto.setProfile_image_name("");
		
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
				String pptName = uploadFileService.upload(ppt_file, pptUploadDir);
				dto.setProfile_ppt(pptName);
				dto.setProfile_ppt_name(ppt_file.getOriginalFilename());
			}
		}

		UserDTO userdto = (UserDTO) session.getAttribute("user_login");

		dto.setUser_key(userdto.getUser_key());

		// 넘어온 키 값의 이력서가 있는지 확인
		if (this.mapper.profileCkeck(dto.getUser_key()) > 0) {
			dto.setProfile_default("N");
		} else {
			dto.setProfile_default("Y");
		}

	
		int check = this.mapper.profileTempInsert(dto);
		
		// 유저키의 프로필키(max) 
		int nowInsertProfileKey = this.mapper.nowInsertTempProfileKey(dto.getUser_key());
		
		
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
		  
		  this.mapper.CareerInsert(crdto);
		  
		  }
		  
		  //자격증 데이터 저장 
		LicenseDTO ldtoArr = codelistDTO.getLDtoList().get(0);
		  
		  for(int i=0; i < ldtoArr.getLicense_name().split(",").length; i++) {
		  
		  LicenseDTO ldto = new LicenseDTO();
		  
		  ldto.setProfile_key(nowInsertProfileKey);
		  ldto.setLicense_name(ldtoArr.getLicense_name().split(",")[i]);
		  ldto.setLicense_barhang(ldtoArr.getLicense_barhang().split(",")[i]);
		  ldto.setLicense_date(ldtoArr.getLicense_date().split(",")[i]);
		  
		  this.mapper.LicenseInsert(ldto); }
		  
		 
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out = response.getWriter();

		if (check > 0) {
			out.println("<script>");
			out.println("alert('이력서 임시저장을 완료하였습니다.')");
			out.println("location.href='profile_list'");
			out.println("</script>");
		} else {
			out.println("<script>");
			out.println("alert('이력서 임시저장을 실패하였습니다.')");
			out.println("history.back()");
			out.println("</script>");
		}
		
	}

	// 이력서 작성 폼
	@GetMapping("/profile_writer")
	public String categorygrouptest(Model model) {

		List<CodeDTO> category = this.mapper.category();

		model.addAttribute("categories", category);

		return "profile/profile";
	}

	// 대분류 카테고리
	@PostMapping("com_board_group")
	@ResponseBody
	public List<CodeDTO> categorysub(@RequestParam("no") String no) {

		List<CodeDTO> categorysub = this.mapper.categorysub(no);

		return categorysub;
	}

	// 중분류 카테고리
	@PostMapping("com_board_sub")
	@ResponseBody
	public List<CodeDTO> categorystep(@RequestParam("no") String no) {

		List<CodeDTO> categorystep = this.mapper.categorystep(no);

		return categorystep;

	}

	// 학교구분과 학교 이름 검색 시 해당 학교 리스트 불러오기
	@PostMapping("search_school_by_name")
	@ResponseBody
	public List<CodeDTO> schoolName(CodeDTO dto) {

		List<CodeDTO> schoolName = this.mapper.schoolname(dto);

		return schoolName;

	}

	// 학교구분과 전공명 검색 시 해당 전공 데이터 불러오기
	@PostMapping("get_department")
	@ResponseBody
	public List<CodeDTO> departmentName(CodeDTO dto) {

		List<CodeDTO> departmentName = this.mapper.department(dto);

		return departmentName;

	}

	// 해당 자격증 리스트 불러오기
	@PostMapping("search_certifications")
	@ResponseBody
	public List<LicenseDTO> searchlicense(@RequestParam("license_name") String license_name) {

		List<LicenseDTO> licenseList = this.mapper.license(license_name);

		return licenseList;

	}

	// 이력서 리스트로 이동
	@GetMapping("profile_list")
	public String profileList(Model model, HttpSession session) {

		UserDTO user = (UserDTO) session.getAttribute("user_login");

		int userKey = user.getUser_key();

		List<ProfileDTO> profileList = this.mapper.profileList(userKey);
		
		List<ProfileDTO> profileTemList = this.mapper.profileTempList(userKey);

		model.addAttribute("ProfileList", profileList);
		model.addAttribute("ProfileTempList", profileTemList);

		return "profile/profile_List";
	}

	// 해당 프로필 키 이력서 상세보기
	@GetMapping("profile_content")
	public String profileContent(@RequestParam("no") int no, Model model) {

		ProfileDTO dto = this.mapper.profileinfo(no);

		List<EduDTO> eduList = this.mapper.eduList(no);
		List<CareerDTO> careerList = this.mapper.careerList(no);
		List<LicenseDTO> licenseList = this.mapper.licenseList(no);

		model.addAttribute("Content", dto).addAttribute("EduList", eduList).addAttribute("CareerList", careerList)
				.addAttribute("License", licenseList);

		return "profile/profile_content";

	}

	// 기본 이력서로 변경
	@GetMapping("profile_default")
	public void profileDefault(@RequestParam("no") int defaultKey, HttpServletResponse response) throws IOException {

		// 기본 이력서로 변경
		this.mapper.defaultChangeN();
		// 대표 이력서로 변경
		int check = this.mapper.defaultChangeY(defaultKey);

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
	@GetMapping("profile_delect")
	public void profile_delect(@RequestParam("no") int pro_key,HttpServletResponse response) throws IOException {
		
		int check = this.mapper.profileDelect(pro_key);
		
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
	@GetMapping("profile_modify")
	public String profile_modify(@RequestParam("no") int no,Model model) {
		
		ProfileDTO content = this.mapper.profileinfo(no);
		List<CareerDTO> carMody = this.mapper.careerList(no);
		List<LicenseDTO> licenMody = this.mapper.licenseList(no);
		
		model.addAttribute("Modify", content);
		model.addAttribute("carMody", carMody);
		model.addAttribute("licenMody", licenMody);
		
		return "profile/profile_modify";
	}
	
	//이력서 수정 업데이트
	@PostMapping("profile_modify_ok")
	public void profile_modify_ok(@RequestParam("profile_key") int pro_key, ProfileDTO profileDto, CareerDTO careerDto, LicenseDTO licenDto,
	        HttpServletResponse response, HttpSession session) throws IOException {
	    
	    profileDto.setProfile_key(pro_key);
	    careerDto.setProfile_key(pro_key);
	    licenDto.setProfile_key(pro_key);
	    
	    int updateResult = 0;
	    
	    try {
	        updateResult = this.mapper.updateProfile(profileDto);
	        updateResult += this.mapper.updateCareer(careerDto);
	        updateResult += this.mapper.updateLicense(licenDto);
	        
	        response.setContentType("text/html; charset=UTF-8");
	        PrintWriter out = response.getWriter();
	        
	        if (updateResult > 0) {
	            out.println("<script>");
	            out.println("alert('이력서를 수정했습니다.')");
	            out.println("location.href='profile_list'");
	            out.println("</script>");
	        } else {
	            out.println("<script>");
	            out.println("alert('이력서 수정을 실패했습니다.')");
	            out.println("history.back()");
	            out.println("</script>");
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	    }
	}
	
	//중간 이력서 이어서 작성
	@GetMapping("profileTemp_add")
	public String profileTempAdd(@RequestParam("no") int no,Model model) {
		
		
		ProfileDTO profileTemp = this.mapper.profileTempinfo(no);
		List<CareerDTO> careerTemp = this.mapper.careerTempinfo(no);
		List<LicenseDTO> licenseTemp = this.mapper.licenseTempinfo(no);
	
		
		model.addAttribute("profileTemp", profileTemp);
		model.addAttribute("careerTemp", careerTemp);
		model.addAttribute("licenseTemp", licenseTemp);
		
		return "profile/profileTempAdd";
	}

	//중간 이력서 작성 저장
	@PostMapping("profile_tempAdd_ok")
	public void profileTempAddOk(@RequestParam("profile_key") int pro_key, ProfileDTO profileDto, CareerDTO careerDto, LicenseDTO licenDto,
	        HttpServletResponse response, HttpSession session) throws IOException {
		
		    profileDto.setProfile_key(pro_key);
		    careerDto.setProfile_key(pro_key);
		    licenDto.setProfile_key(pro_key);
		    
		    int updateResult = 0;
		    
		    try {
		        updateResult = this.mapper.updateProfileTemp(profileDto);
		        updateResult += this.mapper.updateCareerTemp(careerDto);
		        updateResult += this.mapper.updateLicenseTemp(licenDto);
		        
		        response.setContentType("text/html; charset=UTF-8");
		        PrintWriter out = response.getWriter();
		        
		        if (updateResult > 0) {
		            out.println("<script>");
		            out.println("alert('이력서를 추가작성을 저장했습니다.')");
		            out.println("location.href='profile_list'");
		            out.println("</script>");
		        } else {
		            out.println("<script>");
		            out.println("alert('이력서 추가 작성을 실패했습니다.')");
		            out.println("history.back()");
		            out.println("</script>");
		        }
		    } catch (IOException e) {
		        e.printStackTrace();
		        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		    }
		
	}
	
}
