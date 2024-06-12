package com.Team2Project.WorkWave.controller;

import com.Team2Project.WorkWave.service.EmailService;
import com.Team2Project.WorkWave.service.MessageService;
import com.Team2Project.WorkWave.service.TokenService;

import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/veri")
public class VeriController {

    private static final Logger logger = LoggerFactory.getLogger(VeriController.class);

    @Autowired
    private EmailService emailService;

    @Autowired
    private TokenService tokenService;
    
    @Autowired
    private MessageService messageService;
    

    @PostMapping("/sendCode")
    @ResponseBody
    public Map<String, String> sendCode(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        Map<String, String> response = new HashMap<>();
        try {
            if (email == null || email.isEmpty()) {
                throw new IllegalArgumentException("Email is required");
            }
            String token = UUID.randomUUID().toString();
            String verificationCode = String.format("%06d", (int) (Math.random() * 1000000));
            tokenService.saveToken(email, token, verificationCode);
            emailService.sendVeriEmail(email, verificationCode);
            response.put("status", "success");
        } catch (Exception e) {
            logger.error("Error in sendCode: ", e);
            response.put("status", "fail");
            response.put("error", e.getMessage());
        }
        return response;
    }

    @PostMapping("/verifyCode")
    @ResponseBody
    public Map<String, String> verifyCode(@RequestBody Map<String, String> request) {
        Map<String, String> response = new HashMap<>();
        try {
            String email = request.get("email");
            String code = request.get("code");
            if (email == null || email.isEmpty() || code == null || code.isEmpty()) {
                throw new IllegalArgumentException("Email and code are required");
            }
            String storedCode = tokenService.getVeriCodeByEmail(email);
            if (storedCode != null && storedCode.equals(code)) {
                tokenService.removeTokenByEmail(email);
                response.put("status", "success");
                response.put("message", "인증번호 확인 성공하였습니다.");
            } else {
                response.put("status", "fail");
                response.put("message", "인증번호 확인 실패하였습니다.");
            }
        } catch (IllegalArgumentException e) {
            logger.error("Validation error in verifyCode: ", e);
            response.put("status", "fail");
            response.put("message", e.getMessage());
        } catch (Exception e) {
            logger.error("Error in verifyCode: ", e);
            response.put("status", "error");
            response.put("message", "서버 오류: " + e.getMessage());
        }
        return response;
    }
    
    
	@PostMapping("/sendSms")
    @ResponseBody
    public ResponseEntity<?> sendSms(@RequestParam("mgrPhone") String mgrPhone) {
        
    	String phone = mgrPhone;
                
        String verificationCode = String.format("%06d", (int) (Math.random() * 1000000));
        
        messageService.saveSMS(phone, verificationCode);
        SingleMessageSentResponse response = messageService.sendOne(phone, verificationCode);
        
        if (response.getStatusCode().equals("2000")) { // assuming 2000 means success
            return ResponseEntity.ok().body("{\"status\": 200, \"message\": \"인증번호가 전화번호로 전송되었습니다.\"}");
        } else {
            return ResponseEntity.status(500).body("{\"status\": 500, \"message\": \"인증번호 전송에 실패했습니다.\"}");
        }
    }
	
	@PostMapping("/reSendSms")
    @ResponseBody
    public ResponseEntity<?> reSendSms(@RequestParam("mgrPhone") String mgrPhone) {
		
    	String phone = mgrPhone;
    	
    	messageService.removeVerifyCode(phone);
                
        String verificationCode = String.format("%06d", (int) (Math.random() * 1000000));
        
        messageService.saveSMS(phone, verificationCode);
        SingleMessageSentResponse response = messageService.sendOne(phone, verificationCode);
        
        if (response.getStatusCode().equals("2000")) { // assuming 2000 means success
            return ResponseEntity.ok().body("{\"status\": 200, \"message\": \"인증번호가 전화번호로 전송되었습니다.\"}");
        } else {
            return ResponseEntity.status(500).body("{\"status\": 500, \"message\": \"인증번호 전송에 실패했습니다.\"}");
        }
    }
	
	@PostMapping("/smsCodeCheck")
	@ResponseBody
	public Map<String, String> smsCodeCheck(@RequestBody Map<String, String> reqMap) {
		
		Map<String, String> response = new HashMap<>();
		
		String phone = reqMap.get("phone");
		String verify_code = reqMap.get("code");
		
		if(phone == null || phone.isEmpty() || verify_code == null || verify_code.isEmpty()) {
			throw new IllegalArgumentException("Email and code are required");
		}
		
		String store_code = messageService.getVerifyCode(phone);
		
		if(verify_code != null && verify_code.equals(store_code)) {
			messageService.removeVerifyCode(phone);
			response.put("status", "success");
			response.put("message", "인증 성공");
		}else {
			response.put("status", "fail");
			response.put("message", "인증 실패: 코드가 일치하지 않습니다.");
		}
		
		return response;
	}
}
