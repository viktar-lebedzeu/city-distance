package org.test.distance.dto;

import java.util.Collection;

/**
 * Response factory. Allows to create error and success response objects.
 * @author Viktar Lebedzeu
 */
public class ResponseFactory {
    /**
     * Creates error response
     * @param errorCode Error code
     * @param errorMessage Error message
     * @return New instance of error response
     */
    public static ErrorResponse createErrorResponse(Long errorCode, String errorMessage) {
        return new ErrorResponse(errorCode, errorMessage);
    }

    /**
     * Creates success response
     * @param result Result object
     * @return New instance of success response
     */
    public static SuccessResponse createSuccessResponse(Object result) {
        return new SuccessResponse(result);
    }

    /**
     * Creates new success response with a result collection
     * @param result Result collection
     * @param totalCount Total count of records in the database
     * @return New instance of success collection response
     */
    public static SuccessCollectionResponse createSuccessCollectionResponse(Collection<?> result, Long totalCount) {
        return new SuccessCollectionResponse(result, totalCount);
    }
}
