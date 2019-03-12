package org.test.distance.graph;

import java.util.Collection;
import java.util.List;

/**
 * Common interface of edge producer
 * @author Viktar Lebedzeu
 */
public interface EdgeProducer<T, E extends Edge<T>> {
    /**
     * Finds edges for the given vertex key.
     * @param vertexKey Vertex key
     * @param direction Direction of the expected edges
     * @return List of edges
     */
    public List<E> findEdges(T vertexKey, EdgeDirectionEnum direction);

    /**
     * Default implementation of method for searching of directional edges
     * @param vertexKey Vertex key
     * @return List of edges
     */
    public default List<E> findEdges(T vertexKey) {
        return findEdges(vertexKey, EdgeDirectionEnum.DIRECT);
    }

    public List<E> findEdges(Collection<T> vertexKeys, EdgeDirectionEnum direction);

    public default List<E> findEdges(Collection<T> vertexKeys) {
        return findEdges(vertexKeys, EdgeDirectionEnum.DIRECT);
    }
}
