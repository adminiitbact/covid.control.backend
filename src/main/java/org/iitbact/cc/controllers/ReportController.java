package org.iitbact.cc.controllers;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletResponse;

import org.iitbact.cc.services.ReportService;
import org.springframework.http.HttpHeaders;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

	private final ReportService reportService;

	public ReportController(ReportService reportService) {
		this.reportService = reportService;
	}

	@RequestMapping(value = "/facilities", method = RequestMethod.POST)
	@ApiOperation(value = "API request to export facilities report")
	public void getFacilitiesReport(HttpServletResponse response) throws IOException {

		File file = reportService.writeCsvFile(reportService.fetchFacilityData(), "facilities.csv");

		response.setContentType("text/csv");
		response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
				"attachment; filename=\"facilities.csv\"");
		response.setContentLength((int) file.length());

		InputStream inputStream = new BufferedInputStream(new FileInputStream(file));

		FileCopyUtils.copy(inputStream, response.getOutputStream());
	}

	@RequestMapping(value = "/patients", method = RequestMethod.POST)
	@ApiOperation(value = "API request to export patients report")
	public void getPatientsReport(HttpServletResponse response) throws IOException {

		File file = reportService.writeCsvFile(reportService.fetchPatientData(), "patients.csv");

		response.setContentType("text/csv");
		response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
				"attachment; filename=\"patients.csv\"");
		response.setContentLength((int) file.length());

		InputStream inputStream = new BufferedInputStream(new FileInputStream(file));

		FileCopyUtils.copy(inputStream, response.getOutputStream());
	}

}
