package com.huawei.cloudsop.us.queryengine;

import java.util.concurrent.atomic.AtomicLong;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class QueryController {

    private final AtomicLong counter = new AtomicLong();

    private final CalciteQueryManager manager = new CalciteQueryManager();

    @RequestMapping("/query")
    public QueryResult query(
            @RequestParam(value="model", defaultValue="") String model,
            @RequestParam(value="sql", defaultValue="") String sql) {
        return new QueryResult(counter.incrementAndGet(),
                manager.query(CalciteQueryManager.FOODMART, sql));
    }
}
