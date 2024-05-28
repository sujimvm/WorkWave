package com.Team2Project.WorkWave.model;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
	
	// 로그인
	UserDTO doLogin(String user_id);
	
	void insertUser(UserDTO user);
	
	@Select("SELECT * FROM users WHERE user_id = #{userId}")
    UserDTO getUserById(String userId);
	
	@Select("SELECT user_id FROM users WHERE user_name = #{userName} AND user_email = #{userEmail}")
    String findUserId(@Param("userName") String userName, @Param("userEmail") String userEmail);

	@Select("SELECT user_id, user_pwd FROM users WHERE user_name = #{userName} AND user_id = #{userId} AND user_email = #{userEmail}")
	UserDTO findUserPassword(@Param("userName") String userName, @Param("userId") String userId, @Param("userEmail") String userEmail);
	
	UserDTO modify(String user_id);
	
}
	
	
