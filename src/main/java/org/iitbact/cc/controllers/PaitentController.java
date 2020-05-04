package org.iitbact.cc.controllers;

import org.iitbact.cc.beans.ResponseBean;
import org.iitbact.cc.dto.PatientDto;
import org.iitbact.cc.helper.ControllerWrapper;
import org.iitbact.cc.requests.PaitentSearchCriteria;
import org.iitbact.cc.response.ListResponse;
import org.iitbact.cc.services.PaitentServices;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api")
public class PaitentController {

	private final ControllerWrapper controllerWrapper;
	private final PaitentServices paitentServices;

	public PaitentController(ControllerWrapper controllerWrapper, PaitentServices paitentServices) {
		this.controllerWrapper = controllerWrapper;
		this.paitentServices = paitentServices;
	}
    
    @PostMapping(path = "/patients/{pageNo}")
    @ApiOperation(response = PatientDto.class,responseContainer = "List", value = "API to fetch all active patient wrt filters")
    public ResponseBean<ListResponse<PatientDto>> getPatients(@PathVariable int pageNo, @RequestBody PaitentSearchCriteria request){
        return controllerWrapper.wrap(ListResponse::new, request, (uid) -> paitentServices.getPatients(pageNo,uid, request));
    }
    
}
