package com.Team2Project.WorkWave.model;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ChatMapper {

	public List<ChatDTO> list();
}
