package com.nishanth.dropbox.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DirectoryInfo {
	private String path;
	private String is_dir;
	
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getIs_dir() {
		return is_dir;
	}
	public void setIs_dir(String is_dir) {
		this.is_dir = is_dir;
	}
	
}
