package org.iitbact.cc.response;

import org.iitbact.cc.beans.BaseBean;

import com.google.firebase.auth.UserRecord;

import lombok.Data;

@Data
public class FacilityUserProfile implements BaseBean<UserRecord> {
	private UserRecord userRecord;

	@Override
	public void setEntity(UserRecord entity) {
		this.userRecord=entity;
	}
	
}
