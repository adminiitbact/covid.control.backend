package org.iitbact.cc.controllers;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.iitbact.cc.constants.ReportNames;
import org.iitbact.cc.exceptions.CovidControlErpError;
import org.iitbact.cc.exceptions.CovidControlException;
import org.iitbact.cc.requests.CommonReportCriteria;
import org.iitbact.cc.services.ApiValidationService;
import org.iitbact.cc.services.PatientDischargeReportService;
import org.iitbact.cc.services.TotalPatientsReportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/common/reports")
public class CommonReportsController {

	private static final Logger LOGGER = LoggerFactory.getLogger(CommonReportsController.class);

	private final ApiValidationService validationService;
	private final PatientDischargeReportService patientDischargeReportService;
	private final TotalPatientsReportService totalPatientsReportService;

	private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

	public CommonReportsController(PatientDischargeReportService patientDischargeReportService,
			TotalPatientsReportService totalPatientsReportService, ApiValidationService validationService) {
		this.totalPatientsReportService = totalPatientsReportService;
		this.patientDischargeReportService = patientDischargeReportService;
		this.validationService = validationService;
	}

	@PostMapping(value = "/generate")
	@ApiOperation(value = "API request to export common report")

	public ResponseEntity getGeneratedReports(HttpServletResponse response,
			@RequestBody CommonReportCriteria commonReportCriteria) throws Exception {

		CovidControlErpError error = null;
		try {

			System.out.println("CommonReportCriteria Request start");
			LOGGER.info("CommonReportCriteria Request start");

			String userId = validationService.verifyFirebaseIdToken(commonReportCriteria.getAuthToken());

			List<String[]> data;
			String body = "";
			String fileName;
			
			String pattern = "dd-MM-yyyy HH:mm:ss";
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
			String reportCreationdate = simpleDateFormat.format(new Date());
			
			String suffixFilename = "-" + commonReportCriteria.getJurisdiction() + ":"
					+ dateFormat.format(commonReportCriteria.getStartDate()) + ":"
					+ dateFormat.format(commonReportCriteria.getEndDate()) + ":"
					+ reportCreationdate+".csv";

			switch (commonReportCriteria.getReportName()) {
			case ReportNames.DAILY_PATIENT_DISCHARGE_LIST:
				fileName = "DailyPatientDischargeList" + suffixFilename;
				data = patientDischargeReportService.fetchDailyPatientDischargeList(userId,
						ReportNames.DAILY_PATIENT_DISCHARGE_LIST, commonReportCriteria);				
				break;
			case ReportNames.DAILY_PATIENT_DEATH_LIST:
				fileName = "DailyPatientDeathList" + suffixFilename;
				data = patientDischargeReportService.fetchDailyPatientDischargeList(userId,
						ReportNames.DAILY_PATIENT_DEATH_LIST, commonReportCriteria);				
				break;
			case ReportNames.SUM_ISOLATION_TOTAL_PATIENT:
				fileName = "SumIsolationTotalPatient" + suffixFilename;
				data = totalPatientsReportService.fetchSumIsolationPatientList(userId, commonReportCriteria);				
				break;
			case ReportNames.COVID_FACILITIES_SUMMARY:
				fileName = "CovidFacilitiesSummary" + suffixFilename;
				data = totalPatientsReportService.fetchCovidFacilitiesSummaryList(userId, commonReportCriteria);				
				break;	
			default:
				throw new IllegalArgumentException("Invalid report name");
			}
			
			body = prepareCsvData(data, body);
			HttpHeaders headers = new HttpHeaders();
			headers.add("Content-Disposition", "attachment; filename=" + fileName);
			headers.add("Access-Control-Expose-Headers", "*");
			System.out.println("CommonReportCriteria Request end");
			LOGGER.info("CommonReportCriteria Request end");
			return ResponseEntity.ok()
					.headers(headers)
					.contentType(MediaType.parseMediaType("text/csv"))
					.header("filename", fileName)
					.body(body);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("FileNotFoundException =" + e.getMessage());
			LOGGER.error("FileNotFoundException =" + e.getMessage());
			error = new CovidControlException(100, "File Not Found Exception :" + e.getMessage()).getError();
		} catch (CovidControlException e) {
			e.printStackTrace();
			System.out.println("CovidControlException =" + e.getMessage());
			LOGGER.error("CovidControlException =" + e.getMessage());
			error = new CovidControlException(100, "Covid Control Exception :" + e.getMessage()).getError();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("IOException =" + e.getMessage());
			LOGGER.error("IOException =" + e.getMessage());
			error = new CovidControlException(100, "IOException :" + e.getMessage()).getError();
		}
		return ResponseEntity.ok()
				.body(error);
	}

	public String prepareCsvData(List<String[]> data, String body) throws IOException {
		if (CollectionUtils.isEmpty(data)) {
			return null;
		}

		for (int i = 0; i < data.size(); i++) {
			String[] x = data.get(i);
			for (int j = 0; j < x.length; j++) {
				body = body + x[j];
				body = body + ",";
			}
			body = body + "\n";
		}
		return body;
	}

}