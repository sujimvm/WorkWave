package com.Team2Project.WorkWave.service;

import java.util.Random;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.Team2Project.WorkWave.model.MessageDTO;
import com.Team2Project.WorkWave.model.MessageMapper;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;

@Service
@RequiredArgsConstructor
public class MessageService {
	
	@Autowired
	private MessageMapper messageMapper;
	
	@Value("${coolsms.apiKey}")
    private String apiKey;

    @Value("${coolsms.apiSecret}")
    private String apiSecret;

    @Value("${coolsms.senderNumber}")
    private String senderNumber;
    
    private DefaultMessageService messageService;
    
    
    
    @PostConstruct
    public void init(){
        messageService = NurigoApp.INSTANCE.initialize(
                apiKey,
                apiSecret,
                "https://api.coolsms.co.kr"
        );
    }
    
	/*
	 * private String createRandomNumber() { Random rand = new Random(); String
	 * randomNum = ""; for (int i = 0; i < 4; i++) { String random =
	 * Integer.toString(rand.nextInt(10)); randomNum += random; }
	 * 
	 * return randomNum; }
	 */
    
    public SingleMessageSentResponse sendOne(String to, String verificationCode) {
        Message message = new Message();
        
		// 발신번호 및 수신번호는 반드시 01012345678 형태로 입력되어야 합니다.
        message.setFrom(senderNumber);
        message.setTo(to);
        message.setText("[WorkWave] 아래의 인증번호를 입력해주세요\n" + verificationCode);
        

        SingleMessageSentResponse response = this.messageService.sendOne(new SingleMessageSendingRequest(message));
        
        return response;
    }
    
    public void saveSMS(String phone, String verify_code) {
    	
    	MessageDTO messageDTO = new MessageDTO(phone, verify_code);
    	
    	messageDTO.setId(UUID.randomUUID().toString());
    	messageDTO.setPhone(phone);
    	messageDTO.setVerify_code(verify_code);
    	
    	messageMapper.saveSMS(messageDTO);
    	
    }
    
    public String getVerifyCode(String phone) {
    	
    	return messageMapper.getVerifyCode(phone);
    	
    }
    
    public void removeVerifyCode(String phone) {
    	messageMapper.removeVerifyCode(phone);
    }

}
