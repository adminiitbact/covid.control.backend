package org.iitbact.cc.services;

import lombok.extern.slf4j.Slf4j;
import org.iitbact.cc.constants.Constants;
import org.iitbact.cc.dto.PatientDto;
import org.iitbact.cc.dto.PatientStatsDto;
import org.iitbact.cc.entities.AdminUser;
import org.iitbact.cc.entities.Facility;
import org.iitbact.cc.entities.Patient;
import org.iitbact.cc.entities.PatientLiveStatus;
import org.iitbact.cc.exceptions.CovidControlException;
import org.iitbact.cc.repository.PatientLiveStatusRepository;
import org.iitbact.cc.requests.PatientSearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import org.iitbact.cc.repository.PatientLiveStatusRepository ;



@Slf4j
@Service
public class PatientLiveStatusServices {

	private final PatientLiveStatusRepository patientLiveStatusRepository;

	public PatientLiveStatusServices (PatientLiveStatusRepository p) {
		this.patientLiveStatusRepository = p ;
	}

	public List<PatientStatsDto> getPatientStats(int facilityId) {
		List<PatientStatsDto> number = patientLiveStatusRepository.getCount(facilityId);
        return number;
	}
}
