package org.test.distance.dto;

import java.util.List;

/**
 * Path data transfer object
 * @author Viktar Lebedzeu
 */
public class PathDto {
    private List<String> nodes;
    private Double totalLength;
    private Integer nodesCount;

    public PathDto(List<String> nodes, Double totalLength, Integer nodesCount) {
        this.nodes = nodes;
        this.totalLength = totalLength;
        this.nodesCount = nodesCount;
    }

    public List<String> getNodes() {
        return nodes;
    }

    public void setNodes(List<String> nodes) {
        this.nodes = nodes;
    }

    public Double getTotalLength() {
        return totalLength;
    }

    public void setTotalLength(Double totalLength) {
        this.totalLength = totalLength;
    }

    public Integer getNodesCount() {
        return nodesCount;
    }

    public void setNodesCount(Integer nodesCount) {
        this.nodesCount = nodesCount;
    }
}
