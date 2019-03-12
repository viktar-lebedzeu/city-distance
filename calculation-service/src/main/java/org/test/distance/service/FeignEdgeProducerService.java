package org.test.distance.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.test.distance.controller.ManagementServiceProxy;
import org.test.distance.dto.RouteDto;
import org.test.distance.dto.RouteIdsRequestDto;
import org.test.distance.graph.Edge;
import org.test.distance.graph.EdgeDirectionEnum;
import org.test.distance.graph.EdgeProducer;
import org.test.distance.graph.WeightedEdge;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Viktar Lebedzeu
 */
@Service
public class FeignEdgeProducerService<T, E extends Edge<T>> implements EdgeProducer<T, E> {
    /** Logger */
    private static final Logger logger = LoggerFactory.getLogger(FeignEdgeProducerService.class);

    @Autowired
    private ManagementServiceProxy managementServiceProxy;

    @Override
    public List<E> findEdges(T vertexKey, EdgeDirectionEnum direction) {
        RouteIdsRequestDto requestDto = new RouteIdsRequestDto(
                Collections.singletonList(vertexKey.toString()), (direction == EdgeDirectionEnum.REVERSE));
        return requestEdges(requestDto);
    }

    @Override
    public List<E> findEdges(Collection<T> vertexKeys, EdgeDirectionEnum direction) {
        final List<String> ids = vertexKeys.parallelStream().map(Object::toString).collect(Collectors.toList());
        RouteIdsRequestDto requestDto = new RouteIdsRequestDto(ids, (direction == EdgeDirectionEnum.REVERSE));
        return requestEdges(requestDto);
    }

    @SuppressWarnings("unchecked")
    private List<E> requestEdges(RouteIdsRequestDto requestDto) {
        final ResponseEntity<List<RouteDto>> routesResponse = managementServiceProxy.findRoutes(requestDto);

        if (routesResponse.getBody() == null) {
            return new ArrayList<>(0);
        }
        List<WeightedEdge<T>> edges = routesResponse.getBody().stream()
                .map(r -> WeightedEdge.of((T) r.getFrom(), (T) r.getTo(), new BigDecimal(r.getDistance())))
                .collect(Collectors.toList());
        return (List<E>) edges;
    }
}
