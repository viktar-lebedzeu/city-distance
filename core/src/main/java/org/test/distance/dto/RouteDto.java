package org.test.distance.dto;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.Serializable;

/**
 * Data transfer object for Route
 * @author Viktar Lebedzeu
 */
public class RouteDto implements Serializable {
    private String from;
    private String to;
    private Double distance;
    private boolean twoWay = false;

    public RouteDto() {
    }

    public RouteDto(String from, String to, Double distance) {
        this.from = from;
        this.to = to;
        this.distance = distance;
    }

    public RouteDto(String from, String to, Double distance, boolean twoWay) {
        this.from = from;
        this.to = to;
        this.distance = distance;
        this.twoWay = twoWay;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public boolean isTwoWay() {
        return twoWay;
    }

    public void setTwoWay(boolean twoWay) {
        this.twoWay = twoWay;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(from).append(to).append(distance).append(twoWay).toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) { return false; }
        if (obj == this) { return true; }
        if (obj.getClass() != getClass()) {
            return false;
        }
        RouteDto rhs = (RouteDto) obj;
        return new EqualsBuilder()
                .append(from, rhs.from)
                .append(to, rhs.to)
                .append(distance, rhs.distance)
                .append(twoWay, rhs.twoWay)
                .isEquals();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("from", from)
                .append("to", to)
                .append("distance", distance)
                .append("twoWay", twoWay)
                .toString();
    }
}
