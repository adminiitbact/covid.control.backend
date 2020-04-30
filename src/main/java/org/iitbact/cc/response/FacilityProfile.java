package org.iitbact.cc.response;

import org.iitbact.cc.beans.BaseBean;

import org.iitbact.cc.dto.FacilityDto;

import lombok.Data;

@Data
public class FacilityProfile implements BaseBean<FacilityDto> {
	
    private FacilityDto facilityProfile;
    
    @Override
    public void setEntity(FacilityDto facility) {
        this.facilityProfile = facility;
    }
}
