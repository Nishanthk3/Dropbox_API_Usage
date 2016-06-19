package com.nishanth.dropbox.api;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonRootName(value = "metadataInfo")
public class MetadataInfo {

	private String hash;
	private String thumb_exists;
	private String bytes;
	private String path;
	private String is_dir;
	private String icon;
	private String root;
	private List<Contents> contents;
	private String size;
	public String getHash() {
		return hash;
	}
	public void setHash(String hash) {
		this.hash = hash;
	}
	public String getThumb_exists() {
		return thumb_exists;
	}
	public void setThumb_exists(String thumb_exists) {
		this.thumb_exists = thumb_exists;
	}
	public String getBytes() {
		return bytes;
	}
	public void setBytes(String bytes) {
		this.bytes = bytes;
	}
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
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public String getRoot() {
		return root;
	}
	public void setRoot(String root) {
		this.root = root;
	}
	public List<Contents> getContents() {
		return contents;
	}
	public void setContents(List<Contents> contents) {
		this.contents = contents;
	}
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	
	
}
