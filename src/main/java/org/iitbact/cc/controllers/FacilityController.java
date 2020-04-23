package org.iitbact.cc.controllers;

import org.iitbact.cc.beans.ResponseBean;
import org.iitbact.cc.beans.ResponseBuilder;
import org.iitbact.cc.entities.Facility;
import org.iitbact.cc.exceptions.CovidControlErpError;
import org.iitbact.cc.exceptions.CovidControlException;
import org.iitbact.cc.requests.BaseRequest;
import org.iitbact.cc.response.BooleanResponse;
import org.iitbact.cc.response.FacilityProfile;
import org.iitbact.cc.services.FacilityServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api")
public class FacilityController {

	@Autowired
	private FacilityServices facilityServices;
	
	
	
	
	@PostMapping(path = "/facility/{facilityId}")
	@ApiOperation(response = Facility.class, value = "API request to fetch a facility data")
	public ResponseBean getFacilityData(@PathVariable int facilityId, @RequestBody BaseRequest request)
			throws JsonProcessingException {
		CovidControlErpError error = null;
		FacilityProfile data = new FacilityProfile();
		try {
			data.setFacilityProfile(facilityServices.fetchFacilityData(facilityId, request));
		} catch (CovidControlException e) {
			error = e.getError();
		}
		ResponseBuilder responseBuilder = new ResponseBuilder(data, error);
		return responseBuilder.build();
	}

	//TODO change request to fetch inputs from front end
	@PostMapping(path = "/add/facility/profile/{facilityId}")
	@ApiOperation(response = BooleanResponse.class, value = "API request to add profile data for a facility")
	public ResponseBean addFacilityProfileData(@PathVariable int facilityId, @RequestBody BaseRequest request)
			throws JsonProcessingException {
		CovidControlErpError error = null;
		BooleanResponse data = null;
		try {
			data = facilityServices.addFacilityProfileData(facilityId, request);
		} catch (CovidControlException e) {
			error = e.getError();
		}
		ResponseBuilder responseBuilder = new ResponseBuilder(data, error);
		return responseBuilder.build();
	}
	
}
