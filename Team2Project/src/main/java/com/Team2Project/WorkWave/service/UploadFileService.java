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



public String uploadOriName(MultipartFile file, String uploadDir) {
	
	 String fileOriName = file.getOriginalFilename();
	 
    
    File saveFile = new File(uploadDir + File.separator + fileOriName); 
    

    try {
        if (!saveFile.exists()) {
            saveFile.mkdirs(); 
        }
        file.transferTo(saveFile); 
        System.out.println("[UploadFileService] FILE UPLOAD SUCCESS!!");
        return fileOriName; 
    } catch (Exception e) {
        e.printStackTrace();
        System.out.println("[UploadFileService] FILE UPLOAD FAIL!!");
        return null;
    }
  }


// 회원 정보 수정이나 삭제시 저장되어있던 이미지파일 삭제
public void deleteFile(String fileName, String fileDir) {
	
	String filePath = fileDir + "\\" + fileName;
	
	File delete_file = new File(filePath);
	
	if(delete_file.exists()) {
		// 파일경로가 존재하면 삭제
		delete_file.delete();
		System.out.println("파일을 삭제하였습니다.");
	}else {
		System.out.println("파일이 존재하지 않습니다.");
	}
	
}

}
