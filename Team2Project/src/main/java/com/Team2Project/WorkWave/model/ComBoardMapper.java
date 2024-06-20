package com.Team2Project.WorkWave.model;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface ComBoardMapper {
	List<ComBoardDTO> getComBoardList(HashMap<String, Object> reqMapperMap); // 공고리스트
	int[] getInterestCompanyKeyList(int user_key); // 관심기업리스트
	int countComBoard(); // 공고리스트 카운트
	int[] getApplyList(int profile_key); // 공고 지원 리스트
	
	List<CodeDTO> getJobCodeGroupList(); // 직업대분류리스트
	List<CodeDTO> getJobCodeSubList(); // 직업중분류리스트
	List<CodeDTO> getJobCodeStepList(); // 직업소분류리스트
	List<CodeDTO> getLocationCodeGroupList(); // 지역 대분류 리스트
	List<CodeDTO> getLocationCodeSubList(); // 지역 중분류 리스트
	
	int addComBoard(ComBoardDTO dto); // 공고등록
	void deleteComBoardTemp(int temp_key); //임시테이블 공고삭제
	int addComBoardTemp(ComBoardDTO dto); // 공고 임시저장
	int selectTempPk(int company_key); // 공고 임시저장 테이블 키
	int updateComBoardTemp(ComBoardDTO dto); // 공고 임시저장

	int insertInterestCheck(InterestDTO dto); // 관심기업 추가
	int deleteInterestCheck(InterestDTO dto); // 관심기업 삭제

	int selectDefaultProfile(int user_key); // 기본 프로필 키 조회
	int selectComBoardCompanyKey(int company_key); // 지원하는 기업 키 조회
	int addApply(ApplyDTO dto); // 공고 지원
	
	ComBoardDTO getComBoard(int com_board_key);
	int getApplyCount(int com_board_key); // 기본 프로필 키 조회
	List<Map<String, Object>> getApplyAvgAge(int com_board_key);
	List<Map<String, Object>> getApplyAvgGender(int com_board_key);
	List<Map<String, Object>> getApplyAvgEdu(int com_board_key);
	int recommendListCount(String code);//공고 상세보기 추천인재 리스트 총 갯수
	List<ProfileDTO> getRecommendList(HashMap<String, Object> reqMapperMap);//공고 상세보기 추천인재 리스트
	int[] getPositionSuccessList(int com_board_key); // 공고 지원 리스트
	int insertPosition(PositionDTO dto); // 관심기업 추가

	int updateComBoard(ComBoardDTO dto); // 공고수정
	
	List<ComBoardDTO> getMainNewComBoardList(); // (메인) 공고리스트
	List<ComBoardDTO> getMainHotComBoardList(); // (메인) 공고리스트
	List<ComBoardDTO> getMainTimeComBoardList(); // (메인) 공고리스트
	List<ComBoardDTO> getComBoardList(String type); // (메인) 공고리스트
	
	List<ComBoardDTO> getUnifiedSearchList(Page pdto);
	
	
	int countSearchList(String keyword);
}
