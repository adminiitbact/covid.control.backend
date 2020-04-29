package org.iitbact.cc.response;

import org.iitbact.cc.beans.BaseBean;
import org.iitbact.cc.beans.ContactDetails;
import org.iitbact.cc.beans.ContactDetails.ContactDetailsBuilder;
import org.iitbact.cc.constants.Constants;
import org.iitbact.cc.entities.Facility;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Data;

@Data
public class FacilityProfile implements BaseBean<Facility> {
	
    private Facility facilityProfile;
    
    @Override
    public void setEntity(Facility facility) {
        this.facilityProfile = facility;
    }
}
