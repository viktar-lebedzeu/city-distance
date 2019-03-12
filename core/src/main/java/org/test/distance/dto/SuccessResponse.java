package org.test.distance.dto;

/**
 * Success response DTO bean
 * @author Viktar Lebedzeu
 */
public class SuccessResponse extends BaseResponse {
    /** Result object */
    private Object result;

    SuccessResponse(Object result) {
        super("OK");
        this.result = result;
    }

    public Object getResult() {
        return result;
    }
}
