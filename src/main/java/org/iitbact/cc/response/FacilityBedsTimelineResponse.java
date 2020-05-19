package org.iitbact.cc.response;

import org.iitbact.cc.beans.BaseBean;
import org.iitbact.cc.dto.FacilityBedTimelineDto;
import lombok.Data;

@Data
public class FacilityBedsTimelineResponse implements BaseBean<FacilityBedTimelineDto> {
	 private FacilityBedTimelineDto facilityBedTimelineDto;
	    
	    @Override
	    public void setEntity(FacilityBedTimelineDto facility) {
	        this.facilityBedTimelineDto = facility;
	    }

}
