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
	
	private ContactDetails contactDetails;
	
    private Facility facilityProfile;
    
    @Override
    public void setEntity(Facility facility) {
        this.facilityProfile = facility;
        
        Object contact=facility.getFacilityContact().getData();
        ObjectMapper mapper=new ObjectMapper();
        JsonNode node=mapper.valueToTree(contact);
        if(node!=null) {
        	  ContactDetailsBuilder details=ContactDetails.builder();
        	  if(node.get(Constants.PRIMARY_CONTACT_NAME)!=null) {
        		 details= details.name(node.get(Constants.PRIMARY_CONTACT_NAME).asText());
        		  
        	  }
        	  if(node.get(Constants.PRIMARY_CONTACT_EMAIL)!=null) {
        		  details=  details.name(node.get(Constants.PRIMARY_CONTACT_EMAIL).asText());
        		  
        	  }
        	  
        	  if(node.get(Constants.PRIMARY_CONTACT_MOBILE)!=null) {
        		  details=details.name(node.get(Constants.PRIMARY_CONTACT_MOBILE).asText());
        		  
        	  }
        	 this.contactDetails= details.build();
        
        }
       
    }
}
