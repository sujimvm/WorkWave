package com.Team2Project.WorkWave.model;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


public class UsersUserDetails implements UserDetails{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String user_id;
	private String user_pwd;
	private Collection<? extends GrantedAuthority> authorities;
	
	public UsersUserDetails(String user_id, String user_pwd, String role) {
		this.user_id=user_id;
		this.user_pwd=user_pwd;
		this.authorities=Collections.singletonList(new SimpleGrantedAuthority(role));
	}


	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return authorities;
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return user_pwd;
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return user_id;
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
