package org.iitbact.cc.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The persistent class for the patient_discharged database table.
 * 
 */
@Entity
@Table(name = "patient_discharged")
@Data
@NoArgsConstructor
public class PatientDischarged implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "patient_id", referencedColumnName = "patient_id")
	private Patient patient;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "facility_id", referencedColumnName = "facility_id")
	private Facility facility;

	@Column(name = "patient_hospital_id")
	private String patientHospitalId;

	@Column(name = "ward_id")
	private int wardId;

	@Column(name = "severity")
	private String severity;

	@Column(name = "test_status")
	private String testStatus;
	
	@Column(name = "reason")
	private String reason;
	
	@Column(name = "quarantine_type")
	private String quarantineType;
	
	@Column(name = "creation_time")
	private Date  creationTime;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "patient_id", referencedColumnName = "patient_id",insertable = false, updatable = false)
	private PatientHistory patientHistory;

}