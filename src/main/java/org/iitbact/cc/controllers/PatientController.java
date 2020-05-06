package org.iitbact.cc.controllers;

import org.iitbact.cc.beans.ResponseBean;
import org.iitbact.cc.dto.PatientDto;
import org.iitbact.cc.helper.ControllerWrapper;
import org.iitbact.cc.requests.PatientSearchCriteria;
import org.iitbact.cc.response.ListResponse;
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

	public PatientController(ControllerWrapper controllerWrapper, PatientServices patientServices) {
		this.controllerWrapper = controllerWrapper;
		this.patientServices = patientServices;
	}
    
    @PostMapping(path = "/patients/{pageNo}")
    @ApiOperation(response = PatientDto.class,responseContainer = "List", value = "API to fetch all active patient wrt filters")
    public ResponseBean<ListResponse<PatientDto>> getPatients(@PathVariable int pageNo, @RequestBody PatientSearchCriteria request){
        return controllerWrapper.wrap(ListResponse::new, request, (uid) -> patientServices.getPatients(pageNo,uid, request));
    }
    
}
