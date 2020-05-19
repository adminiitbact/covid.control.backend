package org.iitbact.cc.controllers;



import org.iitbact.cc.beans.ResponseBean;
import org.iitbact.cc.dto.FacilityBedTimelineDto;
import org.iitbact.cc.helper.ControllerWrapper;
import org.iitbact.cc.requests.BaseRequest;
import org.iitbact.cc.response.FacilityBedsTimelineResponse;
import org.iitbact.cc.response.ListResponse;
import org.iitbact.cc.services.FacilityBedsTimelineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api")
public class FacilityBedsTimelineController {
	
	@Autowired
	private ControllerWrapper controllerWrapper;
	@Autowired
	private FacilityBedsTimelineService facilityBedsTimelineService;
	
	
	@PostMapping(path = "/facilities/beds/timeline/get")
	@ApiOperation(response = FacilityBedsTimelineResponse.class, value = "API request to provide bed timelines for all facilities")
	public ResponseBean<ListResponse<FacilityBedTimelineDto>> getBedTimelineForAllFacilities(@RequestBody BaseRequest request) {
		
		
		return controllerWrapper.wrap(ListResponse<FacilityBedTimelineDto>::new, request,
		 (uid) -> facilityBedsTimelineService.fetchAllBedFacilities());
		
		//return facilityBedsTimelineService.fetchAllBedFacilities();
		
	}
	
	@PostMapping(path = "/facilities/beds/timeline/{facilityId}/get")
	@ApiOperation(response = FacilityBedsTimelineResponse.class, value = "API request to provide bed timelines for a given facility")
	public ResponseBean<ListResponse<FacilityBedTimelineDto>> getBedTimelineForGivenFacility(@PathVariable int facilityId, @RequestBody BaseRequest request) {
		return controllerWrapper.wrap(ListResponse<FacilityBedTimelineDto>::new, request,
				(uid) -> facilityBedsTimelineService.fetchBedTimelineForFacility(facilityId));
	}

}
