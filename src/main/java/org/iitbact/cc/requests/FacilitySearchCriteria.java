package org.iitbact.cc.requests;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FacilitySearchCriteria extends BaseRequest {
	private String name;
	private Boolean hasLinks;
	private Boolean operatingStatus;
	private List<String> covidFacilityType;
	private List<String> areas;
	private List<String> jurisdictions;
	private List<String> facilityStatus;
}
