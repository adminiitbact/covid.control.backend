package org.iitbact.cc.requests;

import org.iitbact.cc.beans.ContactDetails;
import org.iitbact.cc.entities.Facility;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FacilityRequest extends BaseRequest {
    private Facility facility;
    
    private ContactDetails contactDetails;
}
