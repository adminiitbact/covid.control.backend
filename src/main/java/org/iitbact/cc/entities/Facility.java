package org.iitbact.cc.entities;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The persistent class for the facilities database table.
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "facilities")
@NamedQuery(name = "Facility.findAll", query = "SELECT f FROM Facility f")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Facility implements Serializable {
	private static final long serialVersionUID = 1L;

	@Lob
	private String address;

	@Column(name = "agreement_status")
	private String agreementStatus = "Unassigned";

	private String area;

	@Column(name = "covid_facility_type")
	private String covidFacilityType;

	private String email;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "facility_id")
	private Integer facilityId;

	@Column(name = "facility_status")
	private String facilityStatus = "Unassigned";

	@Column(name = "government_hospital")
	private byte governmentHospital;

	@Column(name = "hospital_category")
	private String hospitalCategory = "Unassigned";

	@Column(name = "institution_type")
	private String institutionType;

	@Column(name = "is_fever_clinic_available")
	private byte isFeverClinicAvailable;

	@Column(name = "is_seperate_entry_exit_available")
	private byte isSeperateEntryExitAvailable;

	private String jurisdiction;

	private String name;

	private String telephone;

	@Column(name = "ulb_ward_name")
	private String ulbWardName;

	@Column(name = "ulb_zone_name")
	private String ulbZoneName;

	private int region;

	@Column(name = "has_links")
	private Boolean hasLinks;

	@Column(name = "operating_status")
	private Boolean operatingStatus;

	// bi-directional one-to-one association to FacilityContact
	@OneToOne(mappedBy = "facility", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private FacilityContact facilityContact;

	@PrePersist
	@PreUpdate
	public void prepersist(){
	    if(agreementStatus == null){
	        agreementStatus = "Unassigned";
        }
	    if(facilityStatus == null){
	        facilityStatus = "Unassigned";
        }
	    if(hospitalCategory == null){
	        hospitalCategory = "Unassigned";
        }
	    // This will be null only for the first time, will be updated only by through automatic logic
	    if(hasLinks == null){
	    	hasLinks = false;
		}
	    if(operatingStatus == null){
	    	operatingStatus = false;
		}

    }

	public void copy(Facility that,AdminUser user) {
		this.address = that.address;
		this.agreementStatus = that.agreementStatus;
		this.area = that.area;
		this.covidFacilityType = that.covidFacilityType;
		this.email = that.email;
		this.facilityStatus = that.facilityStatus;
		this.governmentHospital = that.governmentHospital;
		// this.hospitalCategory = that.hospitalCategory;
		this.institutionType = that.institutionType;
		this.isFeverClinicAvailable = that.isFeverClinicAvailable;
		this.isSeperateEntryExitAvailable = that.isSeperateEntryExitAvailable;
		this.jurisdiction = that.jurisdiction;
		this.name = that.name;
		this.telephone = that.telephone;
		this.ulbWardName = that.ulbWardName;
		this.ulbZoneName = that.ulbZoneName;
		this.facilityContact = that.facilityContact;
		this.facilityContact.setFacilityId(this.facilityId);
		this.region = user.getRegion();
		this.operatingStatus = that.operatingStatus;
		// hasLinks will not be updated through the front-end ever.

	}
}