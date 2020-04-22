package org.iitbact.cc.beans;

import org.iitbact.cc.exceptions.CovidControlErpError;

public class ResponseBuilder {
	private CovidControlErpError error;
	private BaseBean data;
	
	public ResponseBuilder() {}
	
	public ResponseBuilder(BaseBean data, CovidControlErpError error) {
		this.data = data;
		this.error = error;
	}
	
	public ResponseBean build() {
		return new ResponseBean(this);
	}

	public CovidControlErpError getError() {
		return error;
	}

	public BaseBean getData() {
		return data;
	}

	public void setError(CovidControlErpError error) {
		this.error = error;
	}

	public void setData(BaseBean data) {
		this.data = data;
	}
}
