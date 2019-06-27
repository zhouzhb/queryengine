package com.huawei.cloudsop.us.queryengine;

import com.huawei.cloudsop.us.queryengine.Connection.QueryResult;
import com.huawei.cloudsop.us.queryengine.Controller.QueryController;

import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class SchemaTest {

    /**
     * Tests connection to Thrift JDBC server running on Spark: https://spark.apache.org/docs/latest/sql-distributed-sql-engine.html
     * Precondition for this test is a VM set up using steps at: https://github.com/zhouzhb/queryengine/blob/master/doc/Spark-VM.txt
     */
    @Test public void testThriftScheme() {
        QueryController controller = new QueryController();
        String query = "select * from \"thrift\".\"test\"";
        QueryResult res = controller.query(query);
        System.out.println("result: " + res.getContent());
    }
}
