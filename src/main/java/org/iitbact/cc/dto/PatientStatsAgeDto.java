package org.iitbact.cc.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatientStatsAgeDto {

  private int facilityId;
  private String severity;
  private long countLT18;
  private long count18TO44;
  private long count45TO64;
  private long count65TO74;
  private long countGT74;
}
