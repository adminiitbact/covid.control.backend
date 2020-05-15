package org.iitbact.cc.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class PatientDischargedDto {
	private Integer id;
	private String patientName;
	private String gender;
	private Integer age;
    private String hospitalName;
    @JsonFormat(pattern="dd-MM-yyyy HH:mm:ss")
    private Date dateOfPositiveTestResult;
    @JsonFormat(pattern="dd-MM-yyyy HH:mm:ss")
    private Date dateOfAdmission;
    @JsonFormat(pattern="dd-MM-yyyy HH:mm:ss")
    private Date dateOfDischarged;

}
