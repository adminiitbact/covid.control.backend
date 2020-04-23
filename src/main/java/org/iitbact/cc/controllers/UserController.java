package org.iitbact.cc.controllers;

import io.swagger.annotations.ApiOperation;
import org.iitbact.cc.beans.ResponseBean;
import org.iitbact.cc.helper.ControllerWrapper;
import org.iitbact.cc.requests.BaseRequest;
import org.iitbact.cc.response.AdminUserProfileResponse;
import org.iitbact.cc.services.UserServices;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class UserController {

    private final ControllerWrapper controllerWrapper;
    private final UserServices userServices;

    public UserController(ControllerWrapper controllerWrapper, UserServices userServices) {
        this.controllerWrapper = controllerWrapper;
        this.userServices = userServices;
    }


    @PostMapping(path = "/admin/user/profile")
    @ApiOperation(response = AdminUserProfileResponse.class, value = "Api to fetch user profile for a admin")
    public ResponseBean userProfile(@RequestBody BaseRequest request) {
        return controllerWrapper.wrap(AdminUserProfileResponse::new, request, () -> userServices.profile(request));
    }

}
