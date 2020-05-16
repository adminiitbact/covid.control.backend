package org.iitbact.cc.services;

import lombok.extern.slf4j.Slf4j;
import org.iitbact.cc.constants.Constants;
import org.iitbact.cc.dto.PatientDto;
import org.iitbact.cc.dto.PatientStatsAgeDto;
import org.iitbact.cc.dto.PatientStatsDto;
import org.iitbact.cc.dto.PatientStatsGenderDto;
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
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import org.iitbact.cc.repository.PatientLiveStatusRepository ;



@Slf4j
@Service
public class PatientLiveStatusServices {

	@PersistenceContext
	private EntityManager em;

	private final PatientLiveStatusRepository patientLiveStatusRepository;

	public PatientLiveStatusServices (PatientLiveStatusRepository p) {
		this.patientLiveStatusRepository = p ;
	}

	public List<PatientStatsDto> getPatientStats(int facilityId) {
		List<PatientStatsDto> number = patientLiveStatusRepository.getCount(facilityId);
        return number;
	}

	public List<PatientStatsGenderDto> getPatientStatsByGender(int facilityId) {


		Query q = em.createNativeQuery("SELECT x.facility_id, p.gender, COUNT(*), y.severity FROM hospitaldb.patients p, hospitaldb.patient_live_status x , hospitaldb.wards y\n" +
				"WHERE p.patient_id = x.patient_id AND x.ward_id = y.id AND x.facility_id = " + facilityId  + " GROUP BY y.severity, p.gender");
		List<Object[]> data = q.getResultList();
		List l = new ArrayList<PatientStatsGenderDto>();

		for (Object[] a : data) {

			l.add(new PatientStatsGenderDto(((Integer) a[0]).intValue(), (String) a[1],((BigInteger) a[2]).longValue(),(String) a[3]));

		}

		return l;
		//List<PatientStatsDto> number = patientLiveStatusRepository.getCount(facilityId);
		//return number;


	}


	public List<PatientStatsAgeDto> getPatientStatsByAge(int facilityId) {


		Query q = em.createNativeQuery("SELECT x.facility_id, y.severity, SUM(CASE WHEN p.age < 18 THEN 1 ELSE 0 END) AS A, SUM(CASE WHEN p.age BETWEEN 18 AND 44 THEN 1 ELSE 0 END) AS B,SUM(CASE WHEN p.age BETWEEN 45 AND 64 THEN 1 ELSE 0 END) AS C,\n" +
				"SUM(CASE WHEN p.age BETWEEN 65 AND 74 THEN 1 ELSE 0 END) AS D,\n" +
				"SUM(CASE WHEN p.age > 74 THEN 1 ELSE 0 END) AS E\n" +
				"from hospitaldb.patients p , hospitaldb.patient_live_status x , hospitaldb.wards y\n" +
				"WHERE p.patient_id = x.patient_id AND x.ward_id = y.id AND x.facility_id = " + facilityId + " \n" +
				"GROUP BY y.severity; \n");


		List<Object[]> data = q.getResultList();
		List l = new ArrayList<PatientStatsAgeDto>();

		for (Object[] a : data) {

			l.add(new PatientStatsAgeDto(((Integer) a[0]).intValue(), (String) a[1],((BigDecimal) a[2]).longValue(),((BigDecimal) a[3]).longValue(),((BigDecimal) a[4]).longValue(),((BigDecimal) a[5]).longValue(),((BigDecimal) a[6]).longValue() ));

		}

		return l;
		//List<PatientStatsDto> number = patientLiveStatusRepository.getCount(facilityId);
		//return number;


	}

	public List<PatientStatsDto> getPatientStatsAll() {
		List<PatientStatsDto> number = patientLiveStatusRepository.getCountAll();
		return number;
	}

	public List<PatientStatsGenderDto> getPatientStatsByGenderAll() {


		Query q = em.createNativeQuery("SELECT p.gender, COUNT(*), y.severity FROM hospitaldb.patients p, hospitaldb.patient_live_status x , hospitaldb.wards y\n" +
				"WHERE p.patient_id = x.patient_id AND x.ward_id = y.id  " + " GROUP BY y.severity, p.gender");
		List<Object[]> data = q.getResultList();
		List l = new ArrayList<PatientStatsGenderDto>();

		for (Object[] a : data) {

			l.add(new PatientStatsGenderDto(0, (String) a[0],((BigInteger) a[1]).longValue(),(String) a[2]));

		}

		return l;
		//List<PatientStatsDto> number = patientLiveStatusRepository.getCount(facilityId);
		//return number;


	}


	public List<PatientStatsAgeDto> getPatientStatsByAgeAll() {


		Query q = em.createNativeQuery("SELECT y.severity, SUM(CASE WHEN p.age < 18 THEN 1 ELSE 0 END) AS A, SUM(CASE WHEN p.age BETWEEN 18 AND 44 THEN 1 ELSE 0 END) AS B,SUM(CASE WHEN p.age BETWEEN 45 AND 64 THEN 1 ELSE 0 END) AS C,\n" +
				"SUM(CASE WHEN p.age BETWEEN 65 AND 74 THEN 1 ELSE 0 END) AS D,\n" +
				"SUM(CASE WHEN p.age > 74 THEN 1 ELSE 0 END) AS E\n" +
				"from hospitaldb.patients p , hospitaldb.patient_live_status x , hospitaldb.wards y\n" +
				"WHERE p.patient_id = x.patient_id AND x.ward_id = y.id  "  + " \n" +
				"GROUP BY y.severity; \n");


		List<Object[]> data = q.getResultList();
		List l = new ArrayList<PatientStatsAgeDto>();

		for (Object[] a : data) {

			l.add(new PatientStatsAgeDto( 0, (String) a[0],((BigDecimal) a[1]).longValue(),((BigDecimal) a[2]).longValue(),((BigDecimal) a[3]).longValue(),((BigDecimal) a[4]).longValue(),((BigDecimal) a[5]).longValue() ));

		}

		return l;
		//List<PatientStatsDto> number = patientLiveStatusRepository.getCount(facilityId);
		//return number;


	}


}
