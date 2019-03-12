package org.test.distance.graph;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Unit test suite to test GraphResolver class
 * @author Viktar Lebedzeu
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class GraphResolverTest {
    /** Logger */
    private static final Logger logger = LoggerFactory.getLogger(GraphResolverTest.class);

    @Autowired
    private GraphResolver graphResolver;

    @Test
    public void testFindPaths() {
        Assert.assertNotNull(graphResolver);
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

        final List<GraphPath<String>> paths = graphResolver.findAllPaths("A", "D", producer);
        Assert.assertNotNull(paths);
        Assert.assertEquals(5, paths.size());
        final List<String> pathStrings = paths.stream()
                .map(GraphPath::toExtendedKeyListString)
                .sorted()
                .collect(Collectors.toList());
        // logger.info("Paths = " + pathStrings);
        Assert.assertEquals("[A, B, C, D] (16)", pathStrings.get(0));
        Assert.assertEquals("[A, B, C, G, D] (18)", pathStrings.get(1));
        Assert.assertEquals("[A, C, D] (12)", pathStrings.get(2));
        Assert.assertEquals("[A, C, G, D] (14)", pathStrings.get(3));
        Assert.assertEquals("[A, D] (12)", pathStrings.get(4));
    }

    @Test
    public void testNotconnected() {
        Assert.assertNotNull(graphResolver);
        InMemoryEdgeProducer<String, Edge<String>> producer = new InMemoryEdgeProducer<String, Edge<String>>()
                .addEdge(WeightedEdge.of("A", "B", new BigDecimal(1)), EdgeDirectionEnum.TWO_WAY)
                .addEdge(WeightedEdge.of("A", "D", new BigDecimal(1)), EdgeDirectionEnum.TWO_WAY)
                .addEdge(WeightedEdge.of("B", "C", new BigDecimal(1)), EdgeDirectionEnum.TWO_WAY)
                .addEdge(WeightedEdge.of("B", "D", new BigDecimal(1)), EdgeDirectionEnum.TWO_WAY)
                .addEdge(WeightedEdge.of("C", "D", new BigDecimal(1)), EdgeDirectionEnum.TWO_WAY)

                // .addEdge(WeightedEdge.of("C", "F", new BigDecimal(1)), EdgeDirectionEnum.TWO_WAY)

                .addEdge(WeightedEdge.of("E", "F", new BigDecimal(1)), EdgeDirectionEnum.TWO_WAY)
                .addEdge(WeightedEdge.of("E", "G", new BigDecimal(1)), EdgeDirectionEnum.TWO_WAY)
                .addEdge(WeightedEdge.of("E", "H", new BigDecimal(1)), EdgeDirectionEnum.TWO_WAY)
                .addEdge(WeightedEdge.of("F", "G", new BigDecimal(1)), EdgeDirectionEnum.TWO_WAY)
                .addEdge(WeightedEdge.of("G", "H", new BigDecimal(1)), EdgeDirectionEnum.TWO_WAY);

        final List<GraphPath<String>> paths = graphResolver.findAllPaths("A", "H", producer);
        // logger.info("Paths = " + paths);
        Assert.assertNotNull(paths);
        Assert.assertEquals(0, paths.size());
    }
}
