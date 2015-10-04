package com.nishanth.dropbox.api;

import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName(value = "ack")
public class Ack {
	
	private MetadataInfo metadata;
	private Error error;
	public MetadataInfo getMetadata() {
		return metadata;
	}
	public void setMetadata(MetadataInfo metadata) {
		this.metadata = metadata;
	}
	public Error getError() {
		return error;
	}
	public void setError(Error error) {
		this.error = error;
	}
	
	
}
