package com.Team2Project.WorkWave.model;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface MessageMapper {
	
	@Insert("insert into sms_verify (id, phone, verify_code) values(#{id}, #{phone}, #{verify_code})")
	void saveSMS(MessageDTO messageDTO);
	
	@Select("select verify_code from sms_verify where phone = #{phone}")
	String getVerifyCode(String phone);
	
	@Delete("delete from sms_verify where phone = #{phone}")
	void removeVerifyCode(String phone);

}
