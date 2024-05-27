package com.Team2Project.WorkWave.service;

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
}
	
	
	
