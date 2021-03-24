package com.charith.app.ws.security;

import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.charith.app.ws.service.UserService;

@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter{
	
	private final UserService userDetailsService;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	
	public WebSecurity(UserService userDetailsService, BCryptPasswordEncoder bCryptPasswordEncoder) {
		this.userDetailsService = userDetailsService;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}

	/*This method simply configures which class to use to get user details and the encryption method
	 * used to encrypt the password.
	 */
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
	}

	/* This method makes all the other URLs protected except for the signup URL so that a user can sign up but cannot use any other URLs
	   without authorization. */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable().authorizeRequests()
		.antMatchers(HttpMethod.POST, SecurityConstants.SIGN_UP_URL)
		.permitAll()
		.anyRequest()
		.authenticated().and()
		.addFilter(this.getAuthenticationFilter())
		.addFilter(new AuthorizationFilter(this.authenticationManager()))
		.sessionManagement()
		.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	}
	
	protected AuthenticationFilter getAuthenticationFilter(){
		try {
			final AuthenticationFilter filter = new AuthenticationFilter(this.authenticationManager());
			filter.setFilterProcessesUrl("/users/login");
			return filter;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
}
