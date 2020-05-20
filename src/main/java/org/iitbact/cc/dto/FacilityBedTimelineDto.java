package org.iitbact.cc.dto;


import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class FacilityBedTimelineDto {
	
	private Long totalCovidBeds;
	private Long occupiedBeds;
	private Date date;	
	

}
