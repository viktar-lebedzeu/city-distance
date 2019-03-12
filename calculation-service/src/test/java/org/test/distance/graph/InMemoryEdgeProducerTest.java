package org.test.distance.graph;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Unit test suite for testing InMemoryEdgeProducer features
 * @author Viktar Lebedzeu
 */
public class InMemoryEdgeProducerTest {
    /** Logger */
    private static final Logger logger = LoggerFactory.getLogger(InMemoryEdgeProducerTest.class);

    @Test
    public void testFindEdges() {
        InMemoryEdgeProducer<String, Edge<String>> producer = new InMemoryEdgeProducer<String, Edge<String>>()
                .addEdge(WeightedEdge.of("A", "B", new BigDecimal(5)), EdgeDirectionEnum.TWO_WAY)
                .addEdge(WeightedEdge.of("B", "C", new BigDecimal(10)), EdgeDirectionEnum.TWO_WAY)
                .addEdge(WeightedEdge.of("C", "D", new BigDecimal(1)), EdgeDirectionEnum.TWO_WAY)
                .addEdge(WeightedEdge.of("A", "C", new BigDecimal(11)), EdgeDirectionEnum.TWO_WAY)
                .addEdge(WeightedEdge.of("A", "D", new BigDecimal(12)), EdgeDirectionEnum.TWO_WAY)
                .addEdge(WeightedEdge.of("C", "G", new BigDecimal(1)), EdgeDirectionEnum.TWO_WAY)
                .addEdge(WeightedEdge.of("G", "D", new BigDecimal(2)), EdgeDirectionEnum.TWO_WAY)
                .addEdge(WeightedEdge.of("A", "F", new BigDecimal(2)), EdgeDirectionEnum.TWO_WAY)
                .addEdge(WeightedEdge.of("A", "E", new BigDecimal(2)), EdgeDirectionEnum.TWO_WAY)
                .addEdge(WeightedEdge.of("E", "F", new BigDecimal(1)), EdgeDirectionEnum.TWO_WAY);

        List<? extends Edge<String>> edges = producer.findEdges("A");
        Assert.assertEquals("[B, C, D, E, F]", collectDestinationPoints(edges).toString());

        edges = producer.findEdges("B");
        Assert.assertEquals("[A, C]", collectDestinationPoints(edges).toString());

        edges = producer.findEdges("C");
        Assert.assertEquals("[A, B, D, G]", collectDestinationPoints(edges).toString());

        edges = producer.findEdges("D");
        Assert.assertEquals("[A, C, G]", collectDestinationPoints(edges).toString());

        edges = producer.findEdges("E");
        Assert.assertEquals("[A, F]", collectDestinationPoints(edges).toString());

        edges = producer.findEdges("F");
        Assert.assertEquals("[A, E]", collectDestinationPoints(edges).toString());

        edges = producer.findEdges("G");
        Assert.assertEquals("[C, D]", collectDestinationPoints(edges).toString());

        edges = producer.findEdges("X");
        Assert.assertEquals("[]", collectDestinationPoints(edges).toString());
    }

    @Test
    public void testReverseFindEdges() {
        InMemoryEdgeProducer<String, Edge<String>> producer = new InMemoryEdgeProducer<String, Edge<String>>()
                .addEdge(Edge.of("A", "B"))
                .addEdge(Edge.of("B", "C"))
                .addEdge(Edge.of("C", "D"));

        List<? extends Edge<String>> edges = producer.findEdges("D", EdgeDirectionEnum.REVERSE);
        Assert.assertEquals("[C]", collectSourcePoints(edges).toString());

        edges = producer.findEdges("C", EdgeDirectionEnum.REVERSE);
        Assert.assertEquals("[B]", collectSourcePoints(edges).toString());

        edges = producer.findEdges("B", EdgeDirectionEnum.REVERSE);
        Assert.assertEquals("[A]", collectSourcePoints(edges).toString());

        edges = producer.findEdges("X", EdgeDirectionEnum.REVERSE);
        Assert.assertEquals("[]", collectSourcePoints(edges).toString());
    }

    private static <T> List<T> collectDestinationPoints(List<? extends Edge<T>> edges) {
        return edges.parallelStream().map(e -> e.getDestination().getKey()).sorted().collect(Collectors.toList());
    }

    private static <T> List<T> collectSourcePoints(List<? extends Edge<T>> edges) {
        return edges.parallelStream().map(e -> e.getSource().getKey()).sorted().collect(Collectors.toList());
    }

    private static <T> List<String> collectPoints(List<? extends Edge<T>> edges) {
        return edges.parallelStream().map(e -> {
            return e.getSource().getKey().toString() + "-" + e.getDestination().getKey().toString();
        }).collect(Collectors.toList());
    }
}
