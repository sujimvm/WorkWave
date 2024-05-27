package com.Team2Project.WorkWave.controller;

import com.Team2Project.WorkWave.service.EmailService;
import com.Team2Project.WorkWave.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
public class VeriController {

    private static final Logger logger = LoggerFactory.getLogger(VeriController.class);

    @Autowired
    private EmailService emailService;

    @Autowired
    private TokenService tokenService;

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
                response.put("message", "인증 성공");
            } else {
                response.put("status", "fail");
                response.put("message", "인증 실패: 코드가 일치하지 않습니다.");
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
}
