package com.Team2Project.WorkWave.model;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import java.util.Date;

@Data
public class UserDTO {
    private Integer user_key;
    private String user_id;
    private String user_pwd;
    private String user_name;
    private String user_phone;
    private String user_email;
    private String user_addr;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date user_birth;

    private String user_gender;
    private Date user_join_date;
    private String user_confirm;
    private String role;
    private int enabled;
}