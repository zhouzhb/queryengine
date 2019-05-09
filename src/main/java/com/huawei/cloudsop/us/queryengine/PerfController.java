package com.huawei.cloudsop.us.queryengine;

import org.apache.calcite.avatica.metrics.Timer;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for performance testing
 */
@RestController
public class PerfController {

    private final AtomicLong counter = new AtomicLong();

    private final CalciteQueryManager manager = new CalciteQueryManager();

    private final Map<String, PerfTimer> timers = new LinkedHashMap<>();

    private Timer getTimer(String name){
        return this.timers.computeIfAbsent(name, PerfTimer::new);
    }

    private Map<String, Object> getMetrics() {
        Map<String, Object> toReturn = new LinkedHashMap<>();
        this.timers.forEach((k, v) -> toReturn.put(k, v.getDuration()));
        return toReturn;
    }

    @RequestMapping("/perf")
    public QueryResult perf(
        @RequestParam(value = "sql", defaultValue = "") String sql) {

        // Query MySQL and measure query execution latency
        try (Timer.Context ignored = getTimer("MySQL").start()) {
            MySqlController c = new MySqlController();
            c.mysql("", sql);
        }

        // Query Druid and measure query execution latency
        try (Timer.Context ignored = getTimer("Druid").start()) {
            QueryController c = new QueryController();
            c.query("", sql);
        }

        // Generate perf metrics
        ObjectMapper mapper = new ObjectMapper();
        String perfMetricsJson = "{}";
        try {
            perfMetricsJson = mapper.writeValueAsString(getMetrics());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            perfMetricsJson = "{\"" + e.getMessage() + "\"}";
        }
        return new QueryResult(counter.incrementAndGet(), perfMetricsJson);
    }

    private static class PerfTimer implements Timer {
        final String name;
        private long from;
        private long to;

        PerfTimer(String name) {
            this.name = name;
        }

        @Override public Context start() {
            final AtomicBoolean closed = new AtomicBoolean();
            this.from = System.currentTimeMillis();
            return () -> {
                if (closed.compareAndSet(false, true)){
                    this.to = System.currentTimeMillis();
                }
            };
        }

        public long getDuration() {
            return this.to - this.from;
        }
    }

}
