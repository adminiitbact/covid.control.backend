package org.iitbact.cc.response;

import lombok.Data;
import org.iitbact.cc.beans.BaseBean;

@Data
public class BooleanResponse implements BaseBean<Boolean> {

    private Boolean success;

	@Override
	public void setEntity(Boolean entity) {
		this.success=entity;
	}
    
}
