package org.iitbact.cc.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import org.iitbact.cc.constants.ReportNames;
import org.iitbact.cc.exceptions.CovidControlErpError;
import org.iitbact.cc.exceptions.CovidControlException;
import org.iitbact.cc.requests.CommonReportCriteria;
import org.iitbact.cc.services.ApiValidationService;
import org.iitbact.cc.services.PatientDischargeReportService;
import org.iitbact.cc.services.TotalPatientsReportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.opencsv.CSVWriter;

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
			@RequestBody CommonReportCriteria commonReportCriteria) {

		CovidControlErpError error = null;
		try {

			System.out.println("CommonReportCriteria Request start");
			LOGGER.info("CommonReportCriteria Request start");

			String userId = validationService.verifyFirebaseIdToken(commonReportCriteria.getAuthToken());

			Optional<File> file;
			String fileName;
			String suffixFilename = "-" + commonReportCriteria.getJurisdiction() + "-"
					+ dateFormat.format(commonReportCriteria.getStartDate()) + "_"
					+ dateFormat.format(commonReportCriteria.getEndDate()) + ".csv";

			switch (commonReportCriteria.getReportName()) {
			case ReportNames.DAILY_PATIENT_DISCHARGE_LIST:
				fileName = "DailyPatientDischargeList" + suffixFilename;
				file = Optional
						.ofNullable(writeCsvFile(patientDischargeReportService.fetchDailyPatientDischargeList(userId,
								ReportNames.DAILY_PATIENT_DISCHARGE_LIST, commonReportCriteria), fileName));
				break;
			case ReportNames.DAILY_PATIENT_DEATH_LIST:
				fileName = "DailyPatientDeathList" + suffixFilename;
				file = Optional
						.ofNullable(writeCsvFile(patientDischargeReportService.fetchDailyPatientDischargeList(userId,
								ReportNames.DAILY_PATIENT_DEATH_LIST, commonReportCriteria), fileName));
				break;
			case ReportNames.SUM_ISOLATION_TOTAL_PATIENT:
				fileName = "SumIsolationTotalPatient" + suffixFilename;
				file = Optional.ofNullable(writeCsvFile(
						totalPatientsReportService.fetchSumIsolationPatientList(userId, commonReportCriteria),
						fileName));
				break;
			default:
				throw new IllegalArgumentException("Invalid report name");
			}

			HttpHeaders headers = new HttpHeaders();
			headers.add("Content-Disposition", "attachment; filename=" + fileName);
			headers.add("Access-Control-Expose-Headers", "*");

			FileInputStream in = new FileInputStream(file.get());

			System.out.println("CommonReportCriteria Request end");
			LOGGER.info("CommonReportCriteria Request end");

			return ResponseEntity.ok().headers(headers).contentLength((int) file.get().length())
					.contentType(MediaType.parseMediaType("text/csv")).header("filename", fileName)
					.body(new InputStreamResource(in));
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
		return ResponseEntity.ok().body(error);
	}

	public File writeCsvFile(List<String[]> data, String filename) throws IOException {
		File csvFile = new File(filename);
		CSVWriter writer = new CSVWriter(new FileWriter(csvFile, false));
		for (String[] mapping : data) {
			writer.writeNext(mapping);
		}
		writer.close();
		return csvFile;
	}

}