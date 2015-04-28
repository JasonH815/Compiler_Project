package com.cpsc323.compiler.tokenize;

/**
 * 
 * Author: @author Project: Compiler Package: Date:17/11/2013 File:
 * MatchResult.java Todo: TODO
 */

class MatchResult {

	private String token;
	private String remainder;

	public MatchResult(String token, String remainder) {
		this.token = token;
		this.remainder = remainder;
	}

	public String getToken() {
		return token;
	}

	public String getRemainder() {
		return remainder;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public void setRemainder(String remainder) {
		this.remainder = remainder;
	}
}