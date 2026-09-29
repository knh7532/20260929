package com.apptomo.v4.aisqreamboard.clusternodes;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

// ===== 20260916 추가 시작 : SQream Cluster Nodes Repository =====
public interface MetricsSqreamClusterNodeRepository extends JpaRepository<MetricsSqreamClusterNode, UUID> {
    @Query(value="SELECT * FROM public.ax_metrics_sqream_cluster_nodes n " +
            "WHERE n.collect_time >= :startTime AND n.collect_time <= :endTime " +
            "AND (COALESCE(n.hostname,'') ILIKE CONCAT('%',:keyword,'%') " +
            "OR COALESCE(n.instance_id,'') ILIKE CONCAT('%',:keyword,'%') " +
            "OR CAST(n.global_uuid AS varchar) ILIKE CONCAT('%',:keyword,'%'))",
           countQuery="SELECT COUNT(*) FROM public.ax_metrics_sqream_cluster_nodes n " +
            "WHERE n.collect_time >= :startTime AND n.collect_time <= :endTime " +
            "AND (COALESCE(n.hostname,'') ILIKE CONCAT('%',:keyword,'%') " +
            "OR COALESCE(n.instance_id,'') ILIKE CONCAT('%',:keyword,'%') " +
            "OR CAST(n.global_uuid AS varchar) ILIKE CONCAT('%',:keyword,'%'))", nativeQuery=true)
    Page<MetricsSqreamClusterNode> search(@Param("startTime") LocalDateTime startTime,
                                          @Param("endTime") LocalDateTime endTime,
                                          @Param("keyword") String keyword, Pageable pageable);
    List<MetricsSqreamClusterNode> findTop20ByGlobalUuidOrderByCollectTimeDesc(UUID globalUuid);
}
// ===== 20260916 추가 끝 : SQream Cluster Nodes Repository =====
