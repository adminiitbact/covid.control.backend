package org.iitbact.cc.repository;


import java.util.List;

import org.iitbact.cc.dto.FacilityBedTimelineDto;
import org.iitbact.cc.entities.Ward;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface FacilityBedsTimelineRepository extends JpaRepository<Ward, Integer>, JpaSpecificationExecutor<Ward> {
	@Query(value = "SELECT new org.iitbact.cc.dto.FacilityBedTimelineDto(" +
            "SUM(w.totalBeds), " +
            "SUM(w.totalBeds)-SUM(w.availableBeds)," +
            "date(w.creationtime)) " +
            "FROM Ward w " +
            "WHERE w.covidWard = 1 " +
            "and w.active = 1 group by date(w.creationtime)")
	List<FacilityBedTimelineDto> findAllTimelineFacilities();
	
	@Query(value = "SELECT new org.iitbact.cc.dto.FacilityBedTimelineDto(" +
            "SUM(w.totalBeds), " +
            "SUM(w.totalBeds)-SUM(w.availableBeds)," +
            "date(w.creationtime)) " +
            "FROM Ward w " +
            "WHERE w.active = 1 " +
            "and w.covidWard = 1 and w.facilityId = ?1 group by date(w.creationtime)")
	List<FacilityBedTimelineDto> findTimelineForFacility(Integer facilityId);
}


