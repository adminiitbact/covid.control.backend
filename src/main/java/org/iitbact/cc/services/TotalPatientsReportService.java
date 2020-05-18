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

		entries.add(new String[] { "Sum Isolation Total Patient (Hospital Wise)" });
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
	
	
	public List<String[]> fetchCovidFacilitiesSummaryList(String uid, CommonReportCriteria commonReportCriteria)
			throws CovidControlException {

		System.out.println("fetchCovidFacilitiesSummaryList  start");
		System.out.println("uid ::"+uid);
		LOGGER.debug("fetchCovidFacilitiesSummaryList start");

		AdminUser user = userService.profile(uid);

		List<String[]> entries = new ArrayList<>();

		entries.add(new String[] { "COVID Facilities Summary" });
		entries.add(new String[] { "" });
		entries.add(new String[] { "District/Corpoation", commonReportCriteria.getJurisdiction(), "From date",
				commonReportCriteria.getStartDate().toString(), "To Date",
				commonReportCriteria.getEndDate().toString() });
		//entries.add(new String[] { "" });

		entries.add(new String[] {
				"Jurisdiction",
				"COVID Facility Type",
				"Numbers of facilites" ,
				"Maximum  Capacity",
				"Mild Cases Capacity",
				"Mild Cases Occupancy",
				"Moderate Cases Capacity",
				"Moderate Cases Occupancy",
				"Severe Cases Capacity",
				"Severe Cases Occupancy",
				"Total COVID Capacity",
				"Total COVID Occupancy"
				});

		String query1=	" SELECT  f.jurisdiction,count(f.covid_facility_type) no_of_facilities,f.covid_facility_type,\n" +
				"  sum(w.total_beds) as maximum_capacity,\n" +
				"  (\n" +
				"  Select sum(total_beds)Cases_Capacity from    facilities f \n" +
				" inner join wards w on f.facility_id=w.facility_id\n" +
				" where severity='severe' \n" +
				" and f.jurisdiction=? \n" +
				" group by severity,f.jurisdiction \n" +
				" )severe_cases_capacity,\n" +
				" (\n" +
				"  Select sum(total_beds)Cases_Capacity from    facilities f \n" +
				" inner join wards w on f.facility_id=w.facility_id\n" +
				" where severity='MILD' \n" +
				" and f.jurisdiction=? \n" +
				" group by severity,f.jurisdiction \n" +
				" )mild_cases_capacity,\n" +
				" (\n" +
				"  Select sum(total_beds)Cases_Capacity from    facilities f \n" +
				" inner join wards w on f.facility_id=w.facility_id\n" +
				" where severity='moderate' \n" +
				" and f.jurisdiction=? \n" +
				" group by severity,f.jurisdiction \n" +
				" )moderate_cases_capacity,\n" +
				"   (\n" +
				"  Select sum(available_beds)Cases_Capacity from    facilities f \n" +
				" inner join wards w on f.facility_id=w.facility_id\n" +
				" where severity='severe' \n" +
				" and f.jurisdiction=? \n" +
				" group by severity,f.jurisdiction \n" +
				" )severe_cases_occupancy,\n" +
				" (\n" +
				"  Select sum(available_beds)Cases_Capacity from    facilities f \n" +
				" inner join wards w on f.facility_id=w.facility_id\n" +
				" where severity='MILD' \n" +
				" and f.jurisdiction=? \n" +
				" group by severity,f.jurisdiction \n" +
				" )mild_cases_occupancy,\n" +
				" (\n" +
				"  Select sum(available_beds)Cases_Capacity from    facilities f \n" +
				" inner join wards w on f.facility_id=w.facility_id\n" +
				" where severity='moderate' \n" +
				" and f.jurisdiction=? \n" +
				" group by severity,f.jurisdiction \n" +
				" )moderate_cases_occupancy\n" +
				" from    facilities f \n" +
				"  inner join wards w on f.facility_id=w.facility_id\n" +
				" where f.jurisdiction=? \n" +
				" group by f.jurisdiction,covid_facility_type ;\n";

		System.out.println("qlString ====>"+query1);

		Query query = em.createNativeQuery(query1);
		query.setParameter(1, commonReportCriteria.getJurisdiction());
		query.setParameter(2, commonReportCriteria.getJurisdiction());
		query.setParameter(3, commonReportCriteria.getJurisdiction());
		query.setParameter(4, commonReportCriteria.getJurisdiction());
		query.setParameter(5, commonReportCriteria.getJurisdiction());
		query.setParameter(6, commonReportCriteria.getJurisdiction());
		query.setParameter(7, commonReportCriteria.getJurisdiction());

		List<Object[]> resultList = query.getResultList();

		String totalCovidCapacity="0";
		String totalCovidOccupancy="0";
		// Add the data rows.
		for (Object[] p : resultList) {

			String jurisdiction=String.valueOf(p[0]);
			String covidFacilityType=String.valueOf(p[2]);
			String totalNumbers=String.valueOf(p[1]);
			String maximumCapacity=String.valueOf(p[3] !=null ?p[3]:"0");
			String severeCaseCapacity=String.valueOf(p[4] !=null ?p[4]:"0");
			String mildCasesCapacity=String.valueOf(p[5] !=null ?p[5]:"0");
			String moderateCasesCapacity=String.valueOf(p[6] !=null ?p[6]:"0");
			String severeCasesOccupancy=String.valueOf(p[7] !=null ?p[7]:"0");
			String mildCasesOccupancy=String.valueOf(p[8] !=null ?p[8]:"0");;
			String moderateCasesOccupancy=String.valueOf(p[9] !=null ?p[9]:"0");
			Integer intTotalCovidCapacity=Integer.parseInt(String.valueOf(p[4]) !=null ?String.valueOf(p[4]) :"0")+
					Integer.parseInt(String.valueOf(p[5] !=null ?p[5]:"0"))+
					Integer.parseInt(String.valueOf(p[6] !=null ?p[6]:"0"));

			Integer intTotalCovidOccupancy=Integer.parseInt(String.valueOf(p[7] !=null ?p[7]:"0"))+
					Integer.parseInt(String.valueOf(p[8] !=null ?p[8]:"0"))+
					Integer.parseInt(String.valueOf(p[9] !=null ?p[9]:"0"));

			totalCovidCapacity=String.valueOf(intTotalCovidCapacity);
			totalCovidOccupancy=String.valueOf(intTotalCovidOccupancy);

			entries.add(new String[] { jurisdiction,
					covidFacilityType,
					totalNumbers,
					maximumCapacity,
					mildCasesCapacity,
					mildCasesOccupancy,
					moderateCasesCapacity,
					moderateCasesOccupancy,
					severeCaseCapacity,
					severeCasesOccupancy,
					totalCovidCapacity,
					totalCovidOccupancy

					 });
		}

		System.out.println("fetchSumIsolationPatientList  end");
		LOGGER.debug("fetchSumIsolationPatientList end");

		return entries;
	}
}