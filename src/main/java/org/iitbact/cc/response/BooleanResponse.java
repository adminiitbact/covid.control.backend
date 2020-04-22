package org.iitbact.cc.response;

import org.iitbact.cc.beans.BaseBean;

public class BooleanResponse implements BaseBean {
	
	private boolean success;
	
	public BooleanResponse(boolean success) {
		this.success=success;
	}
	
	public BooleanResponse() {
	}
	
	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}
}
