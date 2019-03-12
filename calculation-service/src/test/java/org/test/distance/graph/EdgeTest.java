package org.test.distance.graph;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Set of unit test methods for Edge
 * @author Viktar Lebedzeu
 */
public class EdgeTest {
    /** Logger */
    private static final Logger logger = LoggerFactory.getLogger(EdgeTest.class);

    @Test
    public void testEquals() {
        Assert.assertNotEquals(new Edge<>("A", "B"), new Edge<>("B", "A"));
        Assert.assertEquals(new Edge<>("A", "B"), new Edge<>("A", "B"));

        Assert.assertNotEquals(new Edge<>(1, 2), new Edge<>(2, 1));
        Assert.assertEquals(new Edge<>(3, 4), new Edge<>(3, 4));
    }

    @Test
    public void testToString() {
        Assert.assertTrue(new Edge<>("AB", "CD").toString()
                .endsWith("[source=Vertex[key=AB],destination=Vertex[key=CD]]"));
        Assert.assertTrue(new Edge<>(123, 456).toString()
                .endsWith("[source=Vertex[key=123],destination=Vertex[key=456]]"));
    }

    @Test
    public void testOf() {
        Assert.assertNotNull(Edge.of("AB", "CD"));
        Assert.assertNotNull(Edge.of("AB", "AB"));
        Assert.assertNotEquals(Edge.of("AB", "CD"), Edge.of("AB", "AB"));
        Assert.assertEquals(Edge.of("AB", "CD"), Edge.of("AB", "CD"));
    }

    @Test
    public void testIsCyclic() {
        Assert.assertFalse(Edge.of(1, 2).isCyclic());
        Assert.assertTrue(Edge.of(1, 1).isCyclic());
    }
}
