package com.apptomo.v4.aisqreamboard.clusternodes;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

// ===== 20260916 추가 시작 : SQream Cluster Nodes Entity =====
@Getter @Setter @NoArgsConstructor
@Entity
@Table(name = "ax_metrics_sqream_cluster_nodes", schema = "public")
public class MetricsSqreamClusterNode {
    @Id @Column(name="id", nullable=false, updatable=false) private UUID id;
    @Column(name="server_id", nullable=false, length=100) private String serverId;
    @Column(name="hostname", length=255) private String hostname;
    @Column(name="hostip", length=50) private String hostip;
    @Column(name="global_uuid") private UUID globalUuid;
    @Column(name="collect_time") private LocalDateTime collectTime;
    @Column(name="create_dt") private LocalDateTime createDt;
    @Column(name="update_dt") private LocalDateTime updateDt;
    @Column(name="server_ip", length=32) private String serverIp;
    @Column(name="server_port") private Integer serverPort;
    @Column(name="connection_id") private Long connectionId;
    @Column(name="instance_id", length=32) private String instanceId;
    @Column(name="last_heartbeat") private Integer lastHeartbeat;
    @Column(name="connection_status", length=64) private String connectionStatus;
}
// ===== 20260916 추가 끝 : SQream Cluster Nodes Entity =====
