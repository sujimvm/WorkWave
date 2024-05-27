package com.Team2Project.WorkWave.model;


import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CompanyMapper {
	
	CompanyDTO docompanyLogin(String company_id);
	
	CompanyDTO companyIdCheck(String company_id);
}
