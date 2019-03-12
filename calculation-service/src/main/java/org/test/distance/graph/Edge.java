package org.test.distance.graph;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Class of graph edge
 * @author Viktar Lebedzeu
 */
public class Edge<T> {
    /** Source vertex */
    protected Vertex<T> source;
    /** Destination vertex */
    protected Vertex<T> destination;

    private Edge() {
    }

    protected Edge(T sourceKey, T destinationKey) {
        this.source = new Vertex<>(sourceKey);
        this.destination = new Vertex<>(destinationKey);
    }

    public Vertex<T> getSource() {
        return source;
    }

    public Vertex<T> getDestination() {
        return destination;
    }

    /**
     * Creates new instance of edge
     * @param sourceKey Source vertex key
     * @param destinationKey Destination vertex key
     * @param <T> Type of vertex keys
     * @return New instance of edge
     */
    public static <T> Edge<T> of(T sourceKey, T destinationKey) {
        return new Edge<>(sourceKey, destinationKey);
    }

    /**
     * Checks if the edge is cyclic
     * @return True if edge is cyclic, false otherwise
     */
    public boolean isCyclic() {
        return source.equals(destination);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(source).append(destination).toHashCode();
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean equals(Object obj) {
        if (obj == null) { return false; }
        if (obj == this) { return true; }
        if (obj.getClass() != getClass()) {
            return false;
        }
        Edge<T> rhs = (Edge<T>) obj;
        return new EqualsBuilder()
                .append(source, rhs.source)
                .append(destination, rhs.destination)
                .isEquals();
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    /**
     * Creates new instance with reverted vertices
     * @return Reverted edge
     */
    @SuppressWarnings("unchecked")
    public <E extends Edge<T>> E revertEdge() {
        return (E) Edge.of(destination.getKey(), source.getKey());
    }
}
