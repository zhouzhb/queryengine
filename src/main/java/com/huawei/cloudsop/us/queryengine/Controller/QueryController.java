package com.huawei.cloudsop.us.queryengine.Controller;

import java.util.concurrent.atomic.AtomicLong;

import com.huawei.cloudsop.us.queryengine.Manager.CalciteQueryManager;
import com.huawei.cloudsop.us.queryengine.Connection.QueryResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class QueryController {
    Logger logger = LoggerFactory.getLogger(QueryController.class);

    private final AtomicLong counter = new AtomicLong();

    private final CalciteQueryManager manager = new CalciteQueryManager();

    @RequestMapping("/query")
    public QueryResult query(
            @RequestParam(value="sql", defaultValue="") String sql) {
        logger.debug("query: " + sql);

        return new QueryResult(counter.incrementAndGet(),
                manager.query(CalciteQueryManager.DRUID_MYSQL_MODEL, sql));
    }
}
