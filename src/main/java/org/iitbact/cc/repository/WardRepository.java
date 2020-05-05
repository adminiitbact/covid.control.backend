package org.iitbact.cc.repository;

import java.util.List;

import org.iitbact.cc.dto.AvailabilityStatus;
import org.iitbact.cc.entities.Ward;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface WardRepository extends JpaRepository<Ward, Integer>,JpaSpecificationExecutor<Ward> {

    @Query(value = "select new org.iitbact.cc.dto.AvailabilityStatus(w.facilityId," +
            " w.severity," +
            "sum(w.totalBeds), " +
            "sum(w.availableBeds), " +
            "sum(w.ventilators), " +
            "sum(w.ventilatorsOccupied)) " +
            "from Ward w " +
            "where w.facilityId in ?1 " +
            "group by w.facilityId,w.severity")
    List<AvailabilityStatus> getAvailabilityStatus(@Param("facilities") List<Integer> facilities);
    
}