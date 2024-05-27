package com.Team2Project.WorkWave.service;

import com.Team2Project.WorkWave.model.EmailToken;
import com.Team2Project.WorkWave.model.TokenMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class TokenService {

    @Autowired
    private TokenMapper tokenMapper;

    public void saveToken(String email, String token, String verificationCode) {
        EmailToken emailToken = new EmailToken(email, token, verificationCode); 
        emailToken.setId(UUID.randomUUID().toString());
        emailToken.setEmail(email);
        emailToken.setToken(token);
        emailToken.setVerificationCode(verificationCode);
        tokenMapper.saveToken(emailToken);
    }

    public String getVeriCodeByEmail(String email) {
        return tokenMapper.getVeriCodeByEmail(email);
    }

    public void removeTokenByEmail(String email) {
        tokenMapper.removeTokenByEmail(email);
    }

    // 기타 메서드들
}
