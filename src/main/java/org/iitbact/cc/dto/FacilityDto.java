package org.iitbact.cc.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.iitbact.cc.entities.Facility;
import org.iitbact.cc.entities.FacilityAssets;
import org.iitbact.cc.entities.FacilityChecklist;
import org.iitbact.cc.entities.FacilityContact;
import org.iitbact.cc.entities.FacilityInventory;
import org.iitbact.cc.entities.FacilityMedstaff;

import java.util.Collections;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class FacilityDto {
    private String address;
    private String agreementStatus;
    private String area;
    private String covidFacilityType;
    private String email;
    private Integer facilityId;
    private String facilityStatus;
    private byte governmentHospital;
    private String hospitalCategory;
    private String institutionType;
    private byte isFeverClinicAvailable;
    private byte isSeperateEntryExitAvailable;
    private String jurisdiction;
    private String name;
    private String telephone;
    private String ulbWardName;
    private String ulbZoneName;
    private List<AvailabilityStatus> availabilityStatusList;
    private Boolean hasLinks;
    private Boolean operatingStatus;
    private FacilityContact facilityContact;
    private FacilityAssets facilityAssets;
    private FacilityChecklist facilityChecklist;
    private FacilityInventory facilityInventory;
    private FacilityMedstaff facilityMedstaff;


    public static FacilityDto createFromFacility(Facility facility){
        return createFromFacility(facility, Collections.emptyList());
    }

    public static FacilityDto createFromFacility(Facility facility, List<AvailabilityStatus> availabilityStatus){
        return FacilityDto.builder()
                .address(facility.getAddress())
                .agreementStatus(facility.getAgreementStatus())
                .area(facility.getArea())
                .covidFacilityType(facility.getCovidFacilityType())
                .email(facility.getEmail())
                .facilityId(facility.getFacilityId())
                .facilityStatus(facility.getFacilityStatus())
                .governmentHospital(facility.getGovernmentHospital())
                .hospitalCategory(facility.getHospitalCategory())
                .institutionType(facility.getInstitutionType())
                .isFeverClinicAvailable(facility.getIsFeverClinicAvailable())
                .isSeperateEntryExitAvailable(facility.getIsSeperateEntryExitAvailable())
                .jurisdiction(facility.getJurisdiction())
                .name(facility.getName())
                .telephone(facility.getTelephone())
                .ulbWardName(facility.getUlbWardName())
                .ulbZoneName(facility.getUlbZoneName())
                .availabilityStatusList(availabilityStatus)
                .hasLinks(facility.getHasLinks())
                .operatingStatus(facility.getOperatingStatus())
                .facilityContact(facility.getFacilityContact())
                .facilityAssets(facility.getFacilityAssets())
                .facilityChecklist(facility.getFacilityChecklist())
                .facilityInventory(facility.getFacilityInventory())
                .facilityMedstaff(facility.getFacilityMedstaff())
                .build();
    }


}
