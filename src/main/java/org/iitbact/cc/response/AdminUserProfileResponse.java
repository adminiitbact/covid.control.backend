package org.iitbact.cc.response;

import org.iitbact.cc.beans.BaseBean;
import org.iitbact.cc.entities.AdminUser;

public class AdminUserProfileResponse implements BaseBean {

	private AdminUser profile;


	public AdminUser getProfile() {
		return profile;
	}


	public void setProfile(AdminUser profile) {
		this.profile = profile;
	}
	
}
