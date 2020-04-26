package org.iitbact.cc.controllers;

import org.iitbact.cc.beans.ResponseBean;
import org.iitbact.cc.entities.Facility;
import org.iitbact.cc.helper.ControllerWrapper;
import org.iitbact.cc.requests.BaseRequest;
import org.iitbact.cc.requests.FacilityRequest;
import org.iitbact.cc.requests.FacilitySearchCriteria;
import org.iitbact.cc.requests.LinkFacilitiesRequest;
import org.iitbact.cc.response.BooleanResponse;
import org.iitbact.cc.response.FacilityProfile;
import org.iitbact.cc.response.ListResponse;
import org.iitbact.cc.services.FacilityServices;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api")
public class FacilityController {

	private final ControllerWrapper controllerWrapper;
	private final FacilityServices facilityServices;

	public FacilityController(ControllerWrapper controllerWrapper, FacilityServices facilityServices) {
		this.controllerWrapper = controllerWrapper;
		this.facilityServices = facilityServices;
	}

	@PostMapping(path = "/facilities/new")
	@ApiOperation(response = FacilityProfile.class, value = "API request to create a new facility")
	public ResponseBean<FacilityProfile> createFacility(@RequestBody FacilityRequest facilityRequest) {
		return controllerWrapper.wrap(FacilityProfile::new, facilityRequest,
				(uid) -> facilityServices.createFacility(facilityRequest));
	}

	@PostMapping(path = "/facilities/{facilityId}/post")
	@ApiOperation(response = FacilityProfile.class, value = "API request to edit a facility data")
	public ResponseBean<FacilityProfile> editFacility(@PathVariable int facilityId,
			@RequestBody FacilityRequest facilityRequest) {
		return controllerWrapper.wrap(FacilityProfile::new, facilityRequest,
				(uid) -> facilityServices.editFacility(facilityId, facilityRequest.getFacility()));
	}

	@PostMapping(path = "/facilities/{facilityId}/get")
	@ApiOperation(response = FacilityProfile.class, value = "API request to fetch a facility data")
	public ResponseBean<FacilityProfile> getFacilityData(@PathVariable int facilityId,
			@RequestBody BaseRequest request) {
		return controllerWrapper.wrap(FacilityProfile::new, request,
				(uid) -> facilityServices.fetchFacilityData(facilityId));
	}

    @PostMapping(path = "/facilities/{facilityId}/links/post")
    @ApiOperation(response = BooleanResponse.class, value = "API to create links between facilities")
    public ResponseBean<BooleanResponse> linkFacilities(@PathVariable int facilityId, @RequestBody LinkFacilitiesRequest request){
        return controllerWrapper.wrap(BooleanResponse::new, request, (uid) -> facilityServices.linkFacilities(facilityId, request));
    }

    @PostMapping(path = "/facilitiesy/{facilityId}/links/get")
    @ApiOperation(response = BooleanResponse.class,responseContainer = "List", value = "API to fetch links between facilities")
    public ResponseBean<ListResponse<Facility>> getLinkedFacilities(@PathVariable int facilityId, @RequestBody BaseRequest request){
        return controllerWrapper.wrap(ListResponse::new, request, (uid) -> facilityServices.getLinkedFacilities(facilityId));
    }
    
    @PostMapping(path = "/facilities/{pageNo}")
    @ApiOperation(response = Facility.class,responseContainer = "List", value = "API to fetch all facilities wrt filters")
    public ResponseBean<ListResponse<Facility>> getFacilities(@PathVariable int pageNo, @RequestBody FacilitySearchCriteria request){
        return controllerWrapper.wrap(ListResponse::new, request, (uid) -> facilityServices.getFacilities(pageNo,request));
    }
    
}
