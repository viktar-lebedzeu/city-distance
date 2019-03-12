package org.test.distance.dto;

import java.util.List;

/**
 *
 * @author Viktar Lebedzeu
 */
public class RouteIdsRequestDto {
    private List<String> ids;
    private boolean reverse = false;

    public RouteIdsRequestDto() {
    }

    public RouteIdsRequestDto(List<String> ids, boolean reverse) {
        this.ids = ids;
        this.reverse = reverse;
    }

    public List<String> getIds() {
        return ids;
    }

    public void setIds(List<String> ids) {
        this.ids = ids;
    }

    public boolean isReverse() {
        return reverse;
    }

    public void setReverse(boolean reverse) {
        this.reverse = reverse;
    }
}
