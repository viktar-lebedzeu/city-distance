package org.test.distance.graph;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * In-memory implementation of edge producer interface
 * @author Viktar Lebedzeu
 */
public class InMemoryEdgeProducer<T, E extends Edge<T>> implements EdgeProducer<T, E> {
    /** Logger */
    private final static Logger logger = LoggerFactory.getLogger(InMemoryEdgeProducer.class);

    /** Map of directional edges grouped by vertex key */
    private HashMap<T, List<E>> edgesMap = new HashMap<>();
    /** Map of reverse edges grouped by vertex key */
    private HashMap<T, List<E>> reverseEdgesMap = new HashMap<>();

    /** Count of edges */
    private Integer edgesCount = 0;

    public InMemoryEdgeProducer<T, E> addEdge(E edge, EdgeDirectionEnum direction) {
        E revertedEdge = edge.revertEdge();
        if (direction == EdgeDirectionEnum.DIRECT || direction == EdgeDirectionEnum.TWO_WAY) {
            updateMapValue(edge);
        }
        if (direction == EdgeDirectionEnum.REVERSE || direction == EdgeDirectionEnum.TWO_WAY) {
            updateMapValue(revertedEdge);
        }
        edgesCount++;
        return this;
    }

    public InMemoryEdgeProducer<T, E> addEdge(E edge) {
        return addEdge(edge, EdgeDirectionEnum.DIRECT);
    }

    @Override
    public List<E> findEdges(T vertexKey, EdgeDirectionEnum direction) {
        if (direction == EdgeDirectionEnum.REVERSE) {
            return reverseEdgesMap.getOrDefault(vertexKey, new ArrayList<>(0));
        }
        return edgesMap.getOrDefault(vertexKey, new ArrayList<>(0));
    }

    @Override
    public List<E> findEdges(Collection<T> vertexKeys, EdgeDirectionEnum direction) {
        LinkedList<E> resultList = new LinkedList<>();
        for (T vertexKey : vertexKeys) {
            final List<E> edges = findEdges(vertexKey, direction);
            resultList.addAll(edges);
        }
        return resultList;
    }

    private void updateMapValue(E edge) {
        T sourceKey = edge.source.getKey();
        T destinationKey = edge.destination.getKey();
        if (!edgesMap.containsKey(sourceKey)) {
            edgesMap.put(sourceKey, new LinkedList<>());
        }
        if (!reverseEdgesMap.containsKey(destinationKey)) {
            reverseEdgesMap.put(destinationKey, new LinkedList<>());
        }
        edgesMap.get(sourceKey).add(edge);
        reverseEdgesMap.get(destinationKey).add(edge);
    }
}
