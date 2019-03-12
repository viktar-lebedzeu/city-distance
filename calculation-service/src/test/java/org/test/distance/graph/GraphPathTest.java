package org.test.distance.graph;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

/**
 * Unit test suite for GraphPath
 * @author Viktar Lebedzeu
 */
public class GraphPathTest {
    /** Logger */
    private static final Logger logger = LoggerFactory.getLogger(GraphPath.class);

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Test
    public void testEquals() {
        Assert.assertNotEquals(GraphPath.of("A", "B", "C", "D", "E"), GraphPath.of("A", "B", "C", "E", "D"));
        Assert.assertEquals(GraphPath.of("A", "B", "C", "D", "E"), GraphPath.of("A", "B", "C", "D", "E"));
    }

    @Test
    public void testToString() {
        // logger.debug(GraphPath.of("A", "B", "C", "D", "E").toString());
        Assert.assertTrue(GraphPath.of("A", "B", "C", "D", "E").toString().endsWith("[nodes=[A, B, C, D, E]]"));
        Assert.assertTrue(GraphPath.of("E", "D", "C", "B", "A").toString().endsWith("[nodes=[E, D, C, B, A]]"));
    }

    @Test
    public void testOf() {
        GraphPath path = GraphPath.of(Edge.of("A", "B"));
        Assert.assertNotNull(path);
        Assert.assertEquals(2, path.getNodes().size());
        Assert.assertFalse(path.isCyclic());

        path = GraphPath.of(Edge.of("A", "A"));
        Assert.assertNotNull(path);
        Assert.assertEquals(2, path.getNodes().size());
        Assert.assertTrue(path.isCyclic());

        path = GraphPath.of("A", "B", "C", "D");
        Assert.assertNotNull(path);
        Assert.assertEquals(4, path.getNodes().size());
        Assert.assertFalse(path.isCyclic());

        path = GraphPath.of("A", "B", "C", "D", "A");
        Assert.assertNotNull(path);
        Assert.assertEquals(5, path.getNodes().size());
        Assert.assertTrue(path.isCyclic());
    }

    @Test
    public void testIsCyclic() {
        Assert.assertFalse(GraphPath.of("A", "B", "C", "D", "E").isCyclic());
        Assert.assertTrue(GraphPath.of("A", "B", "C", "B", "E").isCyclic());
        Assert.assertTrue(GraphPath.of("A", "B", "B", "B", "E").isCyclic());
        Assert.assertTrue(GraphPath.of("A", "B", "C", "D", "E", "D", "C", "B", "A").isCyclic());
    }

    @Test
    public void testGetFirstLast() {
        GraphPath<String> path = GraphPath.of("A", "B", "C", "D", "E");
        final Optional<Vertex<String>> firstNodeInPath = path.getFirstNodeInPath();
        final Optional<Vertex<String>> lastNodeInPath = path.getLastNodeInPath();
        Assert.assertTrue(firstNodeInPath.isPresent());
        Assert.assertEquals("A", firstNodeInPath.get().getKey());

        Assert.assertTrue(lastNodeInPath.isPresent());
        Assert.assertEquals("E", lastNodeInPath.get().getKey());
    }

    @Test
    public void testJoining() throws GraphException {
        GraphPath<String> path1 = GraphPath.of("A", "B", "C");
        GraphPath<String> path2 = GraphPath.of("C", "D", "E");
        Assert.assertTrue(path1.canJoinToEnd(path2));
        Assert.assertFalse(path2.canJoinToEnd(path1));
        Assert.assertFalse(path1.canJoinToEnd(path1));

        Assert.assertTrue(path2.canJoinToStart(path1));
        Assert.assertFalse(path1.canJoinToStart(path2));
        Assert.assertFalse(path1.canJoinToStart(path1));

        path1 = GraphPath.of("A", "B", "C", "A");
        path2 = GraphPath.of("A", "C", "D", "A");

        Assert.assertTrue(path1.canJoinToEnd(path2));
        Assert.assertTrue(path1.canJoinToEnd(path1));
        Assert.assertTrue(path2.canJoinToEnd(path1));
        Assert.assertTrue(path2.canJoinToEnd(path2));
        Assert.assertTrue(path1.canJoinToStart(path2));
        Assert.assertTrue(path1.canJoinToStart(path1));
        Assert.assertTrue(path2.canJoinToStart(path1));
        Assert.assertTrue(path2.canJoinToStart(path2));
    }

    @Test
    public void testJoins() throws GraphException {
        GraphPath<String> path1 = GraphPath.of("A", "B", "C");
        GraphPath<String> path2 = GraphPath.of("C", "D", "E");

        GraphPath<String> joinedPath = path1.joinToEnd(path2);
        Assert.assertEquals("[A, B, C, D, E]", joinedPath.toKeyListString());
        joinedPath = path2.joinToStart(path1);
        Assert.assertEquals("[A, B, C, D, E]", joinedPath.toKeyListString());

        // Joining cyclic paths
        GraphPath<String> path3 = GraphPath.of("A", "B", "C", "D", "A");
        Assert.assertEquals("[A, B, C, D, A, B, C, D, A]", path3.joinToEnd(path3).toKeyListString());
        Assert.assertEquals("[A, B, C, D, A, B, C, D, A]", path3.joinToStart(path3).toKeyListString());


        // Testing incorrect joins
        exceptionRule.expect(GraphException.class);
        path2.joinToEnd(path1);
        path1.joinToStart(path2);
    }
}
