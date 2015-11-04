package com.nishanth.dropbox.api;

import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName(value = "sharedFolder")
public class SharedFolder {
	
	private String shared_folder_id;
	private String is_team_only_shared_folder;

	public String getShared_folder_id() {
		return shared_folder_id;
	}

	public void setShared_folder_id(String shared_folder_id) {
		this.shared_folder_id = shared_folder_id;
	}

	public String getIs_team_only_shared_folder() {
		return is_team_only_shared_folder;
	}

	public void setIs_team_only_shared_folder(String is_team_only_shared_folder) {
		this.is_team_only_shared_folder = is_team_only_shared_folder;
	}
	
}
