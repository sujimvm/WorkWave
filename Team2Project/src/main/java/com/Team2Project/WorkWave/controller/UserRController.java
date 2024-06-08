package com.Team2Project.WorkWave.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;

import com.Team2Project.WorkWave.model.*;
import com.Team2Project.WorkWave.service.*;


@Controller("/U")
public class UserRController {

   @Autowired private ChatMapper chatMapper;
   @Autowired private ComBoardMapper comBoardMapper;
   @Autowired private CompanyMapper companyMapper;
   @Autowired private NoticeMapper noticeMapper;
   @Autowired private ProfileMapper profileMapper;
   @Autowired private UserMapper userMapper;

   @Autowired private MessageMapper messageMapper; 
   @Autowired private TokenMapper tokenMapper; 
   @Autowired private PasswordEncoder passwordEncoder;

   @Autowired private EmailService emailService;
   @Autowired private MessageService messageService;
   @Autowired private TokenService tokenService;
   @Autowired private UploadFileService uploadFileService;
   @Autowired private UserService userService;
}