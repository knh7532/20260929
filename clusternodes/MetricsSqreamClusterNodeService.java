package com.apptomo.v4.aisqreamboard.clusternodes;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

// ===== 20260916 추가 시작 : SQream Cluster Nodes Service =====
@Service @RequiredArgsConstructor @Transactional(readOnly=true)
public class MetricsSqreamClusterNodeService {
    private final MetricsSqreamClusterNodeRepository repository;
    public Page<MetricsSqreamClusterNode> search(LocalDateTime startTime, LocalDateTime endTime, String keyword, int page, int size) {
        LocalDateTime s=startTime!=null?startTime:LocalDateTime.of(2000,1,1,0,0,0);
        LocalDateTime e=endTime!=null?endTime:LocalDateTime.of(2099,12,31,23,59,59);
        String k=keyword==null?"":keyword.trim();
        PageRequest p=PageRequest.of(Math.max(page,0),Math.max(size,1),Sort.by(Sort.Direction.DESC,"collectTime"));
        return repository.search(s,e,k,p);
    }
    public List<MetricsSqreamClusterNode> detail(String globalUuid) {
        return repository.findTop20ByGlobalUuidOrderByCollectTimeDesc(UUID.fromString(globalUuid));
    }
}
// ===== 20260916 추가 끝 : SQream Cluster Nodes Service =====
