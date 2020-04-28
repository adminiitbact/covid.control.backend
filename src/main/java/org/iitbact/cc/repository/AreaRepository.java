package org.iitbact.cc.repository;

import org.iitbact.cc.entities.Area;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AreaRepository extends JpaRepository<Area, Integer>,JpaSpecificationExecutor<Area> {
}