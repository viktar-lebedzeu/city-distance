package org.test.distance.graph;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Factory class for creating different implementations
 * @author Viktar Lebedzeu
 */
public class GraphFactory {
    /** Logger */
    private static final Logger logger = LoggerFactory.getLogger(GraphFactory.class);

    /**
     * Creates graph path by the given edge
     * @param edge Edge object to create path
     * @param <T> Vertex key type
     * @param <E> Type of graph edge
     * @return Created graph path
     */
    @SuppressWarnings("unchecked")
    public static <T, E extends Edge<T>> GraphPath<T> createPathByEdge(E edge) {
        if (edge instanceof WeightedEdge) {
            return WeightedGraphPath.of((WeightedEdge<T>) edge);

        } else {
            return GraphPath.of(edge);
        }
    }

    /**
     * Creates list of paths using the given list of edges
     * @param edges List of graph edges
     * @param <T> Vertex key type
     * @param <E> Type of graph edge
     * @return Created list of graph paths
     */
    public static <T, E extends Edge<T>> List<GraphPath<T>> createPathsByEdges(List<E> edges) {
        ArrayList<GraphPath<T>> resultList = new ArrayList<>(edges.size());
        edges.forEach(e -> resultList.add(createPathByEdge(e)));
        return resultList;
    }
}
