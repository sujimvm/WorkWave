package com.Team2Project.WorkWave.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.Team2Project.WorkWave.model.UserDTO;
import com.Team2Project.WorkWave.model.UserMapper;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public UserDTO login(String userId) {
        return userMapper.doLogin(userId);
    }
    
    public void insertUser(UserDTO user) {
        userMapper.insertUser(user);
    }
    
    public boolean isUserIdAvailable(String userId) {
        return userMapper.getUserById(userId) == null;
    }
    
    public String findUserId(String userName, String userEmail) {
        return userMapper.findUserId(userName, userEmail);
    }
    
    public UserDTO findUserPassword(String userName, String userId, String userEmail) {
        return userMapper.findUserPassword(userName, userId, userEmail);
    }
    

    
}
	
	
	
