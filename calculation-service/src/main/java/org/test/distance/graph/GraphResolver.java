package org.test.distance.graph;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * @author Viktar Lebedzeu
 */
@Service
public class GraphResolver {
    /**
     * Logger
     */
    private static final Logger logger = LoggerFactory.getLogger(GraphResolver.class);

    /**
     * Tries to find all possible paths from source point to destination one. Cyclic routes are excluded.
     * @param sourceKey      Source point key
     * @param destinationKey Destination point key
     * @param edgeProducer   Graph edge producer
     * @param <T>            Vertex key type
     * @return List of paths.
     */
    @SuppressWarnings("unchecked")
    public <T, E extends Edge<T>> List<GraphPath<T>> findAllPaths(
            T sourceKey, T destinationKey, EdgeProducer edgeProducer) {

        HashSet<GraphPath<T>> resultPaths = new HashSet<>();

        List<T> directKeys = Collections.singletonList(sourceKey);
        List<T> reverseKeys = Collections.singletonList(destinationKey);

        List<E> sourceEdges = edgeProducer.findEdges(directKeys);
        List<E> destinationEdges = edgeProducer.findEdges(reverseKeys, EdgeDirectionEnum.REVERSE);
        List<GraphPath<T>> sourcePaths = GraphFactory.createPathsByEdges(sourceEdges);
        List<GraphPath<T>> destinationPaths = GraphFactory.createPathsByEdges(destinationEdges);

        do {
            filterPaths(resultPaths, sourcePaths, destinationPaths, sourceKey, destinationKey);
            sourcePaths = nextStepPaths(edgeProducer, sourcePaths, EdgeDirectionEnum.DIRECT);
            filterPaths(resultPaths, sourcePaths, destinationPaths, sourceKey, destinationKey);
            destinationPaths = nextStepPaths(edgeProducer, destinationPaths, EdgeDirectionEnum.REVERSE);
        } while(!sourcePaths.isEmpty() && !destinationPaths.isEmpty());

        return new LinkedList<>(resultPaths);
    }

    @SuppressWarnings("unchecked")
    private static <T, E extends Edge<T>, G extends GraphPath<T>> List<G> nextStepPaths(
            EdgeProducer<T, E> edgeProducer, List<G> paths, EdgeDirectionEnum direction) {

        if (paths == null || paths.isEmpty()) {
            return new ArrayList<>(0);
        }
        LinkedList<G> resultPaths = new LinkedList<>();
        HashSet<T> keys = new HashSet<>();
        paths.forEach(p -> {
            Optional<Vertex<T>> node = (direction == EdgeDirectionEnum.REVERSE)
                    ? p.getFirstNodeInPath()
                    : p.getLastNodeInPath();
            node.ifPresent(v -> keys.add(v.getKey()));
        });
        if (!keys.isEmpty()) {
            HashMap<T, List<G>> pathsByEdgesMap = new HashMap<>();
            final List<E> edges = edgeProducer.findEdges(keys, direction);
            if (!edges.isEmpty()) {
                final List<GraphPath<T>> pathsByEdges = GraphFactory.createPathsByEdges(edges);
                pathsByEdges.forEach(p -> {
                    Optional<Vertex<T>> node = (direction != EdgeDirectionEnum.REVERSE)
                            ? p.getFirstNodeInPath()
                            : p.getLastNodeInPath();
                    node.ifPresent(v -> {
                        if (!pathsByEdgesMap.containsKey(v.getKey())) {
                            pathsByEdgesMap.put(v.getKey(), new LinkedList<>());
                        }
                        pathsByEdgesMap.get(v.getKey()).add((G) p);
                    });
                });
            }
            paths.forEach(p -> {
                Optional<Vertex<T>> node = (direction == EdgeDirectionEnum.REVERSE)
                        ? p.getFirstNodeInPath()
                        : p.getLastNodeInPath();
                if (node.isPresent()) {
                    T key = node.get().getKey();
                    if (pathsByEdgesMap.containsKey(key)) {
                        List<G> list = pathsByEdgesMap.get(key);
                        list.forEach(g -> {
                            try {
                                G newPath = (direction == EdgeDirectionEnum.REVERSE)
                                        ? p.joinToStart(g)
                                        : p.joinToEnd(g);
                                if (!newPath.isCyclic()) {
                                    resultPaths.add(newPath);
                                }
                            }
                            catch (GraphException e) {
                                // skipping the exception
                            }
                        });
                    }
                }
            });
        }
        return resultPaths;
    }

    private static <T> void filterPaths(Set<GraphPath<T>> resultPaths,
                                                           List<GraphPath<T>> sourcePaths,
                                                           List<GraphPath<T>> destinationPaths,
                                                           T sourceKey,
                                                           T destinationKey) {
        joinPaths(sourcePaths, destinationPaths);
        filterPaths(resultPaths, sourcePaths, sourceKey, destinationKey);
        filterPaths(resultPaths, destinationPaths, sourceKey, destinationKey);
    }

    private static <T> void filterPaths(Set<GraphPath<T>> resultPaths, List<GraphPath<T>> paths,
                                        T sourceKey, T destinationKey) {
        HashSet<GraphPath<T>> removedPaths = new HashSet<>();
        paths.forEach(p -> {
            final Optional<Vertex<T>> firstNodeInPath = p.getFirstNodeInPath();
            final Optional<Vertex<T>> lastNodeInPath = p.getLastNodeInPath();
            if (p.isCyclic()) {
                // Removing cyclic paths
                logger.info("Removed cyclic path : " + p.toExtendedKeyListString());
                removedPaths.add(p);

            } else if (firstNodeInPath.isPresent() && lastNodeInPath.isPresent()
                    && sourceKey.equals(firstNodeInPath.get().getKey())
                    && destinationKey.equals(lastNodeInPath.get().getKey())
                    && !resultPaths.contains(p)) {
                logger.info("Accepted path : " + p.toExtendedKeyListString());
                resultPaths.add(p);
            }
        });
        paths.removeAll(removedPaths);
        paths.removeAll(resultPaths);
    }

    private static <T, E extends GraphPath<T>> void joinPaths(List<E> sourcePaths, List<E> destinationPaths) {

        if (sourcePaths == null || sourcePaths.isEmpty() || destinationPaths == null || destinationPaths.isEmpty()) {
            return;
        }
        HashMap<T, List<E>> destMap = new HashMap<>();
        destinationPaths.forEach(p -> {
            final Optional<Vertex<T>> firstNodeInPath = p.getFirstNodeInPath();
            if (firstNodeInPath.isPresent()) {
                final T key = firstNodeInPath.get().getKey();
                if (!destMap.containsKey(key)) {
                    destMap.put(key, new LinkedList<>());
                }
                destMap.get(key).add(p);
            }
        });

        LinkedList<E> pathsToAdd = new LinkedList<>();
        // Trying to join 2 different chains (graph paths)
        sourcePaths.forEach(p -> {
            final Optional<Vertex<T>> lastNodeInPath = p.getLastNodeInPath();
            if (lastNodeInPath.isPresent()) {
                final T key = lastNodeInPath.get().getKey();
                if (destMap.containsKey(key)) {
                    final List<E> dstPaths = destMap.get(key);
                    dstPaths.forEach(d -> {
                        try {
                            final E joinedPath = p.joinToEnd(d);
                            if (!joinedPath.isCyclic()) {
                                pathsToAdd.add(joinedPath);
                            }
                        }
                        catch(GraphException e) {
                            // skipping this exception
                        }
                    });
                }
            }
        });
        sourcePaths.addAll(pathsToAdd);
    }
}