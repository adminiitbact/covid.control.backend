package org.iitbact.cc.beans;

import org.iitbact.cc.exceptions.CovidControlErpError;

import lombok.Data;

@Data
public class ResponseBean<T extends BaseBean<?>> {
    private CovidControlErpError error;
    private T data;

    ResponseBean(ResponseBuilder<T> builder) {
        if (builder.getError() != null) {
            this.setError(builder.getError());
        } else {
            this.setData(builder.getData());
        }
    }
}
