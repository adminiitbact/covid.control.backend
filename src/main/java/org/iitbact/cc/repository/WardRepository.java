package org.iitbact.cc.repository;

import java.util.List;

import org.iitbact.cc.dto.AvailabilityStatus;
import org.iitbact.cc.entities.Ward;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface WardRepository extends JpaRepository<Ward, Integer>,JpaSpecificationExecutor<Ward> {

    @Query(value = "SELECT new org.iitbact.cc.dto.AvailabilityStatus(w.facilityId," +
            " w.severity," +
            "SUM(w.totalBeds), " +
            "SUM(w.availableBeds), " +
            "SUM(w.ventilators), " +
            "SUM(w.ventilatorsOccupied)) " +
            "FROM Ward w " +
            "WHERE w.facilityId in ?1 " +
            "GROUP BY w.facilityId,w.severity")
    List<AvailabilityStatus> getAvailabilityStatus(@Param("facilities") List<Integer> facilities);

}