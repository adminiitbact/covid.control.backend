package org.iitbact.cc.controllers;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletResponse;

import org.iitbact.cc.beans.ResponseBean;
import org.iitbact.cc.dto.PatientDto;
import org.iitbact.cc.helper.ControllerWrapper;
import org.iitbact.cc.requests.PaitentSearchCriteria;
import org.iitbact.cc.response.ListResponse;
import org.iitbact.cc.services.PaitentServices;
import org.springframework.http.HttpHeaders;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
        
    @RequestMapping(value = "/resources/patients.csv", method = RequestMethod.GET)
    public void getFile(HttpServletResponse response) throws IOException {
    	
    	ClassLoader classloader = Thread.currentThread().getContextClassLoader();
    	File file = new File(classloader.getResource("patients.csv").getFile());
         
        response.setContentType("text/csv");
		response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
				"attachment; filename=\"patients.csv\"");         
        response.setContentLength((int)file.length());
 
        InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
 
        FileCopyUtils.copy(inputStream, response.getOutputStream());
    }
    
}
