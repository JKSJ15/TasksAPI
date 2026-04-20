package com.spring.taskAPI.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	private final SecurityFilter filter;
	
	public SecurityConfig(SecurityFilter filter) {
		super();
		this.filter = filter;
	}
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) {
		return http.csrf(csrf -> csrf.disable())
				.sessionManagement(session-> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
				.httpBasic(Customizer.withDefaults())
				.authorizeHttpRequests(authorize -> authorize
				.requestMatchers(HttpMethod.POST, "/auth/**").permitAll()
				.anyRequest().authenticated())
				.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class)
				.build();
	}
	@Bean
	public AuthenticationManager authMan(AuthenticationConfiguration config) throws Exception{
		return config.getAuthenticationManager();
	}
	@Bean
	public PasswordEncoder encoder() {
		return new BCryptPasswordEncoder();
	}
}
