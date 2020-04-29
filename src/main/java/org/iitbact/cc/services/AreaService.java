package org.iitbact.cc.services;

import org.iitbact.cc.entities.AdminUser;
import org.iitbact.cc.entities.Area;
import org.iitbact.cc.exceptions.CovidControlException;
import org.iitbact.cc.repository.AreaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AreaService {

    private final AreaRepository areaRepository;
    private final UserServices userService;
    
    public AreaService(AreaRepository areaRepository,UserServices userService) {
        this.areaRepository = areaRepository;
        this.userService=userService;
    }

    public List<Area> listAreas(String uid) throws CovidControlException{
    	AdminUser user=userService.profile(uid);    			
        return areaRepository.findByRegion(user.getRegion());
    }

}
