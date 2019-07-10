package com.huawei.cloudsop.us.queryengine;

import com.huawei.cloudsop.us.queryengine.Connection.QueryResult;
import com.huawei.cloudsop.us.queryengine.Controller.QueryController;

import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class SchemaTest {

    /**
     * Tests Calcite->Thrift->Hive querying: https://spark.apache.org/docs/latest/sql-distributed-sql-engine.html.
     * Precondition for this test is a VM set up using steps at https://github.com/zhouzhb/queryengine/blob/master/doc/Spark-VM.txt.
     */
    @Test public void testThriftHive() {
        QueryController controller = new QueryController();
        String query = "select * from \"thrift\".\"test\"";
        QueryResult res = controller.query(query);
        System.out.println("result: " + res.getContent());
    }

    /**
     * Tests Calcite->Thrift->MySQL querying.
     * Preconditions for this test are:
     * - VM is set up using steps at https://github.com/zhouzhb/queryengine/blob/master/doc/Spark-VM.txt.
     * - MySQL is set up using steps at https://community.hortonworks.com/articles/4671/sparksql-jdbc-federation.html.
     */
    @Test public void testThriftMySql() {
        QueryController controller = new QueryController();
        String query = "select * from \"thrift\".\"mysql_federated_sample\"";
        QueryResult res = controller.query(query);
        System.out.println("result: " + res.getContent());
    }

    /**
     * Tests Calcite->Thrift->(MySQL+CSV join) query.
     * Preconditions for this test are:
     * - VM is set up using steps at https://github.com/zhouzhb/queryengine/blob/master/doc/Spark-MySQL-CSV-Join.txt.
     */
    @Test public void testThriftMySqlCsvJoin() {
        QueryController controller = new QueryController();
        String query = "select \"t1\".*, \"t2\".* from \"thrift\".\"mysql_bigdata\" \"t1\" inner join \"thrift\".\"csv_bigdata\" \"t2\" on \"t1\".\"intInc1\" = \"t2\".\"intInc1\"";
        long t1 = System.currentTimeMillis();
        QueryResult res = controller.query(query);
        long t2 = System.currentTimeMillis();
        System.out.println("result: " + res.getContent());
        System.out.println("time: " + (t2 - t1) + " ms");
    }

    /**
     * Tests Calcite->(MySQL+CSV join) query.
     * Preconditions for this test are:
     * - VM is set up using steps at https://github.com/zhouzhb/queryengine/blob/master/doc/Spark-MySQL-CSV-Join.txt.
     */
    @Test public void testMySqlCsvJoin() {
        QueryController controller = new QueryController();
        String query = "select \"t1\".* from \"csv\".\"bigdata\" \"t1\" inner join \"mysql\".\"bigdata_tbl\" \"t2\" on \"t1\".\"intInc1\" = \"t2\".\"intInc1\"";
        long t1 = System.currentTimeMillis();
        QueryResult res = controller.query(query);
        long t2 = System.currentTimeMillis();
        System.out.println("result: " + res.getContent());
        System.out.println("time: " + (t2 - t1) + " ms");
    }
}
