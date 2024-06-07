package com.Team2Project.WorkWave.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.session.HttpSessionEventPublisher;

import com.Team2Project.WorkWave.service.CompanyDetailService;

@Configuration
@EnableWebSecurity
public class CompanySecurityConfig {
	
	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Autowired
	private CustomAccessDeniedHandler accessDeniedHandler;
	
	/*
	 * @Autowired private CustomSessionExpiredHandler sessionExpiredHandler;
	 */
	
	@Bean
	public SecurityFilterChain companySecurityFilterChain(HttpSecurity http) throws Exception {
		
		http
			.authorizeHttpRequests(authorize -> authorize
            .requestMatchers("/main.go","/", "/index","/company_insert.go","/idcheck.go",
            				 "/send_sms.go","/smsCodeCheck.go","/company_insert_ok.go", 
            				 "/login.go","/user_insert.go","/user_insert_ok.go",
            				 "/checkUserId","/sendCode","/verifyCode","/comNoCheck.go").permitAll() // 여기 매핑은 누구나 접근 가능(ajax에 들어가는 매핑도 넣어야함.)
            .requestMatchers("/resources/**", "/css/**", "/js/**", "/image/**").permitAll()
            .requestMatchers("/comBoard/list").hasAnyRole("COMPANY","USER") // 여기는 기업이나 개인 롤을 가진 계정만 접근 가능
            .requestMatchers("/comBoard/add").hasRole("COMPANY") // 여기는 기업 롤을 가진 계정만 접근가능
            .requestMatchers("적어야함").hasRole("USER") // 여기는 개인 롤을 가진 계정만 접근가능
            .anyRequest().authenticated()// 다른 요청은 인증 필요
        )
        .formLogin(form -> form
            .loginPage("/login.go")
            .loginProcessingUrl("/login_ok.go")
            .defaultSuccessUrl("/main.go", true)
            .permitAll()
        )
        .logout(logout -> logout
        	.logoutUrl("/logout")
        	.invalidateHttpSession(true)
        	.logoutSuccessUrl("/")
            .permitAll()
        )
        .exceptionHandling(exception -> exception
        	.accessDeniedHandler(accessDeniedHandler)
        )
				/*
				 * .sessionManagement(sessionManagement -> sessionManagement
				 * .invalidSessionUrl("/relogin.go") .maximumSessions(1)
				 * .maxSessionsPreventsLogin(true)
				 * .expiredSessionStrategy(sessionExpiredHandler) )
				 */
		.csrf(csrf -> csrf
			.disable()
		);
	
    return http.build();
		
	}
	
	 @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }
	
}
