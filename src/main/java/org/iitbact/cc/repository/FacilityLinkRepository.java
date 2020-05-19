package org.iitbact.cc.repository;

import java.util.List;

import org.iitbact.cc.dto.LinkCount;
import org.iitbact.cc.entities.FacilityLink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

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

    @Query(value = "SELECT new org.iitbact.cc.dto.LinkCount(f.sourceFacilityId," +
            "SUM( CASE WHEN (f.covidFacilityType = 'DCH') THEN 1 else 0 END), " +
            "SUM( CASE WHEN (f.covidFacilityType = 'DCHC') THEN 1 else 0 END), " +
            "SUM( CASE WHEN (f.covidFacilityType = 'CCC') THEN 1 else 0 END)) " +
            "FROM FacilityLink f " +
            "WHERE f.sourceFacilityId = ?1")
    LinkCount findLinkCount(Integer sourceFacility);

    @Query(value = "SELECT new org.iitbact.cc.dto.LinkCount(f.sourceFacilityId," +
            "SUM( CASE WHEN (f.covidFacilityType = 'DCH') THEN 1 else 0 END), " +
            "SUM( CASE WHEN (f.covidFacilityType = 'DCHC') THEN 1 else 0 END), " +
            "SUM( CASE WHEN (f.covidFacilityType = 'CCC') THEN 1 else 0 END)) " +
            "FROM FacilityLink f " +
            "WHERE f.sourceFacilityId in ?1 " +
            "GROUP BY f.sourceFacilityId")
    List<LinkCount> findLinkCount(List<Integer> sourceFacilities);
}
