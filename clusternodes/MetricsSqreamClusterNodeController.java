package com.apptomo.v4.aisqreamboard.clusternodes;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

// ===== 20260916 추가 시작 : SQream Cluster Nodes API =====
@RestController @RequiredArgsConstructor
@RequestMapping("/api/aisqreamboard/cluster-nodes")
public class MetricsSqreamClusterNodeController {
    private final MetricsSqreamClusterNodeService service;
    @GetMapping
    public Page<MetricsSqreamClusterNode> search(
        @RequestParam(required=false) @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
        @RequestParam(required=false) @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss") LocalDateTime endTime,
        @RequestParam(required=false,defaultValue="") String keyword,
        @RequestParam(defaultValue="0") int page,@RequestParam(defaultValue="20") int size) {
        return service.search(startTime,endTime,keyword,page,size);
    }
    @GetMapping("/{globalUuid}")
    public List<MetricsSqreamClusterNode> detail(@PathVariable String globalUuid){ return service.detail(globalUuid); }
}
// ===== 20260916 추가 끝 : SQream Cluster Nodes API =====
