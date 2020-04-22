package org.iitbact.cc.beans;

import org.iitbact.cc.exceptions.CovidControlErpError;

public class ResponseBean {
	private CovidControlErpError error;
	private BaseBean data;

	ResponseBean(ResponseBuilder builder) {
		if (builder.getError() != null) {
			this.setError(builder.getError());
		} else {
			this.setData(builder.getData());
		}
	}
	
	public CovidControlErpError getError() {
		return error;
	}

	private void setError(CovidControlErpError error) {
		this.error = error;
	}

	/**
	 * @return the data
	 */
	public BaseBean getData() {
		return data;
	}

	/**
	 * @param data the data to set
	 */
	private void setData(BaseBean data) {
		this.data = data;
	}
}
