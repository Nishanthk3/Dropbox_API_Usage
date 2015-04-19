package com.nishanth.dropbox;

public class AccessTokenClass {

	private String access_token;
	private String token_type;
	private String bearer;
	private String uid;
	public String getAccess_token() {
		return access_token;
	}
	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}
	public String getToken_type() {
		return token_type;
	}
	public void setToken_type(String token_type) {
		this.token_type = token_type;
	}
	public String getBearer() {
		return bearer;
	}
	public void setBearer(String bearer) {
		this.bearer = bearer;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	
}

