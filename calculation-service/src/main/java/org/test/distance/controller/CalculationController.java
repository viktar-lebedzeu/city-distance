package org.test.distance.controller;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.test.distance.dto.BaseResponse;
import org.test.distance.dto.PathDto;
import org.test.distance.dto.ResponseFactory;
import org.test.distance.dto.RouteDto;
import org.test.distance.dto.SuccessCollectionResponse;
import org.test.distance.graph.GraphPath;
import org.test.distance.graph.GraphResolver;
import org.test.distance.graph.Vertex;
import org.test.distance.graph.WeightedEdge;
import org.test.distance.graph.WeightedGraphPath;
import org.test.distance.service.FeignEdgeProducerService;

import javax.xml.ws.Response;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Viktar Lebedzeu
 */
@RestController
public class CalculationController {
    /** Logger */
    private static final Logger logger = LoggerFactory.getLogger(CalculationController.class);

    @Autowired
    private Environment environment;

    @Autowired
    private GraphResolver graphResolver;

    @Autowired
    private FeignEdgeProducerService<String, WeightedEdge<String>> producerService;

    @PostMapping("/paths/find")
    public ResponseEntity<? extends BaseResponse> findPaths(@RequestBody RouteDto routeDto) {
        if (StringUtils.isBlank(routeDto.getFrom()) || StringUtils.isBlank(routeDto.getTo())) {
            return new ResponseEntity<>(ResponseFactory.createErrorResponse(-100L,
                            "Required fields \"from\" or \"to\" is empty. " + routeDto.toString()), HttpStatus.OK);
        }

        final List<GraphPath<String>> paths =
                graphResolver.findAllPaths(routeDto.getFrom(), routeDto.getTo(), producerService);

        if (paths != null && !paths.isEmpty()) {
            return new ResponseEntity<>(
                    ResponseFactory.createSuccessCollectionResponse(convertPaths(paths), (long) paths.size()), HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(ResponseFactory.createErrorResponse(-200L,
                    "Can not find any path from " + routeDto.getFrom() + " to " + routeDto.getTo()), HttpStatus.OK);
        }
    }

    private static List<PathDto> convertPaths(List<GraphPath<String>> paths) {
        return paths.stream()
            .map(p -> new PathDto(p.toKeyList(), ((WeightedGraphPath) p).getWeightsSum().doubleValue(), p.getNodes().size()))
            .collect(Collectors.toList());
    }
}
