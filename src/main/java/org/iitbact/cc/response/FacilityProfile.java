package org.iitbact.cc.response;

import org.iitbact.cc.beans.BaseBean;
import org.iitbact.cc.entities.Facility;

public class FacilityProfile implements BaseBean {

	private Facility facilityProfile;

	public Facility getFacilityProfile() {
		return facilityProfile;
	}

	public void setFacilityProfile(Facility facilityProfile) {
		this.facilityProfile = facilityProfile;
	}


	
}
