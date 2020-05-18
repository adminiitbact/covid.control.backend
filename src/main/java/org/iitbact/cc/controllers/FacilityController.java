package org.iitbact.cc.controllers;

import org.iitbact.cc.beans.ResponseBean;
import org.iitbact.cc.dto.FacilityBedsStatsDto;
import org.iitbact.cc.dto.FacilityBedsStatsOverviewDto;
import org.iitbact.cc.dto.FacilityDto;
import org.iitbact.cc.helper.ControllerWrapper;
import org.iitbact.cc.requests.BaseRequest;
import org.iitbact.cc.requests.FacilityRequest;
import org.iitbact.cc.requests.FacilitySearchCriteria;
import org.iitbact.cc.requests.LinkFacilitiesRequest;
import org.iitbact.cc.response.BooleanResponse;
import org.iitbact.cc.response.FacilityLinkResponse;
import org.iitbact.cc.response.FacilityProfile;
import org.iitbact.cc.response.ListResponse;
import org.iitbact.cc.response.ListResponseType2;
import org.iitbact.cc.services.FacilityServices;
import org.iitbact.cc.services.FacilityStatsServices;
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
	private final FacilityStatsServices facilityStatsServices;

	public FacilityController(ControllerWrapper controllerWrapper, FacilityServices facilityServices, FacilityStatsServices facilityStatsServices) {
		this.controllerWrapper = controllerWrapper;
		this.facilityServices = facilityServices;
		this.facilityStatsServices = facilityStatsServices ;
	}

	@PostMapping(path = "/facilities/new")
	@ApiOperation(response = FacilityProfile.class, value = "API request to create a new facility")
	public ResponseBean<FacilityProfile> createFacility(@RequestBody FacilityRequest facilityRequest) {
		return controllerWrapper.wrap(FacilityProfile::new, facilityRequest,
				(uid) -> facilityServices.createFacility(facilityRequest,uid));
	}

	@PostMapping(path = "/facilities/{facilityId}/post")
	@ApiOperation(response = FacilityProfile.class, value = "API request to edit a facility data")
	public ResponseBean<FacilityProfile> editFacility(@PathVariable int facilityId,
			@RequestBody FacilityRequest facilityRequest) {
		return controllerWrapper.wrap(FacilityProfile::new, facilityRequest,
				(uid) -> facilityServices.editFacility(facilityId, facilityRequest,uid));
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

    @PostMapping(path = "/facilities/{facilityId}/links/get")
    @ApiOperation(response = FacilityDto.class,responseContainer = "List", value = "API to fetch links between facilities")
    public ResponseBean<FacilityLinkResponse> getLinkedFacilities(@PathVariable int facilityId, @RequestBody BaseRequest request){
        return controllerWrapper.wrap(FacilityLinkResponse::new, request, (uid) -> facilityServices.getLinkedFacilities(facilityId));
    }
    
    @PostMapping(path = "/facilities/{offset}/{limit}")
    @ApiOperation(response = FacilityDto.class,responseContainer = "List", value = "API to fetch all facilities wrt filters")
    public ResponseBean<ListResponseType2<FacilityDto>> getFacilities(@PathVariable int offset,@PathVariable int limit, @RequestBody FacilitySearchCriteria request){
        return controllerWrapper.wrap(ListResponseType2::new, request, (uid) -> facilityServices.getFacilities(offset,limit,uid, request));
    }

	@PostMapping(path = "/facilities/stats/beds/")
	@ApiOperation(response = FacilityBedsStatsDto.class,responseContainer = "List", value = "API to get count of beds by severity and covid status for ALL facilities")
	public ResponseBean<ListResponse<FacilityBedsStatsDto>> getBedsStatsAll(@RequestBody BaseRequest request ){
		return controllerWrapper.wrap(ListResponse::new, request, (uid) -> facilityStatsServices.getBedsStatsAll());
	}

	@PostMapping(path = "/facilities/stats/beds/{facilityId}")
	@ApiOperation(response = FacilityBedsStatsDto.class,responseContainer = "List", value = "API to get count of beds by severity and covid status for a {facilityId}")
	public ResponseBean<ListResponse<FacilityBedsStatsDto>> getBedsStatsAll(@PathVariable int facilityId,@RequestBody BaseRequest request ){
		return controllerWrapper.wrap(ListResponse::new, request, (uid) -> facilityStatsServices.getBedsStats(facilityId));
	}

	@PostMapping(path = "/facilities/stats/beds/overview/")
	@ApiOperation(response = FacilityBedsStatsOverviewDto.class,responseContainer = "List", value = "API to get count of total and available COVID and non COVID beds for ALL facilities")
	public ResponseBean<ListResponse<FacilityBedsStatsOverviewDto>> getBedsStatsOverviewAll(@RequestBody BaseRequest request ){
		return controllerWrapper.wrap(ListResponse::new, request, (uid) -> facilityStatsServices.getBedsStatsOverviewAll());
	}

	@PostMapping(path = "/facilities/stats/beds/overview/{facilityId}")
	@ApiOperation(response = FacilityBedsStatsOverviewDto.class,responseContainer = "List", value = "API to get count of total and available COVID and non COVID beds for a {facilityId}")
	public ResponseBean<ListResponse<FacilityBedsStatsOverviewDto>> getBedsStatsOverviewAll(@PathVariable int facilityId,@RequestBody BaseRequest request ){
		return controllerWrapper.wrap(ListResponse::new, request, (uid) -> facilityStatsServices.getBedsStatsOverview(facilityId));
	}


    
}
