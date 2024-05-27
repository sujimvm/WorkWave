package com.Team2Project.WorkWave.model;

import org.apache.ibatis.annotations.*;

@Mapper
public interface TokenMapper {

    @Insert("INSERT INTO email_verif_token (id, email, token, verification_code) VALUES (#{id}, #{email}, #{token}, #{verificationCode})")
    void saveToken(EmailToken token);

    @Select("SELECT email FROM email_verif_token WHERE token = #{token}")
    String getEmailByToken(String token);

    @Delete("DELETE FROM email_verif_token WHERE token = #{token}")
    void removeToken(String token);

    @Select("SELECT verification_code FROM email_verif_token WHERE email = #{email}")
    String getVeriCodeByEmail(@Param("email") String email);

    @Delete("DELETE FROM email_verif_token WHERE email = #{email}")
    void removeTokenByEmail(@Param("email") String email);
}
