package com.Team2Project.WorkWave.model;


import java.util.List;

import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface ComBoardMapper {
	List<ComBoardDTO> getComBoardList(); // 공고리스트
	int[] getInterestCompanyKeyList(int user_key); // 관심기업리스트
	List<CodeDTO> getJobCodeGroupList(); // 직업대분류리스트
	List<CodeDTO> getJobCodeSubList(); // 직업중분류리스트
	List<CodeDTO> getJobCodeStepList(); // 직업소분류리스트
	List<CodeDTO> getLocationCodeGroupList(); // 지역 대분류 리스트
	List<CodeDTO> getLocationCodeSubList(); // 지역 중분류 리스트
	
	int addComBoard(ComBoardDTO dto); // 공고등록
	void deleteComBoardTemp(int temp_key); //임시테이블 공고삭제
	int addComBoardTemp(ComBoardDTO dto); // 공고 임시저장
	int selectTempPk(int company_key); // 공고 임시저장

	int insertInterestCheck(InterestDTO dto); // 관심기업 추가
	int deleteInterestCheck(InterestDTO dto); // 관심기업 삭제

	int selectDefaultProfile(int user_key); // 기본 프로필 키 조회
	int addApply(ApplyDTO dto); // 공고 지원
}
