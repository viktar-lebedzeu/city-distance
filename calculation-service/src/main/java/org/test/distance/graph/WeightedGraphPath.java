package org.test.distance.graph;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Weighted graph path
 * @author Viktar Lebedzeu
 */
public class WeightedGraphPath<T> extends GraphPath<T> {
    /** Logger */
    private static final Logger logger = LoggerFactory.getLogger(WeightedGraphPath.class);

    /** List of weights */
    protected LinkedList<BigDecimal> weights = new LinkedList<>();

    protected BigDecimal weightsSum;

    protected WeightedGraphPath() {
        super();
    }

    public static <T> WeightedGraphPath<T> of(WeightedEdge<T> edge) {
        WeightedGraphPath<T> path = new WeightedGraphPath<>();
        path.nodes.add(edge.source);
        path.nodes.add(edge.destination);
        path.weights.add(edge.weight);
        path.weightsSum = edge.weight;
        path.updateTotalWeight();
        return path;
    }

    public BigDecimal getWeightsSum() {
        return weightsSum;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <E extends GraphPath<T>> E joinToStart(E pathToJoin) throws GraphException {
        if (!canJoinToStart(pathToJoin)) {
            throw new GraphException("Graph " + pathToJoin.toKeyListString() + " can not be joined to start of " +
                    toKeyListString() + " graph.");
        }
        WeightedGraphPath<T> joinedGraph = new WeightedGraphPath<>();
        joinedGraph.nodes.addAll(pathToJoin.nodes);
        joinedGraph.nodes.addAll(nodes.subList(1, pathToJoin.nodes.size()));
        joinedGraph.weights.addAll(
                (pathToJoin instanceof WeightedGraphPath)
                        ? ((WeightedGraphPath) pathToJoin).weights
                        : new ArrayList<>());
        joinedGraph.weights.addAll(weights);
        joinedGraph.updateTotalWeight();
        return (E) joinedGraph;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <E extends GraphPath<T>> E joinToEnd(E pathToJoin) throws GraphException {
        if (!canJoinToEnd(pathToJoin)) {
            throw new GraphException("Graph " + pathToJoin.toKeyListString() + " can not be joined to end of " +
                    toKeyListString() + " graph.");
        }
        WeightedGraphPath<T> joinedGraph = new WeightedGraphPath<>();
        joinedGraph.nodes.addAll(nodes);
        joinedGraph.nodes.addAll(pathToJoin.nodes.subList(1, pathToJoin.nodes.size()));
        joinedGraph.weights.addAll(weights);
        joinedGraph.weights.addAll(
                (pathToJoin instanceof WeightedGraphPath)
                    ? ((WeightedGraphPath) pathToJoin).weights
                    : new ArrayList<>());
        joinedGraph.updateTotalWeight();
        return (E) joinedGraph;
    }

    @Override
    public String toExtendedKeyListString() {
        return toKeyListString() + " (" + weightsSum + ")";
    }

    private void updateTotalWeight() {
        weightsSum = weights.parallelStream().reduce(BigDecimal::add).orElse(new BigDecimal(0));
    }
}
