package org.iitbact.cc.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FacilityBedsStatsDto {

  private int facilityId;
  private String severity;
  private String covidStatus;
  private long numberTotalCovidBeds;
  private long numberAvailableCovidBeds;

}
