package com.Team2Project.WorkWave.model;


import java.util.List;

import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface ComBoardMapper {
	List<ComBoardDTO> getJobList();
	List<CodeDTO> getJobCodeGroupList();
	List<CodeDTO> getJobCodeSubList();
	List<CodeDTO> getJobCodeStepList();
	List<CodeDTO> getLocationCodeGroupList();
	List<CodeDTO> getLocationCodeSubList();
}
