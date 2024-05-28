package com.Team2Project.WorkWave.model;


import java.util.List;

import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface ComBoardMapper {
	List<ComBoardDTO> getJobList(); // 공고리스트
	List<CodeDTO> getJobCodeGroupList(); // 직업대분류리스트
	List<CodeDTO> getJobCodeSubList(); // 직업중분류리스트
	List<CodeDTO> getJobCodeStepList(); // 직업소분류리스트
	List<CodeDTO> getLocationCodeGroupList(); // 지역 대분류 리스트
	List<CodeDTO> getLocationCodeSubList(); // 지역 중분류 리스트
}
