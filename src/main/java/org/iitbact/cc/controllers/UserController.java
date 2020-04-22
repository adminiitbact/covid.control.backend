package org.iitbact.cc.controllers;

import org.iitbact.cc.beans.ResponseBean;
import org.iitbact.cc.beans.ResponseBuilder;
import org.iitbact.cc.exceptions.CovidControlErpError;
import org.iitbact.cc.exceptions.CovidControlException;
import org.iitbact.cc.requests.BaseRequest;
import org.iitbact.cc.response.AdminUserProfileResponse;
import org.iitbact.cc.services.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api")
public class UserController {
	
	@Autowired
	private UserServices userServices;
	
	
	@PostMapping(path = "/admin/user/profile")
	@ApiOperation(response = AdminUserProfileResponse.class,value = "Api to fetch user profile for a admin")
	public ResponseBean userProfile(@RequestBody BaseRequest request) {
		CovidControlErpError error = null;
		AdminUserProfileResponse data=null;
		try {
			data=userServices.profile(request);
		} catch (CovidControlException e) {
			error = e.getError();
		}
		ResponseBuilder responseBuilder = new ResponseBuilder(data,error);
		return responseBuilder.build();
	}

}
