package org.test.distance.graph;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Pack of simple unit tests for Vertex class implementation
 * @author Viktar Lebedzeu
 */
public class VertexTest {
    /** Logger */
    private static final Logger logger = LoggerFactory.getLogger(Vertex.class);

    @Test
    public void testEquals() {
        // Assertion string vertices
        Assert.assertNotEquals(new Vertex<>("A"), new Vertex<>("B"));
        Assert.assertEquals(new Vertex<>("C"), new Vertex<>("C"));
        // Assertion integer vertices
        Assert.assertNotEquals(new Vertex<>(1), new Vertex<>(2));
        Assert.assertEquals(new Vertex<>(-1), new Vertex<>(-1));
        // Assertion long vertices
        Assert.assertNotEquals(new Vertex<>(-5L), new Vertex<>(Long.MAX_VALUE));
        Assert.assertEquals(new Vertex<>(-6L), new Vertex<>(-6L));
        // Assertion double vertices
        Assert.assertNotEquals(new Vertex<>(-5.0), new Vertex<>(Double.MAX_VALUE));
        Assert.assertEquals(new Vertex<>(1.356), new Vertex<>(1.356));
    }

    @Test
    public void testToString() {
        Assert.assertTrue(new Vertex<>("ABCD").toString().endsWith("[key=ABCD]"));
        Assert.assertTrue(new Vertex<>(12345).toString().endsWith("[key=12345]"));
        Assert.assertTrue(new Vertex<>(1234567890L).toString().endsWith("[key=1234567890]"));
        Assert.assertTrue(new Vertex<>(0.000025).toString().endsWith("[key=2.5E-5]"));
        Assert.assertTrue(new Vertex<>(1234567890.0987654321).toString().endsWith("[key=1.2345678900987654E9]"));
    }
}
