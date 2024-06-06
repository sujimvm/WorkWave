package com.Team2Project.WorkWave.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.Team2Project.WorkWave.model.CompanyMapper;
import com.Team2Project.WorkWave.model.CompanyUserDetails;
import com.Team2Project.WorkWave.model.UserLoginDTO;
import com.Team2Project.WorkWave.model.UserMapper;
import com.Team2Project.WorkWave.model.UsersUserDetails;
import com.Team2Project.WorkWave.model.CompanyLoginDTO;

@Service
public class CompanyDetailService implements UserDetailsService {
	
	@Autowired
	private CompanyMapper companyMapper;
	
	@Autowired
	private UserMapper userMapper;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		CompanyLoginDTO login_dto = this.companyMapper.findCompanyIdbyId(username);
		
		if (login_dto == null) {
			UserLoginDTO user_dto = this.userMapper.findUserIdById(username);
			if(user_dto == null) {
				throw new UsernameNotFoundException("아이디 없어유");
			}
			
			return new UsersUserDetails(
					user_dto.getUser_id(),
					user_dto.getUser_pwd(),
					user_dto.getRole());
		}
		
		return new CompanyUserDetails(
				login_dto.getCompany_id(),
				login_dto.getCompany_pwd(), 
				login_dto.getRole());
		
	}

}
