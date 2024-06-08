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
			 // 여기 매핑은 누구나 접근 가능(ajax에 들어가는 매핑도 넣어야함.)
            .requestMatchers("/A/**","/ajax/**","/veri/**","/login").permitAll()
            .requestMatchers("/resources/**", "/css/**", "/js/**", "/image/**").permitAll()
            // 여기는 기업이나 개인 롤을 가진 계정만 접근 가능
            .requestMatchers("/CU/**").hasAnyRole("COMPANY","USER") 
            // 여기는 기업 롤을 가진 계정만 접근가능
            .requestMatchers("/C/**").hasRole("COMPANY") 
            // 여기는 개인 롤을 가진 계정만 접근가능
            .requestMatchers("/U/**").hasRole("USER")
						/* .requestMatchers("/G/**").anonymous() */
            // 다른 요청은 인증 필요
            .anyRequest().authenticated()
        )
        .formLogin(form -> form
            .loginPage("/login")
            .loginProcessingUrl("/loginOk")
            .defaultSuccessUrl("/A/main", true)
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
