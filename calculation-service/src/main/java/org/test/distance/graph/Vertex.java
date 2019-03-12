package org.test.distance.graph;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Vertex class
 * @author Viktar Lebedzeu
 */
public class Vertex<T> {
    /* * Logger */
    // private static final Logger logger = LoggerFactory.getLogger(Vertex.class);

    /** Vertex key object */
    private T key;

    /**
     * Default constructor
     * @param key Initial value of vertex's key
     */
    public Vertex(T key) {
        /*
        if (key == null) {
            logger.warn("Vertex key is null.");
        }
        */
        this.key = key;
    }

    public T getKey() {
        return key;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(key).toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) { return false; }
        if (obj == this) { return true; }
        if (obj.getClass() != getClass()) {
            return false;
        }
        Vertex<T> rhs = (Vertex<T>) obj;
        return new EqualsBuilder().append(key, rhs.key).isEquals();
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
