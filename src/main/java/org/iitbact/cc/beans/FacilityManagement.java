package org.iitbact.cc.beans;

import org.iitbact.cc.entities.Facility;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FacilityManagement {


	private int facilityId;

	private String name;
	
	private String area;
	
	private String jurisdiction;

	private String institutionType;
	
	private String covidFacilityType;

	private byte governmentHospital;
	
	private String telephone;
	
	private String email;

	private String facilityStatus;

	private String hospitalCategory;

	private String agreementStatus;

	private byte isSeparateEntryExitAvailable;

	private byte isFeverClinicAvailable;
	
	private String address;

	private String ulbWardName;

	private String ulbZoneName;

	private int toltalBeds;

	private int covidIcuBeds;

	private int totalIcuBeds;

	private int totalVentilators;

	private int totalIcuBedsOccupied;

	private int ventilatorsAllocatedCovid;

	private int mildSuspectedTotal;

	private int mildSuspectedOccupied;

	private int mildConfirmedTotal;

	private int mildConfirmedOccupied;

	private int moderateSuspectedTotal;

	private int moderateSuspectedOccupied;

	private int moderateConfirmedTotal;

	private int moderateConfirmedOccupied;

	private int severeSuspectedTotal;

	private int severeSuspectedOccupied;

	private int severeConfirmedTotal;

	private int severeConfirmedOccupied;

	private int ventilatorsEarmarkedForCovid;

	private int ventilatorInUseCovid;

	private int oxygenCylinderAvailable;

	private int wallOxygenSuppliedBeds;

	private int centralOxygenSuppliedBeds;

	private int pulseOximeters;

	private int infusionPumps;

	private int independentBeds;

	public FacilityManagement(Facility facility ,FacilityManagamentDetails details) {
		this.facilityId = facility.getFacilityId();
		this.name = facility.getName();
		this.area = facility.getArea();
		this.jurisdiction = facility.getJurisdiction();
		this.institutionType = facility.getInstitutionType();
		this.covidFacilityType = facility.getCovidFacilityType();
		this.governmentHospital = facility.getGovernmentHospital();
		this.telephone = facility.getTelephone();
		this.email = facility.getEmail();
		this.facilityStatus = facility.getFacilityStatus();
		this.hospitalCategory = facility.getHospitalCategory();
		this.agreementStatus = facility.getAgreementStatus();
		this.isSeparateEntryExitAvailable = facility.getIsSeperateEntryExitAvailable();
		this.isFeverClinicAvailable = facility.getIsFeverClinicAvailable();
		this.address = facility.getAddress();
		this.ulbWardName = facility.getUlbWardName();
		this.ulbZoneName = facility.getUlbZoneName();
		this.toltalBeds = details.getToltalBeds();
		this.covidIcuBeds = details.getCovidIcuBeds();
		this.totalIcuBeds = details.getTotalIcuBeds();
		this.totalVentilators = details.getTotalVentilators();
		this.totalIcuBedsOccupied = details.getTotalIcuBedsOccupied();
		this.ventilatorsAllocatedCovid = details.getVentilatorsAllocatedCovid();
		this.mildSuspectedTotal = details.getMildSuspectedTotal();
		this.mildSuspectedOccupied = details.getMildSuspectedOccupied();
		this.mildConfirmedTotal = details.getMildConfirmedTotal();
		this.mildConfirmedOccupied = details.getMildConfirmedOccupied();
		this.moderateSuspectedTotal = details.getModerateSuspectedTotal();
		this.moderateSuspectedOccupied = details.getModerateSuspectedOccupied();
		this.moderateConfirmedTotal = details.getModerateConfirmedTotal();
		this.moderateConfirmedOccupied = details.getModerateConfirmedOccupied();
		this.severeSuspectedTotal = details.getSevereSuspectedTotal();
		this.severeSuspectedOccupied = details.getSevereSuspectedOccupied();
		this.severeConfirmedTotal = details.getSevereConfirmedTotal();
		this.severeConfirmedOccupied = details.getSevereConfirmedOccupied();
		this.ventilatorsEarmarkedForCovid = details.getVentilatorsEarmarkedForCovid();
		this.ventilatorInUseCovid = details.getVentilatorInUseCovid();
		this.oxygenCylinderAvailable = details.getOxygenCylinderAvailable();
		this.wallOxygenSuppliedBeds = details.getWallOxygenSuppliedBeds();
		this.centralOxygenSuppliedBeds = details.getCentralOxygenSuppliedBeds();
		this.pulseOximeters = details.getPulseOximeters();
		this.infusionPumps = details.getInfusionPumps();
		this.independentBeds = details.getIndependentBeds();
	}

	
}
