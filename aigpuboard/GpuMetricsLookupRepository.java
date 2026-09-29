package com.apptomo.v4.aigpuboard;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.*;

@Repository
@RequiredArgsConstructor
public class GpuMetricsLookupRepository {
    private final EntityManager em;

    // 20260916 추가: GPU 5개 테이블 공통 목록 조회
    public List<Object[]> search(String table, List<String> columns, String timeColumn, String keyColumn,
                                 String startTime, String endTime, String search1Column, String search1,
                                 String search2Column, String search2, int size) {
        StringBuilder sql = new StringBuilder("SELECT " + String.join(",", columns) + " FROM public." + table + " WHERE 1=1 ");
        if (timeColumn != null && startTime != null && !startTime.isEmpty()) sql.append(" AND ").append(timeColumn).append(" >= CAST(:startTime AS timestamp) ");
        if (timeColumn != null && endTime != null && !endTime.isEmpty()) sql.append(" AND ").append(timeColumn).append(" <= CAST(:endTime AS timestamp) ");
        if (search1Column != null && search1 != null && !search1.trim().isEmpty()) sql.append(" AND CAST(").append(search1Column).append(" AS varchar) ILIKE :search1 ");
        if (search2Column != null && search2 != null && !search2.trim().isEmpty()) sql.append(" AND CAST(").append(search2Column).append(" AS varchar) ILIKE :search2 ");
        sql.append(" ORDER BY ").append(timeColumn != null ? timeColumn : keyColumn).append(" DESC NULLS LAST LIMIT :size");
        Query q=em.createNativeQuery(sql.toString());
        if (timeColumn != null && startTime != null && !startTime.isEmpty()) q.setParameter("startTime",startTime);
        if (timeColumn != null && endTime != null && !endTime.isEmpty()) q.setParameter("endTime",endTime);
        if (search1Column != null && search1 != null && !search1.trim().isEmpty()) q.setParameter("search1","%"+search1.trim()+"%");
        if (search2Column != null && search2 != null && !search2.trim().isEmpty()) q.setParameter("search2","%"+search2.trim()+"%");
        q.setParameter("size", Math.max(1, Math.min(size, 1000)));
        return q.getResultList();
    }

    // 20260916 추가: 클릭한 UUID 기준 전체 컬럼 상세 조회(id 제외)
    public List<Object[]> detail(String table, List<String> columns, String keyColumn, String key) {
        Query q=em.createNativeQuery("SELECT " + String.join(",",columns) + " FROM public."+table+" WHERE CAST("+keyColumn+" AS varchar)=:key LIMIT 100");
        q.setParameter("key",key);
        return q.getResultList();
    }
}
