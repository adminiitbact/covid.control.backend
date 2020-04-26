package org.iitbact.cc.services;

import org.iitbact.cc.entities.Facility;
import org.iitbact.cc.exceptions.CovidControlErpError;
import org.iitbact.cc.exceptions.CovidControlErrorCode;
import org.iitbact.cc.exceptions.CovidControlErrorMsg;
import org.iitbact.cc.exceptions.CovidControlException;
import org.iitbact.cc.repository.FacilityRepository;
import org.springframework.stereotype.Service;

@Service
public class FacilityServices {

    private final FacilityRepository facilityRepository;

    public FacilityServices(FacilityRepository facilityRepository) {
        this.facilityRepository = facilityRepository;
    }

    public Facility createFacility(String uid, Facility facility) {
        facilityRepository.save(facility);
        return facility;
    }

    public Facility editFacility(String uid, int facilityId, Facility facilityRequest) throws CovidControlException {
        Facility facility = facilityRepository.findById(facilityId)
                .orElseThrow(() -> new CovidControlException(new CovidControlErpError(CovidControlErrorCode.INVALID_INPUT, CovidControlErrorMsg.INVALID_INPUT)));
        facility.copy(facilityRequest);
        facilityRepository.save(facility);
        return facility;
    }

    public Boolean addFacilityProfileData(String uid,int facilityId) {
        Facility facility = facilityRepository.findById(facilityId).get();

        //TODO logic to add facility
        facilityRepository.save(facility);
        return true;
    }

    public Facility fetchFacilityData(String uid,int facilityId) {
        return facilityRepository.findById(facilityId).get();
    }

}
