package org.iitbact.cc.controllers;

import org.iitbact.cc.beans.ResponseBean;
import org.iitbact.cc.dto.PatientDto;
import org.iitbact.cc.dto.PatientStatsAgeDto;
import org.iitbact.cc.dto.PatientStatsDto;
import org.iitbact.cc.dto.PatientStatsGenderDto;
import org.iitbact.cc.helper.ControllerWrapper;
import org.iitbact.cc.repository.PatientLiveStatusRepository;
import org.iitbact.cc.requests.BaseRequest;
import org.iitbact.cc.requests.PatientSearchCriteria;
import org.iitbact.cc.response.ListResponse;
import org.iitbact.cc.services.PatientLiveStatusServices;
import org.iitbact.cc.services.PatientServices;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api")
public class PatientController {

	private final ControllerWrapper controllerWrapper;
	private final PatientServices patientServices;
	private final PatientLiveStatusServices patientLiveStatusServices;

	public PatientController(ControllerWrapper controllerWrapper, PatientServices patientServices, PatientLiveStatusServices p) {
		this.controllerWrapper = controllerWrapper;
		this.patientServices = patientServices;
		this.patientLiveStatusServices = p;
	}
    
    @PostMapping(path = "/patients/{pageNo}")
    @ApiOperation(response = PatientDto.class,responseContainer = "List", value = "API to fetch all active patient wrt filters")
    public ResponseBean<ListResponse<PatientDto>> getPatients(@PathVariable int pageNo, @RequestBody PatientSearchCriteria request){
        return controllerWrapper.wrap(ListResponse::new, request, (uid) -> patientServices.getPatients(pageNo,uid, request));
    }

	@PostMapping(path = "/patients/stats/{facilityId}")
	@ApiOperation(response = PatientStatsDto.class,responseContainer = "List", value = "API to get count of patients for a particular {facilityId}")
	public ResponseBean<ListResponse<PatientStatsDto>> getPatientStats(@PathVariable int facilityId, @RequestBody BaseRequest request ){
		return controllerWrapper.wrap(ListResponse::new, request, (uid) -> patientLiveStatusServices.getPatientStats(facilityId));
	}

	@PostMapping(path = "/patients/stats/gender/{facilityId}")
	@ApiOperation(response = PatientStatsGenderDto.class,responseContainer = "List", value = "API to get count of patients based on gender for a particular {facilityId}")
	public ResponseBean<ListResponse<PatientStatsGenderDto>> getPatientStatsByGender(@PathVariable int facilityId, @RequestBody BaseRequest request ){
		return controllerWrapper.wrap(ListResponse::new, request, (uid) -> patientLiveStatusServices.getPatientStatsByGender(facilityId));
	}

	@PostMapping(path = "/patients/stats/age/{facilityId}")
	@ApiOperation(response = PatientStatsAgeDto.class,responseContainer = "List", value = "API to get count of patients based on age at a particular {facilityId}")
	public ResponseBean<ListResponse<PatientStatsAgeDto>> getPatientStatsByAge(@PathVariable int facilityId, @RequestBody BaseRequest request ){
		return controllerWrapper.wrap(ListResponse::new, request, (uid) -> patientLiveStatusServices.getPatientStatsByAge(facilityId));
	}

	@PostMapping(path = "/patients/stats/")
	@ApiOperation(response = PatientStatsDto.class,responseContainer = "List", value = "API to get count of patients for ALL facilities")
	public ResponseBean<ListResponse<PatientStatsDto>> getPatientStatsAll(@RequestBody BaseRequest request ){
		return controllerWrapper.wrap(ListResponse::new, request, (uid) -> patientLiveStatusServices.getPatientStatsAll());
	}

	@PostMapping(path = "/patients/stats/gender/")
	@ApiOperation(response = PatientStatsGenderDto.class,responseContainer = "List", value = "API to get count of patients based on gender for ALL facilities")
	public ResponseBean<ListResponse<PatientStatsGenderDto>> getPatientStatsByGenderAll(@RequestBody BaseRequest request ){
		return controllerWrapper.wrap(ListResponse::new, request, (uid) -> patientLiveStatusServices.getPatientStatsByGenderAll());
	}

	@PostMapping(path = "/patients/stats/age/")
	@ApiOperation(response = PatientStatsAgeDto.class,responseContainer = "List", value = "API to get count of patients based on age for ALL facilities")
	public ResponseBean<ListResponse<PatientStatsAgeDto>> getPatientStatsByAgeAll(@RequestBody BaseRequest request ){
		return controllerWrapper.wrap(ListResponse::new, request, (uid) -> patientLiveStatusServices.getPatientStatsByAgeAll());
	}

    
}
