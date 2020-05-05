package org.iitbact.cc.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatientDto {

	private String name;
	private int patientId;
	private String gender;
	private int age;
	private String locality;
	private String goiCovidId;
	private String severtiy;
	private String testStatus;
	private String facilityName;
	private String area;
	private String covidFaciltyType;

}
