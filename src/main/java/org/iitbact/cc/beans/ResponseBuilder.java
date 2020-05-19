package org.iitbact.cc.beans;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.iitbact.cc.exceptions.CovidControlErpError;

@Data
@AllArgsConstructor
public class ResponseBuilder<T extends BaseBean<?>> {
    private T data;
    private CovidControlErpError error;

    public ResponseBean<T> build() {
        return new ResponseBean<>(this);
    }

}
