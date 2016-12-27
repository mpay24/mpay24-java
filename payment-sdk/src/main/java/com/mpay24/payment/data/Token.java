package com.mpay24.payment.data;

public class Token {
	private String returnCode;
	private String redirectLocation;
	private String token;
	private String apiKey;
	public String getReturnCode() {
		return returnCode;
	}
	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}
	public String getRedirectLocation() {
		return redirectLocation;
	}
	public void setRedirectLocation(String redirectLocation) {
		this.redirectLocation = redirectLocation;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getApiKey() {
		return apiKey;
	}
	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}
}
