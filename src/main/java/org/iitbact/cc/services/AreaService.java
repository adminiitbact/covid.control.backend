package org.iitbact.cc.services;

import org.iitbact.cc.entities.Area;

import org.iitbact.cc.repository.AreaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AreaService {

    private final AreaRepository areaRepository;

    public AreaService(AreaRepository areaRepository) {
        this.areaRepository = areaRepository;
    }

    public List<Area> listAreas(){
        return areaRepository.findAll();
    }

}
