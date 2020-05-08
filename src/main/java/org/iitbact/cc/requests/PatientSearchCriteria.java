package org.iitbact.cc.requests;

import java.util.List;

import org.iitbact.cc.constants.SEVERITY;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PatientSearchCriteria extends BaseRequest {
	private String name;
	
	private List<Integer> facilityIds;
	
	@ApiModelProperty(value="checkbox just like facility")
	private List<String> covidFacilityType;
	
	@ApiModelProperty(value="refer to facility listing filter")
	private List<String> areas;
	
	private int age;
	
	private SEVERITY severity;
	
	@ApiModelProperty(value = "Male/Female/Other")
	private String gender;
	
}
