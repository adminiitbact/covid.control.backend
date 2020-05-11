package org.iitbact.cc.requests;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommonReportCriteria extends BaseRequest {

	private String reportName;
	private String jurisdiction;
	private Date startDate;
	private Date endDate;
}
