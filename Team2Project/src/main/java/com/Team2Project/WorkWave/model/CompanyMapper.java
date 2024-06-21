package com.Team2Project.WorkWave.model;


import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CompanyMapper {
	
	int insertCompany(CompanyDTO dto);
	
	CompanyDTO companyInfo(String comapany_id);
	
	int companyUpdate(CompanyDTO dto);
	
	String findCompanyId(@Param("company_mgr_name") String company_mgr_name, @Param("companyNumber") String companyNumber);
	
	
	String findCompanyPwd(@Param("company_mgr_name") String company_mgr_name, 
						@Param("companyNumber") String companyNumber, 
						@Param("company_id") String company_id);
	
	int companyPwdUpdate(@Param("company_id") String company_id, @Param("encordedPwd") String encordedPwd);
	
	int companyDelete(int company_key);
	
	boolean companyIdCheck(String company_id);
	
	CompanyLoginDTO findCompanyIdbyId(String company_id);
	
	CompanyDTO companyInfoByNo(String company_number);

	int companyBoardingCnt(int company_key);
	
	int companyBoardEndCnt(int company_key);
	
	int applyNoneCheckCnt(int company_key);
	
	int positionCnt(int company_key);
	
	int countCompany();
	
	CompanyDTO searchCompany(int company_key);
	
	List<ComBoardDTO> getComBoardList(int company_key);
	List<ComBoardDTO> getComBoardTempList(int company_key);
	int getComBoardApply(int com_board_key);
	int getComBoardApplyNonCheck(int com_board_key);
	
	List<ProfileDTO> totalApplyList(int com_board_key);
	UserDTO userList(int user_key);
}
