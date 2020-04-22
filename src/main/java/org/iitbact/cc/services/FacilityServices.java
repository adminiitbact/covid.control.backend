package org.iitbact.cc.services;

import org.iitbact.cc.entities.Facility;
import org.iitbact.cc.repository.FacilityRepository;
import org.iitbact.cc.requests.BaseRequest;
import org.iitbact.cc.response.BooleanResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;

@Service
public class FacilityServices {

	@Autowired
	private FacilityRepository facilityRepository;

	@Autowired
	private ApiValidationService validationService;


	private void authenticateUser(String authToken) {
		validationService.verifyFirebaseIdToken(authToken);
	}

	public BooleanResponse addFacilityProfileData(int facilityId, BaseRequest request)
			throws JsonProcessingException {
		Facility facility = facilityRepository.findById(facilityId).get();

		//TODO logic to add facility
		
		facilityRepository.save(facility);
		BooleanResponse returnVal = new BooleanResponse(true);
		return returnVal;
	}


	public Facility fetchFacilityData(int facilityId, BaseRequest request) {
		this.authenticateUser(request.getAuthToken());
		Facility facility = facilityRepository.findById(facilityId).get();
		return facility;
	}

}
