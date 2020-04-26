package org.iitbact.cc.services;

import lombok.extern.slf4j.Slf4j;
import org.iitbact.cc.constants.LinkingStatus;
import org.iitbact.cc.entities.Facility;
import org.iitbact.cc.entities.FacilityLink;
import org.iitbact.cc.exceptions.CovidControlErpError;
import org.iitbact.cc.exceptions.CovidControlErrorCode;
import org.iitbact.cc.exceptions.CovidControlErrorMsg;
import org.iitbact.cc.exceptions.CovidControlException;
import org.iitbact.cc.repository.FacilityLinkRepository;
import org.iitbact.cc.repository.FacilityRepository;
import org.iitbact.cc.requests.LinkFacilitiesRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FacilityServices {

    private static final Supplier<CovidControlException> facilityDoesNotExistException =
            () -> new CovidControlException(new CovidControlErpError(CovidControlErrorCode.FACILITY_DOES_NOT_EXIST, CovidControlErrorMsg.FACILITY_DOES_NOT_EXIST));

    private final FacilityRepository facilityRepository;
    private final FacilityLinkRepository facilityLinkRepository;

    public FacilityServices(FacilityRepository facilityRepository, FacilityLinkRepository facilityLinkRepository) {
        this.facilityRepository = facilityRepository;
        this.facilityLinkRepository = facilityLinkRepository;
    }

    public Facility createFacility(Facility facility) {
        log.info("Create Facility Request");
        log.debug("Create Facility Request - {}", facility.toString());
        facilityRepository.save(facility);
        log.info("Facilty created successfully with id {}", facility.getFacilityId());
        return facility;
    }

    public Facility editFacility(int facilityId, Facility facilityRequest) throws CovidControlException {
        log.info("Edit Facility Request - facility Id {}", facilityId);
        log.debug("Facility Edit Request Content {}", facilityRequest.toString());

        Facility facility = facilityRepository.findById(facilityId)
                .orElseThrow(facilityDoesNotExistException);
        facility.copy(facilityRequest);
        facilityRepository.save(facility);
        log.info("Facility {} updated successfully",facilityId);
        return facility;
    }

    public Boolean addFacilityProfileData(int facilityId) throws CovidControlException {
        log.info("Add Facility Profile Data Request - Facility Id {}", facilityId);

        Facility facility = facilityRepository.findById(facilityId)
                .orElseThrow(facilityDoesNotExistException);

        //TODO logic to add facility

        facilityRepository.save(facility);
        return true;
    }

    public Facility fetchFacilityData(int facilityId) throws CovidControlException {
        log.info("Fetch Facility Data request {}", facilityId);

        return facilityRepository.findById(facilityId)
                .orElseThrow(facilityDoesNotExistException);
    }


    public Boolean linkFacilities(int facilityId, LinkFacilitiesRequest linkFacilitiesRequest) throws CovidControlException {
        log.info("Link Facilities Request. Facility Id - {}, Facilities to be linked {}, Facilities to be de-linked {}",
                facilityId,
                linkFacilitiesRequest.getFacilityLinks().stream().filter(link -> link.getLinkingStatus() == LinkingStatus.LINK)
                        .map(LinkFacilitiesRequest.Link::getFacilityId).collect(Collectors.toList()),
                linkFacilitiesRequest.getFacilityLinks().stream().filter(link -> link.getLinkingStatus() == LinkingStatus.UNLINK)
                        .map(LinkFacilitiesRequest.Link::getFacilityId).collect(Collectors.toList()));

        Facility facility = facilityRepository.findById(facilityId)
                .orElseThrow(facilityDoesNotExistException);
        for(LinkFacilitiesRequest.Link link :  linkFacilitiesRequest.getFacilityLinks()){
            if(link.getLinkingStatus() == LinkingStatus.LINK){
                Boolean linkAlreadyExists = facilityLinkRepository.existsBySourceFacilityIdAndMappedFacilityId(facilityId, link.getFacilityId());
                if(!linkAlreadyExists) {
                    if(facilityRepository.existsById(link.getFacilityId())) {
                        FacilityLink facilityLink = FacilityLink.builder()
                                .sourceFacilityId(facilityId)
                                .mappedFacilityId(link.getFacilityId())
                                .build();
                        facilityLinkRepository.save(facilityLink);
                        log.info("Link created successfully for {} and {}", facilityId, link.getFacilityId());
                    }
                    else{
                        log.error("No facility exists by id {}. Invalid link.", link.getFacilityId());
                    }
                }
                else{
                    log.error("Link already exists between {} and {}", facilityId, link.getFacilityId());
                }
            }
            else if(link.getLinkingStatus() == LinkingStatus.UNLINK){
                Optional<FacilityLink> facilityLink = facilityLinkRepository.getBySourceFacilityIdAndMappedFacilityId(facilityId, link.getFacilityId());
                if(facilityLink.isPresent()){
                    facilityLinkRepository.delete(facilityLink.get());
                    log.info("Link deleted successfully for {} and {}", facilityId, link.getFacilityId());
                }
                else{
                    log.error("Link does not exist between {} and {}", facilityId, link.getFacilityId());
                }
            }
        }
        return true;
    }

    public List<Facility> getLinkedFacilities(int facilityId) throws CovidControlException {
        log.info("Get Linked Facilities - facility id {}", facilityId);

        Facility facility = facilityRepository.findById(facilityId)
                .orElseThrow(facilityDoesNotExistException);
        return facilityLinkRepository.getAllBySourceFacilityId(facilityId)
                .stream()
                .map(FacilityLink::getMappedFacilty)
                .collect(Collectors.toList());
    }
}
