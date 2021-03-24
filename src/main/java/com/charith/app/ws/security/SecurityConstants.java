package com.charith.app.ws.security;

import com.charith.app.ws.SpringApplicationContext;

public class SecurityConstants {
	
	public static final long EXPIRATION_TIME = 864000000; //10 days in milliseconds
	public static final String TOKEN_PREFIX = "Bearer ";
	public static final String HEADER_STRING = "Authorization";
	public static final String SIGN_UP_URL = "/users";
//	public static final String TOKEN_SECRET = "jf9f45lasdfk8";
	
	public static String getTokenSecret() {
		return ((AppProperties)SpringApplicationContext.getBean("appProperties")).getTokenSecret();
		
		
	}
}
