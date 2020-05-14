package org.iitbact.cc.repository;

import java.util.List;
import java.util.Optional;

import org.iitbact.cc.entities.FacilityLink;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FacilityLinkRepository extends JpaRepository<FacilityLink, Integer> {
    /**
     * Checks if a link exists between two facilities
     */
    Boolean existsBySourceFacilityIdAndMappedFacilityIdAndCovidFacilityType(Integer sourceFacilityId, Integer mappedFacilityId, String covidFacilityType);

    /**
     * Returns a link between two facilities else return Optional.empty()
     */
    List<FacilityLink> findAllBySourceFacilityIdAndMappedFacilityId(Integer sourceFacilityId, Integer mappedFacilityId);

    /**
     * Returns all the facility links associated with the source facility
     */
    List<FacilityLink> getAllBySourceFacilityIdAndCovidFacilityType(Integer sourceFacilityId, String covidFacilityType);


    /**
     * Checks if any entry with the sourceFacility exists or not
     */
    Boolean existsBySourceFacilityId(Integer sourceFacilityId);
}
