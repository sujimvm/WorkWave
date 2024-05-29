package com.Team2Project.WorkWave.model;

import java.util.List;

import lombok.Data;

@Data
public class CodeListDTO {
	List<CareerDTO> crDtoList; 
	List<EduDTO> eDtoList;
	List<LicenseDTO> lDtoList;
}
