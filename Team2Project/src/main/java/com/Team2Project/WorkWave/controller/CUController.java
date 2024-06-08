package com.Team2Project.WorkWave.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.Charset;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.Team2Project.WorkWave.model.CareerDTO;
import com.Team2Project.WorkWave.model.EduDTO;
import com.Team2Project.WorkWave.model.LicenseDTO;
import com.Team2Project.WorkWave.model.ProfileDTO;
import com.Team2Project.WorkWave.model.ProfileMapper;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;

@Controller("/CU")
public class CUController {

	@Autowired
	private ProfileMapper profileMapper;

	private final String pptUploadDir = "C:\\Users\\BSH\\git\\WorkWave\\Team2Project\\src\\main\\resources\\static\\ppt\\profile";

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

	// 다운
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

}
