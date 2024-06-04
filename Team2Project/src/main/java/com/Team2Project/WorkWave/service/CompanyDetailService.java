package com.Team2Project.WorkWave.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.Team2Project.WorkWave.model.CompanyMapper;
import com.Team2Project.WorkWave.model.CompanyUserDetails;
import com.Team2Project.WorkWave.model.LoginDTO;

@Service
public class CompanyDetailService implements UserDetailsService {
	
	@Autowired
	private CompanyMapper companyMapper;

	@Override
	public UserDetails loadUserByUsername(String company_id) throws UsernameNotFoundException {
		
		System.out.println("확인용");
		
		LoginDTO login_dto = companyMapper.findCompanyIdbyId(company_id);
		
		System.out.println(login_dto);
		
		if (login_dto == null) {
            throw new UsernameNotFoundException("오류(유저 없음)");
		}
		
		return new CompanyUserDetails(
				login_dto.getCompany_id(),
				login_dto.getCompany_pwd(), 
				login_dto.getRole());
		
	}

}
