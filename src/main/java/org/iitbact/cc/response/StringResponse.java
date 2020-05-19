package org.iitbact.cc.response;

import org.iitbact.cc.beans.BaseBean;

import lombok.Data;

@Data
public class StringResponse implements BaseBean<String> {

    private String response;

	@Override
	public void setEntity(String entity) {
		this.response= entity;
	}
    
}
