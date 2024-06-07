package com.Team2Project.WorkWave.model;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


public class CompanyUserDetails implements UserDetails {

	private static final long serialVersionUID = 1L;
	private final String company_id;
	private final String company_pwd;
	private final Collection<? extends GrantedAuthority> authorities;
	
	public CompanyUserDetails(String company_id, String company_pwd, String role) {
		this.company_id=company_id;
		this.company_pwd=company_pwd;
		this.authorities = Collections.singletonList(new SimpleGrantedAuthority(role));
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return authorities;
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return company_pwd;
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return company_id;
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}

	
}
