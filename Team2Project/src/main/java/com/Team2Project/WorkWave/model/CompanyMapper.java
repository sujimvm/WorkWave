package com.Team2Project.WorkWave.model;


import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CompanyMapper {
	
	int insertCompany(CompanyDTO dto);
	
	CompanyDTO companyInfo(String comapny_id);
	
	int companyUpdate(CompanyDTO dto);
	
	String findCompanyId(@Param("company_mgr_name") String company_mgr_name, @Param("companyNumber") String companyNumber);
	
	String findCompanyPwd(@Param("company_mgr_name") String company_mgr_name, 
						@Param("companyNumber") String companyNumber, 
						@Param("company_id") String company_id);
	
	int companyPwdUpdate(@Param("company_id") String company_id, @Param("company_pwd") String company_pwd);
	
	int companyDelete(int company_key);
	
	boolean companyIdCheck(String company_id);
	
	LoginDTO findCompanyIdbyId(String company_id);
}
