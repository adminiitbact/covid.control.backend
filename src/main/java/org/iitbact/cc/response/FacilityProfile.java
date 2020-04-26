package org.iitbact.cc.response;

import org.iitbact.cc.beans.BaseBean;
import org.iitbact.cc.entities.Facility;

import lombok.Data;

@Data
public class FacilityProfile implements BaseBean<Facility> {

    private Facility facilityProfile;
    @Override
    public void setEntity(Facility facilityProfile) {
        this.facilityProfile = facilityProfile;
    }
}
