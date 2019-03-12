package org.test.distance.dto;

import java.util.Collection;

/**
 * Success response that contains list of results
 * @author Viktar Lebedzeu
 */
public class SuccessCollectionResponse extends SuccessResponse {
    /** Count of records in the result collection. */
    private Long totalCount = 0L;

    /**
     * Initializing constructor.
     * @param result Result collection of objects.
     * @param totalCount Total count of records in the database.
     */
    SuccessCollectionResponse(Collection<?> result, Long totalCount) {
        super(result);
        this.totalCount = totalCount;
    }

    public Long getTotalCount() {
        return totalCount;
    }
}
