package org.iitbact.cc.services;


import java.util.List;

import org.iitbact.cc.dto.FacilityBedTimelineDto;
import org.iitbact.cc.exceptions.CovidControlErpError;
import org.iitbact.cc.exceptions.CovidControlErrorCode;
import org.iitbact.cc.exceptions.CovidControlErrorMsg;
import org.iitbact.cc.exceptions.CovidControlException;
import org.iitbact.cc.repository.FacilityBedsTimelineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class FacilityBedsTimelineService {
	
	
	@Autowired
	private  FacilityBedsTimelineRepository facilitybedRepository;
	
	public List<FacilityBedTimelineDto> fetchAllBedFacilities()  {
		log.info("fetch all bed facilities");
		return facilitybedRepository.findAllTimelineFacilities();
		
	}
	
	public List<FacilityBedTimelineDto> fetchBedTimelineForFacility(Integer facilityId) 
	  throws CovidControlException  {		
		
		log.info("fetching all bed facilities");
		List<FacilityBedTimelineDto> dto = facilitybedRepository.findTimelineForFacility(facilityId);
		if(dto == null && dto.size() == 0){
			throw new CovidControlException(new CovidControlErpError(CovidControlErrorCode.FACILITY_DOES_NOT_EXIST,
				CovidControlErrorMsg.FACILITY_DOES_NOT_EXIST));
		}
		return dto;
		
	}

}
