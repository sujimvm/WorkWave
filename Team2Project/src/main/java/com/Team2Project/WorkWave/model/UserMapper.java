package com.Team2Project.WorkWave.model;


import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
	
	// 로그인
	UserDTO doLogin(String user_id);
	
	void insertUser(UserDTO user);
	
	@Select("SELECT * FROM users WHERE user_id = #{userId}")
    UserDTO getUserById(String userId);
}
