package com.charith.app.ws.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.charith.app.ws.SpringApplicationContext;
import com.charith.app.ws.service.UserService;
import com.charith.app.ws.shared.dto.UserDto;
import com.charith.app.ws.ui.model.request.UserLoginRequestModel;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private final AuthenticationManager authenticationManager;

	public AuthenticationFilter(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}

	//This method will attempt to authenticate the user. This will be called in WebSecurity class inside configure method.
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		
		try {
			// Read the JSON request and map the values to the fields in UserLoginRequestModle
			UserLoginRequestModel creds = new ObjectMapper().readValue(request.getInputStream(), UserLoginRequestModel.class);
			//Authenticate the user with credentials.
			return this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
					creds.getEmail(), 
					creds.getPassword(), 
					new ArrayList<>())
					);
			
			
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException();
		}
		//return null;
		
	}

	//This method is called after attemptAuthentication method above if the authentication is successful. 
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		
		String username = ((User) authResult.getPrincipal()).getUsername();
		// Jwts is included in the pom.xml, so you do not need to instantiate it.
		String token = Jwts.builder()
				.setSubject(username)
				.setExpiration(new Date(System.currentTimeMillis()+SecurityConstants.EXPIRATION_TIME))
				.signWith(SignatureAlgorithm.HS512, SecurityConstants.getTokenSecret())
				.compact();
		
		response.addHeader(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX + token);
		UserService userService = ((UserService)SpringApplicationContext.getBean("userServiceImpl"));
		UserDto userDto = userService.getUser(username);
		response.addHeader("UserID", userDto.getUserId());
	}
	
	
	
	
}
