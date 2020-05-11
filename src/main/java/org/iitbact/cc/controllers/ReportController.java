package org.iitbact.cc.controllers;

import java.io.File;
import java.io.FileInputStream;

import javax.servlet.http.HttpServletResponse;

import org.iitbact.cc.requests.BaseRequest;
import org.iitbact.cc.services.ApiValidationService;
import org.iitbact.cc.services.ReportService;
import org.springframework.http.HttpHeaders;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

	private final ReportService reportService;
	private final ApiValidationService validationService;

	public ReportController(ReportService reportService, ApiValidationService validationService) {
		this.reportService = reportService;
		this.validationService = validationService;
	}

	@RequestMapping(value = "/facilities", method = RequestMethod.POST)
	@ApiOperation(value = "API request to export facilities report")
	public void getFacilitiesReport(HttpServletResponse response, @RequestBody BaseRequest reportRequest) {
		
		try {
			String userId = validationService.verifyFirebaseIdToken(reportRequest.getAuthToken());
			File file = reportService.writeCsvFile(reportService.fetchFacilityData(userId), "facilities.csv");

			response.setContentType("text/csv");
			response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
					"attachment; filename=\"facilities.csv\"");
			response.setContentLength((int) file.length());

			FileCopyUtils.copy(new FileInputStream(file), response.getOutputStream());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@RequestMapping(value = "/patients", method = RequestMethod.POST)
	@ApiOperation(value = "API request to export patients report")
	public void getPatientsReport(HttpServletResponse response, @RequestBody BaseRequest reportRequest) {
		try {
			String userId = validationService.verifyFirebaseIdToken(reportRequest.getAuthToken());
			File file = reportService.writeCsvFile(reportService.fetchPatientData(userId), "patients.csv");

			response.setContentType("text/csv");
			response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
					"attachment; filename=\"patients.csv\"");
			response.setContentLength((int) file.length());

			FileCopyUtils.copy(new FileInputStream(file), response.getOutputStream());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
