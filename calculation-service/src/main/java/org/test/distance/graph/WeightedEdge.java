package org.test.distance.graph;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.math.BigDecimal;

/**
 * Implementation of weighted edge
 * @author Viktar Lebedzeu
 */
public class WeightedEdge<T> extends Edge<T> {
    /** Weight of the edge */
    protected BigDecimal weight;

    protected WeightedEdge(T sourceKey, T destinationKey, BigDecimal weight) {
        super(sourceKey, destinationKey);
        this.weight = weight;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    /**
     * Creates new instance of edge
     * @param sourceKey Source vertex key
     * @param destinationKey Destination vertex key
     * @param weight Edge's weight
     * @param <T> Type of vertex keys
     * @return New instance of edge
     */
    public static <T> WeightedEdge<T> of(T sourceKey, T destinationKey, BigDecimal weight) {
        return new WeightedEdge<T>(sourceKey, destinationKey, weight);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .appendSuper(super.hashCode())
                .append(weight)
                .toHashCode();
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean equals(Object obj) {
        if (obj == null) { return false; }
        if (obj == this) { return true; }
        if (obj.getClass() != getClass()) {
            return false;
        }
        WeightedEdge<T> rhs = (WeightedEdge<T>) obj;
        return new EqualsBuilder()
                .appendSuper(super.equals(obj))
                .append(weight, rhs.weight)
                .isEquals();
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <E extends Edge<T>> E revertEdge() {
        return (E) WeightedEdge.of(destination.getKey(), source.getKey(), weight);
    }
}
