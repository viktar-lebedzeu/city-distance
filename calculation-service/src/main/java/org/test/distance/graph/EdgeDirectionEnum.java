package org.test.distance.graph;

/**
 * Enum of edge directions
 * @author Viktar Lebedzeu
 */
public enum EdgeDirectionEnum {
    /** Directional edge */
    DIRECT,
    /** Reverse edge. Used for reverse searching of edges.  */
    REVERSE,
    /** Bidirectional (two-way) edge */
    TWO_WAY;
}
