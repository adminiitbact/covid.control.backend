package org.iitbact.cc.response;

import lombok.Data;
import org.iitbact.cc.beans.BaseBean;
import org.iitbact.cc.entities.Facility;

@Data
public class FacilityProfile implements BaseBean<Facility> {

    private Facility facilityProfile;

    @Override
    public void setEntity(Facility facilityProfile) {
        this.facilityProfile = facilityProfile;
    }
}
