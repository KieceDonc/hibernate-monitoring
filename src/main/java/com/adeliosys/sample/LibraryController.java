package com.adeliosys.sample;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Map;

@RestController
@Transactional
public class LibraryController {

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Autowired
    private DataSource dataSource;

    @GetMapping("/hibernate-stats")
    @Transactional(propagation = Propagation.SUPPORTS)
    public String getHibernateStats(@RequestParam(required = false) boolean clear) {
        return HibernateStatisticsUtil.generateStatsReport(entityManagerFactory, clear);
    }

    @GetMapping("/datasource-stats")
    @Transactional(propagation = Propagation.SUPPORTS)
    public Map<String, Object> getDatasourceStats() {
        HikariDataSource hikariDataSource = (HikariDataSource) dataSource;
        return Map.of(
                "totalConnections", hikariDataSource.getHikariPoolMXBean().getTotalConnections(),
                "activeConnections", hikariDataSource.getHikariPoolMXBean().getActiveConnections(),
                "threadsWaiting", hikariDataSource.getHikariPoolMXBean().getThreadsAwaitingConnection());
    }
}
