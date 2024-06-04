package com.Team2Project.WorkWave.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.Team2Project.WorkWave.service.CompanyDetailService;

@Configuration
@EnableWebSecurity(debug = true)
public class SecurityConfig {
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		
		http
        .authorizeHttpRequests(authorize -> authorize
            .requestMatchers("/", "/index","/company_insert.go","/idcheck.go",
            				 "/send_sms.go","/smsCodeCheck.go","/company_insert_ok.go", 
            				 "/login.go").permitAll() // 홈 페이지는 누구나 접근 가능
            .requestMatchers("/resources/**", "/css/**", "/js/**", "/image/**").permitAll()
            .requestMatchers("/main.go","/add").hasRole("COMPANY")
            .anyRequest().permitAll()// 다른 요청은 인증 필요
        )
        .formLogin(form -> form
            .loginPage("/login.go")
            .usernameParameter("company_id")
            .passwordParameter("company_pwd")
            .loginProcessingUrl("/company_login.go")
            .defaultSuccessUrl("/main.go", true)
            .permitAll()
        )
        .logout(logout -> logout
        	.logoutUrl("/logout")
        	.invalidateHttpSession(true)
        	.logoutSuccessUrl("/")
            .permitAll()
        )
		.csrf(csrf -> csrf
			.disable()
		);
	
    return http.build();
		
	}
	
	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
}
