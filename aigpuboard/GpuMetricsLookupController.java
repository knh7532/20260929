package com.apptomo.v4.aigpuboard;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

// ===== 20260916 추가 시작 : GPU Metrics 공통 조회 Controller =====
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/aigpuboard")
public class GpuMetricsLookupController {

    private final GpuMetricsLookupRepository repository;

    /**
     * GPU Metrics 목록 조회
     */
    @GetMapping("/{type}")
    public ResponseEntity<Map<String, Object>> search(
            @PathVariable String type,
            @RequestParam(required = false) String startTime,
            @RequestParam(required = false) String endTime,
            @RequestParam(required = false) String search1,
            @RequestParam(required = false) String search2,
            @RequestParam(defaultValue = "100") int size) {

        Config c = config(type);

        // 20260916 추가
        // Repository.search()는 검색조건 3개까지 받을 수 있음.
        // 현재 GPU Metrics 화면은 search1/search2 두 개만 사용하므로
        // 세 번째 검색조건은 null 처리.
        List<Object[]> rows = repository.search(
                c.table,
                c.main,
                c.time,
                c.key,
                c.search1Column,
                search1,
                c.search2Column,
                search2,
                null,
                null,
                size
        );

        List<Map<String, Object>> data = convert(rows, c.main);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("rows", data);
        result.put("total", data.size());

        return ResponseEntity.ok(result);
    }

    /**
     * GPU Metrics 상세 조회
     */
    @GetMapping("/{type}/detail")
    public ResponseEntity<Map<String, Object>> detail(
            @PathVariable String type,
            @RequestParam String key) {

        Config c = config(type);

        // 20260916 추가
        // Repository.detail() 반환형이 List<Object[]>이므로
        // List로 받은 뒤 첫 번째 ROW를 상세 데이터로 사용.
        List<Object[]> rows = repository.detail(
                c.table,
                c.detail,
                c.key,
                key
        );

        if (rows == null || rows.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Object[] row = rows.get(0);

        return ResponseEntity.ok(
                convertOne(row, c.detail)
        );
    }

    /**
     * 화면 TYPE별 테이블/컬럼 설정
     */
    private Config config(String type) {

        switch (type) {

            /*
             * ============================================================
             * NVIDIA SMI INFO
             * ============================================================
             */
            case "nvidia-smi":

                return new Config(
                        "public.ax_metrics_gpu_nvidia_smi_info",

                        // 20260916 추가 : 메인 화면 컬럼
                        list(
                                "global_uuid",
                                "collect_time",
                                "driver_version",
                                "cuda_version",
                                "hostname",
                                "hostip",
                                "server_id"
                        ),

                        // 20260916 추가 : 상세 화면 컬럼 (ID 제외)
                        list(
                                "snapshot_time",
                                "driver_version",
                                "cuda_version",
                                "create_dt",
                                "update_dt",
                                "hostname",
                                "hostip",
                                "global_uuid",
                                "collect_time",
                                "server_id"
                        ),

                        // 시간 기준 컬럼
                        "collect_time",

                        // 상세 조회 KEY
                        "global_uuid",

                        // 검색조건
                        "hostname",
                        "server_id"
                );


            /*
             * ============================================================
             * DCGMI METRIC
             * ============================================================
             */
            case "dcgmi":

                return new Config(
                        "public.ax_metrics_gpu_dcgmi_metric",

                        // 20260916 추가 : 메인 화면
                        list(
                                "gpu_uuid",
                                "nvidia_smi_id",
                                "snapshot_time",
                                "gpu_id",
                                "gi_id",
                                "ci_id",
                                "entity_type",
                                "uuid",
                                "device_name",
                                "driver_version",
                                "persistence_mode",
                                "sm_clock_mhz",
                                "mem_clock_mhz",
                                "temperature_c",
                                "power_usage_w",
                                "memory_used_mib",
                                "memory_total_mib",
                                "gpu_util_percent",
                                "gr_engine_active_percent",
                                "sm_active_percent",
                                "sm_occupancy_percent",
                                "dram_active_percent"
                        ),

                        // 20260916 추가 : 상세 화면 (ID 제외)
                        list(
                                "nvidia_smi_id",
                                "snapshot_time",
                                "gpu_id",
                                "gi_id",
                                "ci_id",
                                "entity_type",
                                "uuid",
                                "gpu_uuid",
                                "mig_uuid",
                                "mig_instance_id",
                                "device_name",
                                "driver_version",
                                "persistence_mode",
                                "sm_clock_mhz",
                                "mem_clock_mhz",
                                "temperature_c",
                                "power_usage_w",
                                "clock_throttle_reasons",
                                "memory_used_mib",
                                "memory_total_mib",
                                "bar1_used_mib",
                                "bar1_total_mib",
                                "gpu_util_percent",
                                "mem_copy_util_percent",
                                "gr_engine_active_percent",
                                "sm_active_percent",
                                "sm_occupancy_percent",
                                "tensor_core_active_percent",
                                "dram_active_percent",
                                "enc_util_percent",
                                "dec_util_percent",
                                "pcie_bytes_transmitted",
                                "pcie_bytes_received",
                                "nvlink_bytes_transmitted",
                                "nvlink_bytes_received",
                                "xid_errors",
                                "ecc_sbe_volatile_total",
                                "ecc_dbe_volatile_total",
                                "ecc_sbe_aggregate_total",
                                "ecc_dbe_aggregate_total",
                                "gpu_util_samples",
                                "mem_util_samples",
                                "create_dt",
                                "update_dt"
                        ),

                        // 시간
                        "snapshot_time",

                        // 상세 KEY
                        "gpu_uuid",

                        // 검색
                        "gpu_id",
                        "gi_id"
                );


            /*
             * ============================================================
             * GPU DEVICE
             * ============================================================
             */
            case "device":

                return new Config(
                        "public.ax_metrics_gpu_device",

                        // 20260916 추가 : 메인 화면
                        list(
                                "gpu_uuid",
                                "nvidia_smi_id",
                                "gpu_id",
                                "gpu_name",
                                "persistence_mode",
                                "bus_id",
                                "display_active",
                                "ecc_mode",
                                "fan_speed",
                                "temperature_c",
                                "performance_state",
                                "power_usage_w",
                                "power_cap_w",
                                "gpu_memory_used_mib",
                                "gpu_memory_total_mib",
                                "gpu_util",
                                "compute_mode",
                                "mig_mode"
                        ),

                        // 20260916 추가 : 상세 화면
                        list(
                                "nvidia_smi_id",
                                "gpu_id",
                                "gpu_uuid",
                                "gpu_name",
                                "persistence_mode",
                                "bus_id",
                                "display_active",
                                "ecc_mode",
                                "fan_speed",
                                "temperature_c",
                                "performance_state",
                                "power_usage_w",
                                "power_cap_w",
                                "gpu_memory_used_mib",
                                "gpu_memory_total_mib",
                                "gpu_util",
                                "compute_mode",
                                "mig_mode",
                                "create_dt",
                                "update_dt"
                        ),

                        // 시간
                        "create_dt",

                        // 상세 KEY
                        "gpu_uuid",

                        // 검색
                        "gpu_id",
                        "gpu_name"
                );


            /*
             * ============================================================
             * MIG DEVICE
             * ============================================================
             */
            case "mig-device":

                return new Config(
                        "public.ax_metrics_gpu_mig_device",

                        // 20260916 추가 : 메인 화면
                        list(
                                "nvidia_smi_id",
                                "gpu_id",
                                "gi_id",
                                "ci_id",
                                "mig_index",
                                "mig_uuid",
                                "profile_name",
                                "memory_used_mib",
                                "memory_total_mib",
                                "bar1_used_mib",
                                "bar1_total_mib",
                                "sm_count",
                                "ecc_errors",
                                "eng_copy",
                                "eng_enc",
                                "eng_dec",
                                "eng_ofa",
                                "eng_jpg"
                        ),

                        // 20260916 추가 : 상세 화면
                        list(
                                "nvidia_smi_id",
                                "gpu_id",
                                "gi_id",
                                "ci_id",
                                "mig_index",
                                "mig_uuid",
                                "profile_name",
                                "memory_used_mib",
                                "memory_total_mib",
                                "bar1_used_mib",
                                "bar1_total_mib",
                                "sm_count",
                                "ecc_errors",
                                "eng_copy",
                                "eng_enc",
                                "eng_dec",
                                "eng_ofa",
                                "eng_jpg",
                                "create_dt",
                                "update_dt"
                        ),

                        // 시간
                        "create_dt",

                        // MIG에는 gpu_uuid가 없으므로 mig_uuid 사용
                        "mig_uuid",

                        // 검색
                        "gpu_id",
                        "gi_id"
                );


            /*
             * ============================================================
             * GPU PROCESS
             * ============================================================
             */
            case "process":

                return new Config(
                        "public.ax_metrics_gpu_process",

                        // 20260916 추가 : 메인 화면
                        list(
                                "gpu_uuid",
                                "nvidia_smi_id",
                                "pid",
                                "gpu_id",
                                "gi_id",
                                "ci_id",
                                "mig_uuid",
                                "process_type",
                                "process_name",
                                "memory_used_mib",
                                "gpu_memory_used_mib",
                                "gpu_utilization_percent",
                                "user_name",
                                "cpu_usage_percent",
                                "memory_usage_percent",
                                "physical_memory_kib",
                                "virtual_memory_kib",
                                "status",
                                "elapsed_time",
                                "diff_time",
                                "start_time_str",
                                "command",
                                "physical_memory_mib",
                                "virtual_memory_mib",
                                "start_time"
                        ),

                        // 20260916 추가 : 상세 화면
                        list(
                                "nvidia_smi_id",
                                "pid",
                                "gpu_id",
                                "gi_id",
                                "ci_id",
                                "gpu_uuid",
                                "mig_uuid",
                                "process_type",
                                "process_name",
                                "memory_used_mib",
                                "gpu_memory_used_mib",
                                "gpu_utilization_percent",
                                "user_name",
                                "cpu_usage_percent",
                                "memory_usage_percent",
                                "physical_memory_kib",
                                "virtual_memory_kib",
                                "status",
                                "elapsed_time",
                                "diff_time",
                                "start_time_str",
                                "command",
                                "physical_memory_mib",
                                "virtual_memory_mib",
                                "create_dt",
                                "update_dt",
                                "start_time"
                        ),

                        // 시간
                        "start_time",

                        // 상세 KEY
                        "gpu_uuid",

                        // 검색
                        "gpu_id",
                        "pid"
                );

            default:
                throw new IllegalArgumentException(
                        "지원하지 않는 GPU Metrics type 입니다: " + type
                );
        }
    }


    // ============================================================
    // Repository 결과 → JSON 변환
    // ============================================================

    private List<Map<String, Object>> convert(
            List<Object[]> rows,
            List<String> columns) {

        List<Map<String, Object>> result = new ArrayList<>();

        if (rows == null) {
            return result;
        }

        for (Object[] row : rows) {
            result.add(
                    convertOne(row, columns)
            );
        }

        return result;
    }


    private Map<String, Object> convertOne(
            Object[] row,
            List<String> columns) {

        Map<String, Object> result = new LinkedHashMap<>();

        if (row == null) {
            return result;
        }

        for (int i = 0; i < columns.size(); i++) {

            Object value =
                    i < row.length
                            ? row[i]
                            : null;

            result.put(
                    columns.get(i),
                    value
            );
        }

        return result;
    }


    private List<String> list(String... values) {
        return Arrays.asList(values);
    }


    // ============================================================
    // GPU 화면별 설정
    // ============================================================

    private static class Config {

        private final String table;

        private final List<String> main;

        private final List<String> detail;

        private final String time;

        private final String key;

        private final String search1Column;

        private final String search2Column;


        private Config(
                String table,
                List<String> main,
                List<String> detail,
                String time,
                String key,
                String search1Column,
                String search2Column) {

            this.table = table;
            this.main = main;
            this.detail = detail;
            this.time = time;
            this.key = key;
            this.search1Column = search1Column;
            this.search2Column = search2Column;
        }
    }
}
// ===== 20260916 추가 끝 : GPU Metrics 공통 조회 Controller =====