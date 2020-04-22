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


/**
 * The persistent class for the facilities database table.
 * 
 */
@Entity
@Table(name="facilities")
@NamedQuery(name="Facility.findAll", query="SELECT f FROM Facility f")
public class Facility implements Serializable {
	private static final long serialVersionUID = 1L;

	@Lob
	private String address;

	@Column(name="agreement_status")
	private String agreementStatus;

	private String area;

	@Column(name="covid_facility_type")
	private String covidFacilityType;

	private String email;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="facility_id")
	private int facilityId;

	@Column(name="facility_status")
	private String facilityStatus;

	@Column(name="government_hospital")
	private byte governmentHospital;

	@Column(name="hospital_category")
	private String hospitalCategory;

	@Column(name="institution_type")
	private String institutionType;

	@Column(name="is_fever_clinic_available")
	private byte isFeverClinicAvailable;

	@Column(name="is_seperate_entry_exit_available")
	private byte isSeperateEntryExitAvailable;

	private String jurisdiction;

	private String name;

	private String telephone;

	@Column(name="ulb_ward_name")
	private String ulbWardName;

	@Column(name="ulb_zone_name")
	private String ulbZoneName;

	public Facility() {
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getAgreementStatus() {
		return this.agreementStatus;
	}

	public void setAgreementStatus(String agreementStatus) {
		this.agreementStatus = agreementStatus;
	}

	public String getArea() {
		return this.area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getCovidFacilityType() {
		return this.covidFacilityType;
	}

	public void setCovidFacilityType(String covidFacilityType) {
		this.covidFacilityType = covidFacilityType;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getFacilityId() {
		return this.facilityId;
	}

	public void setFacilityId(int facilityId) {
		this.facilityId = facilityId;
	}

	public String getFacilityStatus() {
		return this.facilityStatus;
	}

	public void setFacilityStatus(String facilityStatus) {
		this.facilityStatus = facilityStatus;
	}

	public byte getGovernmentHospital() {
		return this.governmentHospital;
	}

	public void setGovernmentHospital(byte governmentHospital) {
		this.governmentHospital = governmentHospital;
	}

	public String getHospitalCategory() {
		return this.hospitalCategory;
	}

	public void setHospitalCategory(String hospitalCategory) {
		this.hospitalCategory = hospitalCategory;
	}

	public String getInstitutionType() {
		return this.institutionType;
	}

	public void setInstitutionType(String institutionType) {
		this.institutionType = institutionType;
	}

	public byte getIsFeverClinicAvailable() {
		return this.isFeverClinicAvailable;
	}

	public void setIsFeverClinicAvailable(byte isFeverClinicAvailable) {
		this.isFeverClinicAvailable = isFeverClinicAvailable;
	}

	public byte getIsSeperateEntryExitAvailable() {
		return this.isSeperateEntryExitAvailable;
	}

	public void setIsSeperateEntryExitAvailable(byte isSeperateEntryExitAvailable) {
		this.isSeperateEntryExitAvailable = isSeperateEntryExitAvailable;
	}

	public String getJurisdiction() {
		return this.jurisdiction;
	}

	public void setJurisdiction(String jurisdiction) {
		this.jurisdiction = jurisdiction;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTelephone() {
		return this.telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getUlbWardName() {
		return this.ulbWardName;
	}

	public void setUlbWardName(String ulbWardName) {
		this.ulbWardName = ulbWardName;
	}

	public String getUlbZoneName() {
		return this.ulbZoneName;
	}

	public void setUlbZoneName(String ulbZoneName) {
		this.ulbZoneName = ulbZoneName;
	}

}