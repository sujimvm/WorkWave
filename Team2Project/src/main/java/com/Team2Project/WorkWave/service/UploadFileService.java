package com.Team2Project.WorkWave.service;

import java.io.File;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UploadFileService {
	
public String upload(MultipartFile file, String uploadDir) {
		
	 String fileOriName = file.getOriginalFilename();
	 
     String fileExtension = fileOriName.substring(fileOriName.lastIndexOf(".")); //확장자
     
     UUID uuid = UUID.randomUUID(); 
     
     String uniqueName = uuid.toString().replaceAll("-", "") + fileExtension;
     
     File saveFile = new File(uploadDir + File.separator + uniqueName); 

     try {
         if (!saveFile.exists()) {
             saveFile.mkdirs(); 
         }
         file.transferTo(saveFile); 
         System.out.println("[UploadFileService] FILE UPLOAD SUCCESS!!");
         return uniqueName; 
     } catch (Exception e) {
         e.printStackTrace();
         System.out.println("[UploadFileService] FILE UPLOAD FAIL!!");
         return null;
     }
   }

}
