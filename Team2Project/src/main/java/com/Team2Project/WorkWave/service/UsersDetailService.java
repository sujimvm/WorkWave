package com.Team2Project.WorkWave.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.Team2Project.WorkWave.model.UserLoginDTO;
import com.Team2Project.WorkWave.model.UserMapper;
import com.Team2Project.WorkWave.model.UsersUserDetails;

@Service
public class UsersDetailService implements UserDetailsService{
	
	@Autowired
	private UserMapper userMapper;

	@Override
	public UserDetails loadUserByUsername(String user_id) throws UsernameNotFoundException {
		
		System.out.println(user_id);
		
		UserLoginDTO login_dto = this.userMapper.findUserIdById(user_id);
		
		if(login_dto == null) {
			throw new UsernameNotFoundException("아이디 없음");
		}
		
		return new UsersUserDetails(
				login_dto.getUser_id(), 
				login_dto.getUser_pwd(),
				login_dto.getRole());
	}

	
	
}
