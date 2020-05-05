package org.iitbact.cc.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.annotations.TypeDef;

import com.vladmihalcea.hibernate.type.json.JsonStringType;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "patients")
@NamedQuery(name = "Patient.findAll", query = "SELECT p FROM Patient p")
@TypeDef(name = "json", typeClass = JsonStringType.class)
@Data
@NoArgsConstructor
public class Patient implements Serializable {
	private static final long serialVersionUID = 1L;

	private String name;
	private int age;
	private String gender;
	@Lob
	private String locality;

	@Column(name = "contact_number")
	private String contactNumber;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "patient_id")
	private int patientId;

	@Column(name = "district_case_id")
	private String districtCaseId;

	@Column(name = "hospital_patient_id")
	private String hostpitalPatientId;

	@Column(name = "goi_covid_id")
	private String goiCovidId;

}