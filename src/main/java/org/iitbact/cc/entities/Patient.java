package org.iitbact.cc.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import com.vladmihalcea.hibernate.type.json.JsonStringType;

import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * The persistent class for the patients database table.
 */
@Entity
@Table(name = "patients")
@NamedQuery(name = "Patient.findAll", query = "SELECT p FROM Patient p")
@TypeDef(name = "json", typeClass = JsonStringType.class)
@Data
@NoArgsConstructor
public class Patient implements Serializable {
	private static final long serialVersionUID = 1L;
		
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "patient_id")
	private Integer patientId;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "gender")
	private String gender;
	
	@Column(name = "dob")
	private String dob;
	
	@Column(name = "age")
	private Integer age;
	
	@Column(name = "month")
	private Integer month;
	
	@Lob
	@Column(name = "address")
	private String address;
	
	@Lob
	@Column(name = "locality")
	private String locality;
	
	@Column(name = "pincode")
	private String pincode;
	
	@Column(name = "contact_number")
	private String contactNumber;
	
	@Column(name = "emergency_contact")
	private String emergencyContact;
	
	@Type(type = "json")
    @Column(name = "pre_existing_medical_condition")
    private Object preExistingMedicalCondition;
	
	@Column(name = "district")
	private String district;
	
	@Column(name = "state")
	private String state;
	
	@Column(name = "first_name")
	private String firstName;
	
	@Column(name = "last_name")
	private String lastName;
	

	@Column(name = "occupation")
	private String occupation;
	
	@Column(name = "goi_covid_id")
	private String goiCovidId;
	
	@Column(name = "hospital_patient_id")
	private String hospitalPatientId;
	
	@Column(name = "district_case_id")
	private String district_case_id;
	
	@Column(name = "covid_uid")
	private String covid_uid;
	
	@Column(name = "creation_time")
	private Date creationTime;
}