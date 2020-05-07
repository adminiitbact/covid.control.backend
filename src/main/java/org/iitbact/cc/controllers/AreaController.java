package org.iitbact.cc.controllers;

import org.iitbact.cc.beans.ResponseBean;
import org.iitbact.cc.entities.Area;
import org.iitbact.cc.helper.ControllerWrapper;
import org.iitbact.cc.requests.BaseRequest;
import org.iitbact.cc.response.ListResponse;
import org.iitbact.cc.services.AreaService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping("/api/areas")
public class AreaController {

    private final AreaService areaService;
    private final ControllerWrapper controllerWrapper;
    
    public AreaController(AreaService areaService, ControllerWrapper controllerWrapper) {
        this.areaService = areaService;
        this.controllerWrapper = controllerWrapper;
    }


    @PostMapping(path = "/list")
    @ApiOperation(response = Area.class, value = "API request to create a new area")
    public ResponseBean<ListResponse<Area>> listArea(@RequestBody BaseRequest areaRequest) {
        return controllerWrapper.wrap(ListResponse::new, areaRequest,
                (uid) -> areaService.listAreas(uid));
    }

}
