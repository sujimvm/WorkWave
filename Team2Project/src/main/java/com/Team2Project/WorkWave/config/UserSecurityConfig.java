package com.Team2Project.WorkWave.config; 

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.Team2Project.WorkWave.service.UsersDetailService;

@Configuration
@EnableWebSecurity
@Order(2)
public class UserSecurityConfig {
	
	@Bean
	public UserDetailsService userDetailsService() {
		return new UsersDetailService();
	}
	
	@Bean
	public BCryptPasswordEncoder userbCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public SecurityFilterChain userSecurityFilterChain(HttpSecurity http) throws Exception {
		
		http
		.authenticationProvider(userDaoAuthenticationProvider())
        .authorizeHttpRequests(authorize -> authorize
            .requestMatchers("/", "/index","/company_insert.go","/idcheck.go",
            				 "/send_sms.go","/smsCodeCheck.go","/company_insert_ok.go", 
            				 "/login.go").permitAll() // 홈 페이지는 누구나 접근 가능
            .requestMatchers("/resources/**", "/css/**", "/js/**", "/image/**").permitAll()
            .requestMatchers("/user_main.go","/add").hasRole("USER")
            .anyRequest().permitAll()// 다른 요청은 인증 필요
        )
        .formLogin(form -> form
            .loginPage("/ulogin.go")
            .usernameParameter("user_id")
            .passwordParameter("user_pwd")
            .loginProcessingUrl("/user_login.go")
            .defaultSuccessUrl("/user_main.go", true)
            .failureUrl("/ulogin.go")
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
	DaoAuthenticationProvider userDaoAuthenticationProvider() {
		DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
		daoAuthenticationProvider.setUserDetailsService(userDetailsService());
		daoAuthenticationProvider.setPasswordEncoder(userbCryptPasswordEncoder());
		return daoAuthenticationProvider;
	}

}
