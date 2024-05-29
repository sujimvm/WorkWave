package com.Team2Project.WorkWave.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.http.HttpResponse;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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

import jakarta.servlet.http.HttpServletRequest;
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

	// 이력서 추가 메서드
	@PostMapping("profile_insert")
	public void profileInsert(ProfileDTO dto, HttpServletResponse response, HttpSession session, 
			@ModelAttribute(value = "CodeListDTO") CodeListDTO codelistDTO,
			@RequestParam("profile_ppt_input") MultipartFile ppt_file,
			@RequestParam("profile_image_input") MultipartFile image_file) throws IOException {

	
		// 파일을 저장할 디렉토리 설정
		String imageUploadDir = "C:\\Users\\BSH\\git\\WorkWave\\Team2Project\\src\\main\\resources\\static\\image\\profile";
		
		String pptUploadDir = "C:\\Users\\BSH\\git\\WorkWave\\Team2Project\\src\\main\\resources\\static\\ppt\\profile";

		// 이미지 파일 업로드
		if (image_file.getOriginalFilename() != null) {
			if (image_file != null && !image_file.isEmpty()) {
				String imageName = uploadFileService.upload(image_file, imageUploadDir);
				dto.setProfile_image(imageName);
				dto.setProfile_image_name(image_file.getOriginalFilename());
			}
		}

		dto.setProfile_ppt("");
		dto.setProfile_ppt_name("");
		
		// PPT 파일 업로드
		if (image_file.getOriginalFilename() != null) {
			if (ppt_file != null && !ppt_file.isEmpty()) {
				String pptName = uploadFileService.upload(ppt_file, pptUploadDir);
				dto.setProfile_ppt(pptName);
				dto.setProfile_ppt_name(ppt_file.getOriginalFilename());
			}
		}

		// 세션에서 로그인한 사용자 정보 가져오기
		UserDTO userdto = (UserDTO) session.getAttribute("user_login");

		if (userdto == null) {
			response.sendRedirect("/user.go"); // 로그인 페이지로 리다이렉트
			return; // 메서드 종료
		}
		dto.setUser_key(userdto.getUser_key());

		// 넘어온 키 값의 이력서가 있는지 확인
		if (this.mapper.profileCkeck(dto.getUser_key()) > 0) {
			dto.setProfile_default("N");
		} else {
			dto.setProfile_default("Y");
		}

		// 프로필 정보 삽입
		int check = this.mapper.profileInsert(dto);
		
		// 유저키의 프로필키(max) 가져오기
		int nowInsertProfileKey = this.mapper.nowInsertProfileKey(dto.getUser_key());
		
		/*
		// 입력 받은 리스트 배열로 가져오기
		EduDTO edtoArr = codelistDTO.getEDtoList().get(0);
		for (int i = 0; i < edtoArr.getEdu_kind().split(",").length; i++) {
			
			EduDTO edto = new EduDTO();
			
			edto.setProfile_key(nowInsertProfileKey);
			edto.setEdu_kind(edtoArr.getEdu_kind().split(",")[i]);
			edto.setEdu_name(edtoArr.getEdu_name().split(",")[i]);
			edto.setEdu_start_date(edtoArr.getEdu_start_date().split(",")[i]);
			edto.setEdu_end_date(edtoArr.getEdu_end_date().split(",")[i]);
			edto.setEdu_major(edtoArr.getEdu_major().split(",")[i]);
			edto.setEdu_degree(edtoArr.getEdu_degree().split(",")[i]);
			edto.setEdu_hakjum(edtoArr.getEdu_hakjum().split(",")[i]);
			edto.setEdu_status(edtoArr.getEdu_status().split(",")[i]);
			
			edto.setEdu_transfer(edtoArr.getEdu_transfer().split(",")[i]);
			
			edto.setEdu_nonmun(edtoArr.getEdu_nonmun().split(",")[i]);
			edto.setEdu_submajor(edtoArr.getEdu_submajor().split(",")[i]);
			
			this.mapper.EduInsert(edto);
		 }
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

		model.addAttribute("ProfileList", profileList);

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

	
//	// 데이터 넘어오는 지 test
//	@PostMapping("dateReqTest")
//	public String dateReqTest(Model model,@ModelAttribute(value = "CodeListDTO") CodeListDTO li) {
//		
//
//		 System.out.println(li+" li>>" );
//		
//		 for (int i = 0; i < li.getEDtoList().size(); i++) { 
//			 EduDTO dto = new EduDTO();
//			 dto.setEdu_key(li.getEDtoList().get(i).getEdu_key());
//			 System.out.println(dto.getEdu_key()+" key>>" );
//		 }
//		 
//		return "index";
//	}
	
}
