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
import com.Team2Project.WorkWave.model.EduDTO;
import com.Team2Project.WorkWave.model.LicenseDTO;
import com.Team2Project.WorkWave.model.ProfileDTO;
import com.Team2Project.WorkWave.model.ProfileMapper;
import com.Team2Project.WorkWave.model.UserDTO;
import com.Team2Project.WorkWave.service.UploadFileService;

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
	public void profileInsert(ProfileDTO dto, HttpServletResponse response, HttpSession session) throws IOException {

		// 파일을 저장할 디렉토리 설정
	    String imageUploadDir = "C:\\Users\\BSH\\git\\WorkWave\\Team2Project\\src\\main\\resources\\static\\image";
;
	    String pptUploadDir = "C:\\Users\\BSH\\git\\WorkWave\\Team2Project\\src\\main\\resources\\static\\ppt";
	    

	    // 이미지 파일 업로드
	    MultipartFile profileImage = dto.getProfile_image_name();
	    
	    if (profileImage != null && !profileImage.isEmpty()) {
	        String imageName = uploadFileService.upload(profileImage, imageUploadDir);
	        dto.setProfile_image(imageName);
	    }

	    // PPT 파일 업로드
	    MultipartFile profilePpt = dto.getProfile_ppt_name();
	    
	    if (profilePpt != null && !profilePpt.isEmpty()) {
	        String pptName = uploadFileService.upload(profilePpt, pptUploadDir);
	        dto.setProfile_ppt(pptName);
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

	/*
	 * // 데이터 넘어오는 지 test
	 * 
	 * @PostMapping("dateReqTest") public String dateReqTest(Model
	 * model,@ModelAttribute(value = "CodeListDTO") CodeListDTO li) {
	 * System.out.println(li);
	 * 
	 * 
	 * List<CodeDTO> list = request.getParameterValues(dto);
	 * 
	 * for (int i = 0; i < list.size(); i++) { CodeDTO dto = new CodeDTO();
	 * dto.setCode(list.get(i).getCode());
	 * 
	 * System.out.println(dto.getCode()); }
	 * 
	 * return "index"; }
	 */
	
	// 이력서 리스트로 이동
	@GetMapping("profile_list")
	public String profileList(Model model,HttpSession session) {
		
		UserDTO user = (UserDTO)session.getAttribute("user_login");
		
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
	
	//기본 이력서로 변경
	@GetMapping("profile_default")
	public void profileDefault(@RequestParam("no") int defaultKey,HttpServletResponse response) throws IOException {
		
		// 대표 이력서로 변경
		int check = this.mapper.defaultChangeY(defaultKey);
		
		// 기본 이력서로 변경
		this.mapper.defaultChangeN();
		
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

}
