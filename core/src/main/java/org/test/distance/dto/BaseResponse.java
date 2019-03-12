package org.test.distance.dto;

/**
 * Base response class. Contains only status field. Other implementations can add another necessary fields.
 * @author Viktar Lebedzeu
 */
public abstract class BaseResponse {
    /** Response status */
    private String status;

    /** Disabled public default constructor */
    private BaseResponse() {
    }

    /**
     * Initialization constructor
     * @param status Initial value of response status
     */
    protected BaseResponse(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
