package org.iitbact.cc.repository;

import org.iitbact.cc.entities.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PatientRepository extends JpaRepository<Patient, Integer>, JpaSpecificationExecutor<Patient> {

}
