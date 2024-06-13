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
	
	@Select("SELECT * FROM users WHERE user_id = #{user_id}")
    UserDTO getUserById(String user_id);
	
	@Select("SELECT user_id, user_join_date FROM users WHERE user_name = #{userName} AND user_email = #{userEmail}")
    UserDTO findUserId(@Param("userName") String userName, @Param("userEmail") String userEmail);

	@Select("SELECT user_id, user_pwd FROM users WHERE user_name = #{userName} AND user_id = #{userId} AND user_email = #{userEmail}")
	UserDTO findUserPassword(@Param("userName") String userName, @Param("userId") String userId, @Param("userEmail") String userEmail);
	
	public int userUpdatePwd(String user_id, String user_pwd);
	
	public int idCnt(String user_id);
	
	UserDTO modify(String user_id);

	public int updateok(UserDTO dto);
	
	UserDTO delete(String user_id);
	
	public int deleteok(int user_key);
	
	public int applyCnt(int user_key);

	public int applyCheckCnt(int user_key);
	
	public int positionJean(int user_key);

	public int interest(int user_key);
	
	UserLoginDTO findUserIdById(String user_id);
	
	public String profileName(int user_key);
	
}
	
	
