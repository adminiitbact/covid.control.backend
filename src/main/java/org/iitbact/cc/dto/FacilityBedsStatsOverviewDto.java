package org.iitbact.cc.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FacilityBedsStatsOverviewDto {

  private int  facilityId;
  private int  government_hospital;
  private long numberTotalBeds;
  private long numberTotalCovidBeds;
  private long numberAvailableBeds;
  private long numberAvailableCovidBeds;

}
