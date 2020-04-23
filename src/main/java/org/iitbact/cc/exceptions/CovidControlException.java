package org.iitbact.cc.exceptions;

import lombok.Getter;

@Getter
public class CovidControlException extends Exception {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private final long errorCode;
    private final String errorMessage;
    private final CovidControlErpError error;
    private String detailedMessage;

    public CovidControlException(final long errorCode, final String errorMsg) {
        super();
        this.errorCode = errorCode;
        this.errorMessage = errorMsg;
        this.error = new CovidControlErpError(errorCode, errorMsg);
    }

    public CovidControlException(final CovidControlErpError error) {
        this(error.getErrorCode(), error.getErrorMsg());
    }

    public CovidControlException(final long errorCode, final String errorMsg,
                                 final String detailedMessage) {
        this(errorCode, errorMsg);
        this.detailedMessage = detailedMessage;
    }

    public CovidControlException(final long errorCode, final String errorMsg,
                                 final Throwable t) {
        super(t);
        this.errorCode = errorCode;
        this.errorMessage = errorMsg;
        this.error = new CovidControlErpError(errorCode, errorMsg);
    }

    public CovidControlException(final CovidControlErpError error, final Throwable t) {
        this(error.getErrorCode(), error.getErrorMsg(), t);
    }

    public CovidControlException(final long errorCode, final String errorMsg,
                                 final String detailedMessage, final Throwable t) {
        this(errorCode, errorMsg, t);
        this.detailedMessage = detailedMessage;
    }
}
