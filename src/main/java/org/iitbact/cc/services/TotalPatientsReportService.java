package org.iitbact.cc.services;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.iitbact.cc.entities.AdminUser;
import org.iitbact.cc.exceptions.CovidControlException;
import org.iitbact.cc.requests.CommonReportCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class TotalPatientsReportService {

	private static final Logger LOGGER = LoggerFactory.getLogger(PatientDischargeReportService.class);

	@PersistenceContext
	private EntityManager em;

	private final UserServices userService;

	public TotalPatientsReportService(UserServices userService) {
		this.userService = userService;
	}

	public List<String[]> fetchSumIsolationPatientList(String uid, CommonReportCriteria commonReportCriteria)
			throws CovidControlException {

		System.out.println("fetchSumIsolationPatientList  start");
		LOGGER.debug("fetchSumIsolationPatientList start");

		AdminUser user = userService.profile(uid);

		List<String[]> entries = new ArrayList<>();

		entries.add(new String[] { "16. Sum Isolation Total Patient (Hospital Wise)" });
		entries.add(new String[] { "" });
		entries.add(new String[] { "District/Corpoation", commonReportCriteria.getJurisdiction(), "From date",
				commonReportCriteria.getStartDate().toString(), "To Date",
				commonReportCriteria.getStartDate().toString() });
		entries.add(new String[] { "" });

		entries.add(new String[] { "#", "Facility Name", "Patients Admitted", "Patients Discharged" });

		String qlString = "select admission.name, admission.admission_count, discharged.discharged_count " + "from ( "
				+ "select  facility.name, count(distinct patients.name) as admission_count "
				+ "from patients patients  inner join patient_history history on history.patient_id=patients.patient_id  "
				+ "inner join facilities facility on history.facility_id=facility.facility_id "
				+ "where  facility.jurisdiction = ?" + "group by  facility.name " + ") admission " + "LEFT join " + "("
				+ "select  facility.name, count(discharged.creation_time) as discharged_count "
				+ "from patients patients  inner join patient_discharged discharged on discharged.patient_id=patients.patient_id  "
				+ "inner join facilities facility on discharged.facility_id=facility.facility_id  "
				+ "where discharged.reason=\"DISCHARGED\" and facility.jurisdiction = ?" + "group by facility.name "
				+ ") discharged " + "on admission.name=discharged.name";

		Query query = em.createNativeQuery(qlString);
		query.setParameter(1, commonReportCriteria.getJurisdiction());
		query.setParameter(2, commonReportCriteria.getJurisdiction());
		List<Object[]> resultList = query.getResultList();

		Integer dischargedCount;
		Integer admittedCount;
		Integer totalDischargedCount = 0;
		Integer totalAdmittedCount = 0;
		// Add the data rows.
		for (Object[] p : resultList) {
			admittedCount = p[1] == null ? 0 : Integer.parseInt(p[1].toString());
			dischargedCount = p[2] == null ? 0 : Integer.parseInt(p[2].toString());

			totalAdmittedCount += admittedCount;
			totalDischargedCount += dischargedCount;

			entries.add(new String[] { "", p[0].toString(), admittedCount.toString(), dischargedCount.toString() });
		}
		entries.add(new String[] { "Total", "-", totalAdmittedCount.toString(), totalDischargedCount.toString() });

		System.out.println("fetchSumIsolationPatientList  end");
		LOGGER.debug("fetchSumIsolationPatientList end");

		return entries;
	}
}