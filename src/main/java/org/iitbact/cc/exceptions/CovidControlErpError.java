package org.iitbact.cc.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CovidControlErpError {

    private long errorCode;
    private String errorMsg;
}
