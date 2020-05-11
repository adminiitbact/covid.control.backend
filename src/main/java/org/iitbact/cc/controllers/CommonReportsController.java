package org.iitbact.cc.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import org.iitbact.cc.constants.ReportNames;
import org.iitbact.cc.exceptions.CovidControlException;
import org.iitbact.cc.requests.CommonReportCriteria;
import org.iitbact.cc.services.ApiValidationService;
import org.iitbact.cc.services.PatientDischargeReportService;
import org.iitbact.cc.services.ReportService;
import org.iitbact.cc.services.TotalPatientsReportService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.opencsv.CSVWriter;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/common/reports")
public class CommonReportsController {

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
		try {
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
//				response.setContentType("text/csv");
//				response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"");
//				response.setContentLength((int) file.get().length());
//
//				FileCopyUtils.copy(new FileInputStream(file.get()), response.getOutputStream());

			HttpHeaders headers = new HttpHeaders();
			headers.add("Content-Disposition", "attachment; filename=" + fileName);
			headers.add("Access-Control-Expose-Headers","*");

			FileInputStream in = new FileInputStream(file.get());
			return ResponseEntity.ok().headers(headers).contentLength((int) file.get().length())
					.contentType(MediaType.parseMediaType("text/csv")).header("filename", fileName)
					.body(new InputStreamResource(in));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CovidControlException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
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
