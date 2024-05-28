package com.Team2Project.WorkWave.service;

import java.io.File;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UploadFileService {
	
public String upload(MultipartFile file, String uploadDir) {
		
	 String fileOriName = file.getOriginalFilename();
	 
     String fileExtension = fileOriName.substring(fileOriName.lastIndexOf(".")); //파일 확장자
     
     UUID uuid = UUID.randomUUID(); //고유한 이름 생성
     
     String uniqueName = uuid.toString().replaceAll("-", "") + fileExtension;
     
     File saveFile = new File(uploadDir + File.separator + uniqueName); //separator운영체제에 맞는 파일 구분자

     try {
         if (!saveFile.exists()) {
             saveFile.mkdirs(); //디렉토리 생성
         }
         file.transferTo(saveFile); // 저장할 경로에 파일 전송
         System.out.println("[UploadFileService] FILE UPLOAD SUCCESS!!");
         return uniqueName; // 파일 고유한 이름 반환
     } catch (Exception e) {
         e.printStackTrace();
         System.out.println("[UploadFileService] FILE UPLOAD FAIL!!");
         return null;
     }
 }

}
