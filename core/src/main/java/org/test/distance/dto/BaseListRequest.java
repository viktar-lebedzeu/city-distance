package org.test.distance.dto;

import java.util.List;

/**
 * Base request object
 * @author Viktar Lebedzeu
 */
public class BaseListRequest<T> {
    protected List<T> data;

    public BaseListRequest() {
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }
}
