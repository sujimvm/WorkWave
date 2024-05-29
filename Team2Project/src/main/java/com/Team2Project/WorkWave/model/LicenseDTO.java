package com.Team2Project.WorkWave.model;

import lombok.Data;

@Data
public class LicenseDTO {
	private int license_key;
	private int profile_key;
	private String license_name;
	private String license_barhang;
	private String license_date;

    // 생성자, Getter 및 Setter 생략 (필요시 추가)
}
