package org.iitbact.cc.repository;

import org.iitbact.cc.entities.Facility;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface FacilityRepository extends JpaRepository<Facility, Integer>,JpaSpecificationExecutor<Facility> {
}