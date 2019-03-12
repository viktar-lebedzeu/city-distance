package org.test.distance.graph;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Basic implementations of graph path. Contains list of vertices and total count of them in the queue.
 * @author Viktar Lebedzeu
 */
public class GraphPath<T> {
    /** List of nodes (vertices) in the path */
    protected LinkedList<Vertex<T>> nodes = new LinkedList<>();

    protected GraphPath() {
    }

    /**
     * Creates new instance of graph path using the given edge object
     * @param edge Edge object to create path
     * @param <T> Type of vertex keys
     * @return New instance of graph path
     */
    public static <T> GraphPath<T> of(Edge<T> edge) {
        GraphPath<T> path = new GraphPath<>();
        path.nodes.add(edge.source);
        path.nodes.add(edge.destination);
        return path;
    }

    public static <T> GraphPath<T> of(T... keys) {
        GraphPath<T> path = new GraphPath<>();
        Stream.of(keys).forEach(k -> path.nodes.add(new Vertex<>(k)));
        return path;
    }

    public LinkedList<Vertex<T>> getNodes() {
        return nodes;
    }

    public Optional<Vertex<T>> getFirstNodeInPath() {
        return ((nodes.isEmpty() || nodes.size() < 2)
                ? Optional.empty()
                : Optional.of(nodes.getFirst())
        );
    }

    public Optional<Vertex<T>> getLastNodeInPath() {
        return ((nodes.isEmpty() || nodes.size() < 2)
                ? Optional.empty()
                : Optional.of(nodes.getLast())
        );
    }

    public boolean canJoinToStart(GraphPath<T> pathToJoin) throws GraphException {
        if (pathToJoin == null) {
            throw new GraphException("Graph to join is null");
        }
        final Optional<Vertex<T>> firstNodeInPath = getFirstNodeInPath();
        return (firstNodeInPath.isPresent() && firstNodeInPath.equals(pathToJoin.getLastNodeInPath()));
    }

    public boolean canJoinToEnd(GraphPath<T> pathToJoin) throws GraphException {
        if (pathToJoin == null) {
            throw new GraphException("Graph to join is null");
        }
        final Optional<Vertex<T>> lastNodeInPath = getLastNodeInPath();
        return (lastNodeInPath.isPresent() && lastNodeInPath.equals(pathToJoin.getFirstNodeInPath()));
    }

    @SuppressWarnings("unchecked")
    public <E extends GraphPath<T>> E joinToStart(E pathToJoin) throws GraphException {
        if (!canJoinToStart(pathToJoin)) {
            throw new GraphException("Graph " + pathToJoin.toKeyListString() + " can not be joined to start of " +
                    toKeyListString() + " graph.");
        }
        GraphPath<T> joinedGraph = new GraphPath<>();
        joinedGraph.nodes.addAll(pathToJoin.nodes);
        joinedGraph.nodes.addAll(nodes.subList(1, pathToJoin.nodes.size()));
        return (E) joinedGraph;
    }

    @SuppressWarnings("unchecked")
    public <E extends GraphPath<T>> E joinToEnd(E pathToJoin) throws GraphException {
        if (!canJoinToEnd(pathToJoin)) {
            throw new GraphException("Graph " + pathToJoin.toKeyListString() + " can not be joined to end of " +
                    toKeyListString() + " graph.");
        }
        GraphPath<T> joinedGraph = new GraphPath<>();
        joinedGraph.nodes.addAll(nodes);
        joinedGraph.nodes.addAll(pathToJoin.nodes.subList(1, pathToJoin.nodes.size()));
        return (E) joinedGraph;
    }

    public boolean isCyclic() {
        final Set<T> set = nodes.stream().map(Vertex::getKey).collect(Collectors.toSet());
        return set.size() != nodes.size();
    }

    public int size() {
        return nodes.size();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(nodes).toHashCode();
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean equals(Object obj) {
        if (obj == null) { return false; }
        if (obj == this) { return true; }
        if (obj.getClass() != getClass()) {
            return false;
        }
        GraphPath<T> rhs = (GraphPath<T>) obj;
        return new EqualsBuilder()
                .append(nodes, rhs.nodes)
                .isEquals();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("nodes", toKeyListString()).toString();
    }

    public String toKeyListString() {
        final List<String> keyList = toKeyList();
        return keyList.toString();
    }

    public List<String> toKeyList() {
        return nodes.stream().map(v -> v.getKey().toString()).collect(Collectors.toList());
    }

    public String toExtendedKeyListString() {
        return toKeyListString();
    }
}
