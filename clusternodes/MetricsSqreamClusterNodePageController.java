package com.apptomo.v4.aisqreamboard.clusternodes;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

// ===== 20260916 추가 시작 : SQream Cluster Nodes Page =====
@Controller
public class MetricsSqreamClusterNodePageController {
    @GetMapping("/aisqreamboard/cluster_nodes")
    public String page(){ return "thymeleaf/aisqreamboard/cluster_nodes"; }
}
// ===== 20260916 추가 끝 : SQream Cluster Nodes Page =====
