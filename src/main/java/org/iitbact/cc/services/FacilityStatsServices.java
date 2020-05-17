package org.iitbact.cc.services;

import lombok.extern.slf4j.Slf4j;
import org.iitbact.cc.dto.*;
import org.iitbact.cc.repository.PatientLiveStatusRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;


@Slf4j
@Service
public class FacilityStatsServices {

	@PersistenceContext
	private EntityManager em;

	public FacilityStatsServices() {

	}

	public List<FacilityBedsStatsDto> getBedsStats(int facilityId) {
		Query q = em.createNativeQuery("SELECT y.facility_id, y.severity, y.covid_status, SUM(y.total_beds), SUM(y.available_beds) FROM hospitaldb.wards y\n" +
				"WHERE y.facility_id = " +  facilityId  + " AND y.covid_ward = 1 GROUP By y.severity, y.covid_status");
		List<Object[]> data = q.getResultList();
		List l = new ArrayList<FacilityBedsStatsDto>();

		for (Object[] a : data) {

			l.add(new FacilityBedsStatsDto(((Integer) a[0]).intValue(), (String) a[1], (String) a[2],((BigDecimal) a[3]).longValue(), ((BigDecimal) a[4]).longValue()));

		}

		return l;

	}

	public List<FacilityBedsStatsDto> getBedsStatsAll() {

		Query q = em.createNativeQuery("SELECT y.severity, y.covid_status, SUM(y.total_beds), SUM(y.available_beds)  FROM hospitaldb.wards y\n" +
				"WHERE y.covid_ward = 1 GROUP By y.severity, y.covid_status");
		List<Object[]> data = q.getResultList();
		List l = new ArrayList<FacilityBedsStatsDto>();

		for (Object[] a : data) {

			l.add(new FacilityBedsStatsDto(0, (String) a[0], (String) a[1],((BigDecimal) a[2]).longValue(), ((BigDecimal) a[3]).longValue()));

		}

		return l;

	}

	public List<FacilityBedsStatsOverviewDto> getBedsStatsOverview(int facilityId) {
		Query q = em.createNativeQuery("SELECT w.facility_id, f.government_hospital, SUM(w.total_beds), SUM(CASE WHEN w.covid_ward = 1 THEN w.total_beds ELSE 0 END), SUM(w.available_beds), SUM(CASE WHEN w.covid_ward = 1 THEN w.available_beds ELSE 0 END)  FROM hospitaldb.wards w , hospitaldb.facilities f WHERE w.facility_id = " + facilityId + " AND w.facility_id = f.facility_id");
		List<Object[]> data = q.getResultList();
		List l = new ArrayList<FacilityBedsStatsOverviewDto>();

		for (Object[] a : data) {

			l.add(new FacilityBedsStatsOverviewDto(((Integer) a[0]).intValue(),((Byte) a[1]).intValue(),((BigDecimal) a[2]).longValue(), ((BigDecimal) a[3]).longValue(),((BigDecimal) a[4]).longValue(), ((BigDecimal) a[5]).longValue()));

		}

		return l;

	}


	public List<FacilityBedsStatsOverviewDto> getBedsStatsOverviewAll() {
		Query q = em.createNativeQuery("SELECT f.government_hospital,SUM(w.total_beds), SUM(CASE WHEN w.covid_ward = 1 THEN w.total_beds ELSE 0 END), SUM(w.available_beds), SUM(CASE WHEN w.covid_ward = 1 THEN w.available_beds ELSE 0 END)  FROM hospitaldb.wards w , hospitaldb.facilities f GROUP BY f.government_hospital");
		List<Object[]> data = q.getResultList();
		List l = new ArrayList<FacilityBedsStatsOverviewDto>();

		for (Object[] a : data) {

			l.add(new FacilityBedsStatsOverviewDto(0, ((Byte) a[0]).intValue(), ((BigDecimal) a[1]).longValue(), ((BigDecimal) a[2]).longValue(),((BigDecimal) a[3]).longValue(), ((BigDecimal) a[4]).longValue()));

		}

		return l;

	}


}
