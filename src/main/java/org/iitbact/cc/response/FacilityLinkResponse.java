package org.iitbact.cc.response;

import lombok.Data;
import org.iitbact.cc.beans.BaseBean;
import org.iitbact.cc.dto.FacilityLinkWrapperDto;

@Data
public class FacilityLinkResponse implements BaseBean<FacilityLinkWrapperDto> {

    private FacilityLinkWrapperDto success;

	@Override
	public void setEntity(FacilityLinkWrapperDto entity) {
		this.success=entity;
	}
    
}
