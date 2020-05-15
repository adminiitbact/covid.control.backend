package org.iitbact.cc.repository;

import org.iitbact.cc.dto.AvailabilityStatus;
import org.iitbact.cc.dto.PatientStatsDto;
import org.iitbact.cc.entities.Patient;
import org.iitbact.cc.entities.PatientLiveStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PatientLiveStatusRepository extends JpaRepository<PatientLiveStatus, Integer>, JpaSpecificationExecutor<PatientLiveStatus> {


    @Query(value = "SELECT new org.iitbact.cc.dto.PatientStatsDto( " +
            "p.facility.facilityId ," +
            "COUNT(*) )  " +
            "FROM PatientLiveStatus p   " +
            "WHERE p.facility.facilityId in ?1  " )

    List<PatientStatsDto> getCount(@Param("facilityId") int facilityId) ;
}
