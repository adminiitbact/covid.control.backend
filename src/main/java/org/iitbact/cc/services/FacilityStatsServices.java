package org.iitbact.cc.services;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.iitbact.cc.dto.FacilityBedsStatsDto;
import org.iitbact.cc.dto.FacilityBedsStatsOverviewDto;
import org.iitbact.cc.entities.AdminUser;
import org.iitbact.cc.exceptions.CovidControlException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.NoArgsConstructor;


@Service
@NoArgsConstructor
public class FacilityStatsServices {

	@PersistenceContext
	private EntityManager em;
	
	@Autowired
	private UserServices userServices;
	
	@Autowired
	private ApiValidationService validationService;
	
	public List<FacilityBedsStatsDto> getBedsStats(String uid,int facilityId) throws CovidControlException {
		validationService.facilityValidation(uid, facilityId);
		Query q = em.createNativeQuery("SELECT y.facility_id, y.severity, y.covid_status, SUM(y.total_beds), SUM(y.available_beds) FROM hospitaldb.wards y\n" +
				"WHERE y.facility_id = :fId AND y.covid_ward = 1 GROUP By y.severity, y.covid_status").setParameter("fId", facilityId);
		List<Object[]> data = q.getResultList();
		List l = new ArrayList<FacilityBedsStatsDto>();

		for (Object[] a : data) {
			l.add(new FacilityBedsStatsDto(((Integer) a[0]).intValue(), (String) a[1], (String) a[2],((BigDecimal) a[3]).longValue(), ((BigDecimal) a[4]).longValue()));

		}

		return l;

	}

	public List<FacilityBedsStatsDto> getBedsStatsAll(String uid) throws CovidControlException {
		AdminUser user= userServices.profile(uid);
		Query q = em.createNativeQuery("SELECT y.severity, y.covid_status, SUM(y.total_beds), SUM(y.available_beds)  FROM hospitaldb.wards y INNER JOIN hospitaldb.facilities f ON y.facility_id=f.facility_id  WHERE y.covid_ward = 1 and f.region=:region GROUP By y.severity, y.covid_status").setParameter("region", user.getRegion());;
		List<Object[]> data = q.getResultList();
		List l = new ArrayList<FacilityBedsStatsDto>();

		for (Object[] a : data) {
			l.add(new FacilityBedsStatsDto(0, (String) a[0], (String) a[1],((BigDecimal) a[2]).longValue(), ((BigDecimal) a[3]).longValue()));
		}
		return l;

	}

	public List<FacilityBedsStatsOverviewDto> getBedsStatsOverview(String uid,int facilityId) throws CovidControlException {
		validationService.facilityValidation(uid, facilityId);
		Query q = em.createNativeQuery("SELECT w.facility_id, f.government_hospital, SUM(w.total_beds), SUM(CASE WHEN w.covid_ward = 1 THEN w.total_beds ELSE 0 END), SUM(w.available_beds), SUM(CASE WHEN w.covid_ward = 1 THEN w.available_beds ELSE 0 END)  FROM hospitaldb.wards w , hospitaldb.facilities f WHERE w.facility_id = :fId AND w.facility_id = f.facility_id").setParameter("fId", facilityId);;
		List<Object[]> data = q.getResultList();
		List l = new ArrayList<FacilityBedsStatsOverviewDto>();
		for (Object[] a : data) {
			l.add(new FacilityBedsStatsOverviewDto(((Integer) a[0]).intValue(),((Byte) a[1]).intValue(),((BigDecimal) a[2]).longValue(), ((BigDecimal) a[3]).longValue(),((BigDecimal) a[4]).longValue(), ((BigDecimal) a[5]).longValue()));
		}

		return l;

	}


	public List<FacilityBedsStatsOverviewDto> getBedsStatsOverviewAll(String uid) throws CovidControlException {
		AdminUser user=userServices.profile(uid);
		Query q = em.createNativeQuery("SELECT f.government_hospital,SUM(w.total_beds), SUM(CASE WHEN w.covid_ward = 1 THEN w.total_beds ELSE 0 END), SUM(w.available_beds), SUM(CASE WHEN w.covid_ward = 1 THEN w.available_beds ELSE 0 END)  FROM hospitaldb.wards w , hospitaldb.facilities f where f.region=:region GROUP BY f.government_hospital").setParameter("region", user.getRegion());
		List<Object[]> data = q.getResultList();
		List l = new ArrayList<FacilityBedsStatsOverviewDto>();

		for (Object[] a : data) {

			l.add(new FacilityBedsStatsOverviewDto(0, ((Byte) a[0]).intValue(), ((BigDecimal) a[1]).longValue(), ((BigDecimal) a[2]).longValue(),((BigDecimal) a[3]).longValue(), ((BigDecimal) a[4]).longValue()));

		}

		return l;

	}
}
