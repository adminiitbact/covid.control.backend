package org.iitbact.cc.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class AvailabilityStatus {
    private Integer facilityId;
    private String severity;
    private String covidStatus;
    private Long totalBeds;
    private Long availableBeds;
    private Long totalVentilators;
    private Long ventilatorsOccupied;
}
