package org.iitbact.cc.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * The persistent class for the patient_live_status database table.
 * 
 */
@Entity
@Table(name="patient_live_status")
@Data
@NoArgsConstructor
public class PatientLiveStatus implements Serializable {
	
	private static final long serialVersionUID = 1L;


	/*
	 * @Column(name="facility_id") private int facilityId;
	 */
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name="severity")
	private String severity;

	/*
	 * @Column(name="patient_id") private int patientId;
	 */

	@Column(name="test_status")
	private String testStatus;

	@Column(name="ward_id")
	private int wardId;
	
	@Column(name="patient_hospital_id")
	private String patientHospitalId;
	
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="facility_id", referencedColumnName="facility_id")
	private Facility facility;

	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="patient_id", referencedColumnName="patient_id")
	private Patient patient;
	

}