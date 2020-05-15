package org.iitbact.cc.services;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.iitbact.cc.constants.ReportNames;
import org.iitbact.cc.dto.PatientDischargedDto;
import org.iitbact.cc.entities.AdminUser;
import org.iitbact.cc.entities.Facility;
import org.iitbact.cc.entities.Patient;
import org.iitbact.cc.entities.PatientDischarged;
import org.iitbact.cc.entities.PatientHistory;
import org.iitbact.cc.exceptions.CovidControlException;
import org.iitbact.cc.requests.CommonReportCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class PatientDischargeReportService {

	private static final Logger LOGGER = LoggerFactory.getLogger(PatientDischargeReportService.class);

	@PersistenceContext
	private EntityManager em;

	private final UserServices userService;

	public PatientDischargeReportService(UserServices userService) {
		this.userService = userService;
	}

	public List<String[]> fetchDailyPatientDischargeList(String uid, String reportName,
			CommonReportCriteria commonReportCriteria) throws CovidControlException {

		System.out.println("fetchDailyPatientDischargeList  start");
		LOGGER.debug("fetchDailyPatientDischargeList start");

		AdminUser user = userService.profile(uid);

		List<String[]> entries = new ArrayList<>();
		String testStatus;
		String reason;

		if (reportName.equals(ReportNames.DAILY_PATIENT_DISCHARGE_LIST)) {

			entries.add(new String[] { "20. Daily Patient Discharge List" });
			entries.add(new String[] { "" });
			entries.add(new String[] { "District/Corpoation", commonReportCriteria.getJurisdiction(), "From date",
					commonReportCriteria.getStartDate().toString(), "To Date",
					commonReportCriteria.getStartDate().toString() });

			entries.add(new String[] { "" });

			// Add header for the CSV file.
			entries.add(new String[] { "Sr.No.", "Name of Patient", "Age", "Sex", "Name of admitted Hospital",
					"Date of Positive test result", "Date of admission", "Date of Discharge" });

			testStatus = "POSITIVE";
			reason = "DISCHARGED";

		} else {

			entries.add(new String[] { "21. Daily Patient Death List" });
			entries.add(new String[] { "" });
			entries.add(new String[] { "District/Corpoation", commonReportCriteria.getJurisdiction(), "From date",
					commonReportCriteria.getStartDate().toString(), "To Date",
					commonReportCriteria.getStartDate().toString() });

			entries.add(new String[] { "" });

			// Add header for the CSV file.
			entries.add(new String[] { "Sr.No.", "Name of Deceased", "Age", "Sex", "Name of admitted Hospital",
					"Date of Positive test result", "Date of admission", "Date of Death" });

			testStatus = null;
			reason = "DECEASED";
		}

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<PatientDischargedDto> cq = cb.createQuery(PatientDischargedDto.class);
		Root<PatientDischarged> root = cq.from(PatientDischarged.class);

		Join<Patient, PatientDischarged> join1 = root.join("patient", JoinType.INNER);
		Join<Facility, PatientDischarged> join2 = root.join("facility", JoinType.INNER);
		Join<PatientHistory, PatientDischarged> join3 = root.join("patientHistory", JoinType.LEFT);

		Expression positiveDate = join3.get("creationTime");

		Expression<?>[] groupCondition = { join1.get("name"), join1.get("gender"), join1.get("age"), join2.get("name"),
				join1.get("creationTime"), root.get("creationTime") };

		cq.select(cb.construct(PatientDischargedDto.class, root.get("id"), join1.get("name").alias("patientName"),
				join1.get("gender"), join1.get("age"), join2.get("name").alias("hospitalName"),
				join1.get("creationTime").alias("dateOfAdmission"), root.get("creationTime").alias("dateOfDischarged"),
				// join3.get("creationTime").alias("dateOPositiveTestResult")
				cb.max(positiveDate).alias("dateOPositiveTestResult")));
		cq.groupBy(groupCondition);

		List<Predicate> predicates = new ArrayList<>();
		if (null != testStatus && !testStatus.isEmpty()) {
			predicates.add(cb.equal(root.get("testStatus"), testStatus));
		}
		if (null != reason && !reason.isEmpty()) {
			predicates.add(cb.equal(root.get("reason"), reason));
		}

		if (null != commonReportCriteria.getJurisdiction() && !commonReportCriteria.getJurisdiction().isEmpty()) {
			predicates.add(cb.equal(join2.get("jurisdiction"), commonReportCriteria.getJurisdiction()));
		}
		if (null != commonReportCriteria.getStartDate()) {

			predicates.add(cb.greaterThanOrEqualTo(root.get("creationTime"), commonReportCriteria.getStartDate()));
		}
		if (null != commonReportCriteria.getEndDate()) {

			predicates.add(cb.lessThanOrEqualTo(root.get("creationTime"), commonReportCriteria.getEndDate()));
		}

		cq.where(predicates.toArray(new Predicate[predicates.size()]));

		TypedQuery<PatientDischargedDto> query = em.createQuery(cq);

		SimpleDateFormat dateformater_Java = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

		// Add the data rows.
		int srno = 1;
		for (PatientDischargedDto p : query.getResultList()) {
			entries.add(new String[] { String.valueOf(srno++), p.getPatientName(), Integer.toString(p.getAge()),
					p.getGender(), p.getHospitalName(), dateformater_Java.format(p.getDateOfPositiveTestResult()),
					dateformater_Java.format(p.getDateOfAdmission()),
					dateformater_Java.format(p.getDateOfDischarged()) });
		}

		System.out.println("fetchDailyPatientDischargeList  end");
		LOGGER.debug("fetchDailyPatientDischargeList end");

		return entries;
	}
}