package com.Team2Project.WorkWave.model;

import java.util.List;
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
	
	public int userUpdatePwd(@Param("user_id") String user_id, @Param("encordedPwd") String encordedPwd);
	
	public int idCnt(String user_id);
	
	UserDTO modify(String user_id);

	public int updateok(UserDTO dto);
	
	UserDTO delete(String user_id);
	
	public int deleteok(int user_key);
	// 이력서 지원 갯수
	public int applyCnt(int user_key);
	// 이력서 열람 갯수
	public int applyCheckCnt(int user_key);
	// 이력서 미열람 갯수
	public int UapplyNonCheckCnt(int user_key);
	// 포지션 제안 갯수
	public int positionJean(int user_key);
	// 관심 기업 갯수
	public int interest(int user_key);
	// 지원 취소 갯수
	public int applyCancel(int user_key);
	// 유저 지원 정보 리스트 
	public List<ApplyDTO> applyInfo(int user_key);
	// 유저 지원한 공고 리스트
	public ComBoardDTO applyBoard(int com_board_key);
	// 지원한 공고의 기업 이름
	public CompanyDTO applyCom(int company_key);
	// 지원취소시 apply_check 'N' -> 'Y'로 바꾸기
	void applyCancelUp(int apply_key);
	
	UserLoginDTO findUserIdById(String user_id);
	
	public String profileName(int user_key);
	
	// 유저수 조회
	int countUser();
}
	
	
