package com.Team2Project.WorkWave.model;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProfileMapper {
	
	//카테고리를 대분류 가져오는 메서드
		public List<CodeDTO> category(); 
		
		//카테고리를 중분류 가져오는 메서드
		public List<CodeDTO> categorysub(String no);
		
		//카테고리를 소분류 가져오는 메서드
		public List<CodeDTO> categorystep(String no);
		
		//검색 학교명 학교리스트 가져오는 메서드
		public List<CodeDTO> schoolname(CodeDTO dto);
		
		//검색 학교명의 학과리스트 가져오는 메서드
		public List<CodeDTO> department(CodeDTO dto);
		
		//검색 자격증의 자격증리스트 가져오는 메서드
		public List<LicenseDTO> license(String c_name);
		
		//사용자 이력서 리스트 가져오는 메서드
		public List<ProfileDTO> profileList(int userKey);
		
		//사용자 미완성 리스트 가져오는 메서드
		public List<ProfileDTO> profileTempList(int userKey);
		
		// 이력서 학력 가져오는 메서드
		public List<EduDTO> eduList(int no);
		
		// 이력서 경력 가져오는 메서드
		public List<CareerDTO> careerList(int no);
		
		// 이력서 자격증 가져오는 메서드
		public List<LicenseDTO> licenseList(int no);
		
		// 이력서 기본정보 가져오는 메서드
		public ProfileDTO profileinfo(int no);
		
		// 이력서 추가하는 메서드
		public int profileInsert(ProfileDTO dto);
		
		// 이력서 임시저장 메서드
		public int profileTempInsert(ProfileDTO dto);
		
		// 기본 이력서 있는지 확인하는 메서드
		public int profileCkeck(int no);
		
		//대표 이력서로 변경 메서드
		public int defaultChangeY(int defaultKey);
		
		// 기본 이력서로 변경 메서드
		public void defaultChangeN();

		//대표 이력서로 변경 메서드
		public int nowInsertProfileKey(int no);
		
		//미완성 이력서 프로필 킬 가져오는 메서드
		public int nowInsertTempProfileKey(int no);
		
		// 학력 추가하는 메서드
		public void EduInsert(EduDTO edto);
		
		// 경력 추가하는 메서드
		public int CareerInsert(CareerDTO crdto);

		// 자격증 추가하는 메서드
		public int LicenseInsert(LicenseDTO ldto);

		//이력서 삭제하는 메서드
		public int profileDelect(int pro_key);
		
		//이력서 업데이트 메서드
		public int updateProfile(ProfileDTO profileDto);
		public int updateCareer(CareerDTO careerDto);
		public int updateLicense(LicenseDTO licenDto);
		
		//이력서 중간저장 불러오는 메서드
		public ProfileDTO profileTempinfo(int no);
		public List<CareerDTO> careerTempinfo(int no);
		public List<LicenseDTO> licenseTempinfo(int no);
		
		
		//중간 이력서 중간 저장
		public int updateProfileTemp(ProfileDTO profileDto);
		public int updateCareerTemp(CareerDTO careerDto);
		public int updateLicenseTemp(LicenseDTO licenDto);
		
		
		

}
