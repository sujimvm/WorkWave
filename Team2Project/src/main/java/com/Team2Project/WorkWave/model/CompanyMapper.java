package com.Team2Project.WorkWave.model;


import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CompanyMapper {
	
	int insertCompany(CompanyDTO dto);
	
	CompanyDTO companyInfo(String comapny_id);
	
	int companyUpdate(CompanyDTO dto);
}
