package org.test.distance.graph;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

/**
 * Unit test suite for testing of WeightedGraphPath
 * @author Viktar Lebedzeu
 */
public class WeightedGraphPathTest {
    /** Logger */
    private static final Logger logger = LoggerFactory.getLogger(WeightedGraphPathTest.class);

    @Test
    public void testJoining() throws GraphException {
        WeightedGraphPath<String> path1 = WeightedGraphPath.of(WeightedEdge.of("A", "B", new BigDecimal(1)));
        WeightedGraphPath<String> path2 = WeightedGraphPath.of(WeightedEdge.of("B", "C", new BigDecimal(10)));
        WeightedGraphPath<String> joinedPath = path1.joinToEnd(path2);
        Assert.assertEquals(new BigDecimal(11), joinedPath.weightsSum);

        path1 = WeightedGraphPath.of(WeightedEdge.of("B", "C", new BigDecimal(5)));
        path2 = WeightedGraphPath.of(WeightedEdge.of("A", "B", new BigDecimal(7)));
        joinedPath = path1.joinToStart(path2);
        Assert.assertEquals(new BigDecimal(12), joinedPath.weightsSum);
    }

    @Test(expected = GraphException.class)
    public void testIncorrectJoinToStart() throws GraphException {
        WeightedGraphPath<String> path1 = WeightedGraphPath.of(WeightedEdge.of("A", "B", new BigDecimal(1)));
        WeightedGraphPath<String> path2 = WeightedGraphPath.of(WeightedEdge.of("B", "C", new BigDecimal(10)));
        path1.joinToStart(path2);
    }

    @Test(expected = GraphException.class)
    public void testIncorrectJoinToEnd() throws GraphException {
        WeightedGraphPath<String> path1 = WeightedGraphPath.of(WeightedEdge.of("A", "B", new BigDecimal(1)));
        WeightedGraphPath<String> path2 = WeightedGraphPath.of(WeightedEdge.of("C", "D", new BigDecimal(10)));
        path1.joinToEnd(path2);
    }
}
