package org.test.distance.graph;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

/**
 * Set of unit test methods for WeightedEdge class.
 * @author Viktar Lebedzeu
 */
public class WeightedEdgeTest {
    /** Logger */
    private static final Logger logger = LoggerFactory.getLogger(WeightedEdge.class);

    @Test
    public void testEquals() {
        Assert.assertNotEquals(new WeightedEdge<>("AB", "CD", new BigDecimal(5)), new WeightedEdge<>("CD", "AB", new BigDecimal(5)));
        Assert.assertNotEquals(new WeightedEdge<>("AB", "CD", new BigDecimal(5)), new WeightedEdge<>("AB", "CD", new BigDecimal(10)));
        Assert.assertEquals(new WeightedEdge<>("AB", "CD", new BigDecimal(5)), new WeightedEdge<>("AB", "CD", new BigDecimal(5)));

        Assert.assertNotEquals(new WeightedEdge<>(123, 456, new BigDecimal(5.12345)), new WeightedEdge<>(456, 123, new BigDecimal(5.12345)));
        Assert.assertNotEquals(new WeightedEdge<>(123, 456, new BigDecimal(5.12345)), new WeightedEdge<>(123, 456, new BigDecimal(6.0)));
        Assert.assertEquals(new WeightedEdge<>(123, 456, new BigDecimal(5.12345)), new WeightedEdge<>(123, 456, new BigDecimal(5.12345)));
    }

    @Test
    public void testToString() {
        Assert.assertTrue(new WeightedEdge<>("AB", "CD", new BigDecimal(5)).toString()
                .endsWith("[weight=5,source=Vertex[key=AB],destination=Vertex[key=CD]]"));
        Assert.assertTrue(new WeightedEdge<>(123, 456, new BigDecimal(5.12345).setScale(5, BigDecimal.ROUND_HALF_UP)).toString()
                .endsWith("[weight=5.12345,source=Vertex[key=123],destination=Vertex[key=456]]"));
    }

    @Test
    public void testOf() {
        Assert.assertNotNull(WeightedEdge.of("AB", "CD", new BigDecimal(1)));
        Assert.assertNotNull(WeightedEdge.of("AB", "AB", new BigDecimal(5)));
        Assert.assertNotEquals(WeightedEdge.of("AB", "CD", new BigDecimal(1)), WeightedEdge.of("AB", "AB", new BigDecimal(1)));
        Assert.assertNotEquals(WeightedEdge.of("AB", "CD", new BigDecimal(1)), WeightedEdge.of("AB", "CD", new BigDecimal(5)));
        Assert.assertEquals(WeightedEdge.of("AB", "CD", new BigDecimal(10)), WeightedEdge.of("AB", "CD", new BigDecimal(10)));
    }

    @Test
    public void testIsCyclic() {
        Assert.assertFalse(WeightedEdge.of(1, 2, new BigDecimal(3)).isCyclic());
        Assert.assertTrue(WeightedEdge.of(1, 1, new BigDecimal(3)).isCyclic());
    }
}
