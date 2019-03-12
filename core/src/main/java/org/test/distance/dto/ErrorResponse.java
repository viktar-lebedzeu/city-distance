package org.test.distance.dto;

/**
 * Error response. Contains error count and error message.
 * @author Viktar Lebedzeu
 */
public class ErrorResponse extends BaseResponse {
    /** Error code */
    private Long errorCode;
    /** Error message */
    private String errorMessage;

    /**
     * Initialization constructor.
     * @param errorCode Initial value of error code
     * @param errorMessage Initial value of error message
     */
    ErrorResponse(Long errorCode, String errorMessage) {
        super("FAIL");
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public Long getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
