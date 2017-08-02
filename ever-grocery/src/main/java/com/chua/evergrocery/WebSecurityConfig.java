package com.chua.evergrocery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private @Qualifier("userAuthenticationProvider") AuthenticationProvider authenticationProvider;
	
	@Autowired
	private @Qualifier("userAuthenticationSuccessHandler") AuthenticationSuccessHandler authenticationSuccessHandler;
	
	@Autowired
	private @Qualifier("userAuthenticationFailureHandler") AuthenticationFailureHandler authenticationFailureHandler;
	
	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {
		httpSecurity
			.authorizeRequests()
				.antMatchers("/", 
							 "/index.html", 
							 "/app/**", 
							 "/lib/**", 
							 "/favicon.ico",
							 "/services/security/user",
							 "/login").permitAll()
				.anyRequest().authenticated()
					.and()
				.formLogin()
						.successHandler(authenticationSuccessHandler)
						.failureHandler(authenticationFailureHandler)
					.and()
				.csrf().disable();
	}
	
	@Override
	public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
		authenticationManagerBuilder
			.authenticationProvider(authenticationProvider);
	}
}
