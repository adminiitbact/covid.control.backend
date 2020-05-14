package org.iitbact.cc.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class FacilityLinkWrapperDto {
    private List<FacilityDto> DCHFacilities;
    private List<FacilityDto> DCHCFacilities;
    private List<FacilityDto> CCCFacilities;
}
