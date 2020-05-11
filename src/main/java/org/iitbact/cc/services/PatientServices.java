package org.iitbact.cc.services;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.iitbact.cc.constants.Constants;
import org.iitbact.cc.dto.PatientDto;
import org.iitbact.cc.entities.AdminUser;
import org.iitbact.cc.entities.Facility;
import org.iitbact.cc.entities.Patient;
import org.iitbact.cc.entities.PatientLiveStatus;
import org.iitbact.cc.exceptions.CovidControlException;
import org.iitbact.cc.requests.PatientSearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class PatientServices {

	@PersistenceContext
	private EntityManager em;

	private final UserServices userServices;

	public PatientServices(UserServices userServices) {
		this.userServices = userServices;
	}

	public List<PatientDto> getPatients(int pageNo, String uid, PatientSearchCriteria searchCriteria)
			throws CovidControlException {
		AdminUser user = userServices.profile(uid);

		Pageable pageRequest = PageRequest.of(pageNo - 1, Constants.PAGE_SIZE);

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<PatientDto> cq = cb.createQuery(PatientDto.class);
		Root<PatientLiveStatus> root = cq.from(PatientLiveStatus.class);
		
		Join<Patient, PatientLiveStatus> join1 =  root.join("patient", JoinType.INNER);
		Join<Facility, PatientLiveStatus> join2 = root.join("facility", JoinType.INNER);

		cq.select(cb.construct(PatientDto.class, join1.get("name"),
				join1.get("patientId").alias("patientId"),
				join1.get("gender"), 
				join1.get("age"),
				join1.get("locality"),
				join1.get("goiCovidId").alias("goiCovidId"), 
				root.get("severity"),
				root.get("testStatus").alias("testStatus"),
				join2.get("name").alias("facilityName"),
				join2.get("area"), 
				join2.get("covidFacilityType").alias("covidFacilityType")));

		List<Predicate> predicates = new ArrayList<>();

		if (searchCriteria.getName() != null && !searchCriteria.getName().isEmpty()) {
			Predicate predicateForName = cb.like(cb.lower(join1.get("name")),
					"%" + searchCriteria.getName().toLowerCase() + "%");
			predicates.add(predicateForName);
		}

		if (searchCriteria.getAreas() != null && !searchCriteria.getAreas().isEmpty()) {
			predicates.add(join2.get("area").in(searchCriteria.getAreas()));
		}
		
		if (searchCriteria.getFacilityIds() != null && !searchCriteria.getFacilityIds().isEmpty()) {
			predicates.add(join2.get("facilityId").in(searchCriteria.getFacilityIds()));
		}

		if (searchCriteria.getCovidFacilityType() != null && !searchCriteria.getCovidFacilityType().isEmpty()) {
			predicates.add(join2.get("covidFacilityType").in(searchCriteria.getCovidFacilityType()));
		}

		if (searchCriteria.getAge() != 0) {
			predicates.add(cb.equal(join1.get("age"), searchCriteria.getAge()));
		}
		if (searchCriteria.getSeverity() != null) {
			predicates.add(cb.equal(root.get("severity"), searchCriteria.getSeverity().toString()));
		}

		if (searchCriteria.getGender() != null) {
			predicates.add(cb.equal(join1.get("gender"), searchCriteria.getGender()));
		}
		
		Predicate predicateForRegion = cb.equal(join2.get("region"),  user.getRegion());
		predicates.add(predicateForRegion);
		
		cq.where(predicates.toArray(new Predicate[predicates.size()]));

		TypedQuery<PatientDto> query = em.createQuery(cq);

		int totalRows = query.getResultList().size();

		query.setFirstResult(pageRequest.getPageNumber()* pageRequest.getPageSize());
		query.setMaxResults(pageRequest.getPageSize());

		Page<PatientDto> result = new PageImpl<PatientDto>(query.getResultList(), pageRequest, totalRows);

		return result.toList();
	}

}
