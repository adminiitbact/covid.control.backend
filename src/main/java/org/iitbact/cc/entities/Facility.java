package org.iitbact.cc.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import java.io.Serializable;


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
    private String agreementStatus;

    private String area;

    @Column(name = "covid_facility_type")
    private String covidFacilityType;

    private String email;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "facility_id")
    private int facilityId;

    @Column(name = "facility_status")
    private String facilityStatus;

    @Column(name = "government_hospital")
    private byte governmentHospital;

    @Column(name = "hospital_category")
    private String hospitalCategory;

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

    public void copy(Facility that) {
        this.address = that.address;
        this.agreementStatus = that.agreementStatus;
        this.area = that.area;
        this.covidFacilityType = that.covidFacilityType;
        this.email = that.email;
        this.facilityStatus = that.facilityStatus;
        this.governmentHospital = that.governmentHospital;
        this.hospitalCategory = that.hospitalCategory;
        this.institutionType = that.institutionType;
        this.isFeverClinicAvailable = that.isFeverClinicAvailable;
        this.isSeperateEntryExitAvailable = that.isSeperateEntryExitAvailable;
        this.jurisdiction = that.jurisdiction;
        this.name = that.name;
        this.telephone = that.telephone;
        this.ulbWardName = that.ulbWardName;
        this.ulbZoneName = that.ulbZoneName;
    }
}