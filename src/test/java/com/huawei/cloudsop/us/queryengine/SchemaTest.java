package com.huawei.cloudsop.us.queryengine;

import com.huawei.cloudsop.us.queryengine.Connection.QueryResult;
import com.huawei.cloudsop.us.queryengine.Controller.QueryController;

import org.junit.Ignore;
import org.junit.Test;

//@Ignore
public class SchemaTest {

//    /**
//     * Tests MySQL schema. You can setup MySQL on VM using the following steps:
//     * https://github.com/zhouzhb/queryengine/blob/master/doc/MySQL-VM.txt
//     */
//    @Test public void testMySqlScheme() {
//        QueryController controller = new QueryController();
//        String query = "select count(*) from \"mysql\".\"test\"";
//        QueryResult res = controller.query(query);
//        System.out.println(">>>>> result: " + res.getContent());
//    }
//
//    /**
//     * Tests Druid schema. You can set up Druid on VM using the following steps:
//     * https://github.com/zhouzhb/queryengine/blob/master/doc/Druid-VM.txt
//     */
//    @Test public void testDruidScheme() {
//        QueryController controller = new QueryController();
//        String query = "select count(*) from \"druid\".\"test\"";
//        QueryResult res = controller.query(query);
//        System.out.println(">>>>> result: " + res.getContent());
//    }

    /**
     * Tests connection to Thrift JDBC server running on Spark: https://spark.apache.org/docs/latest/sql-distributed-sql-engine.html
     * https://github.com/zhouzhb/queryengine/blob/master/doc/Spark-VM.txt
     */
    @Test public void testThriftScheme() {
        QueryController controller = new QueryController();
//        String query = "select * from \"thrift\".\"employee\";";
        String query = "select count(*) from \"thrift\".\"test\";";
        QueryResult res = controller.query(query);
        System.out.println(">>>>> result: " + res.getContent());
    }
}
