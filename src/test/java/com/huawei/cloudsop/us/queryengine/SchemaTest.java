package com.huawei.cloudsop.us.queryengine;

import com.huawei.cloudsop.us.queryengine.Connection.QueryResult;
import com.huawei.cloudsop.us.queryengine.Controller.QueryController;

import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class SchemaTest {

    /**
     * Tests VM MySQL schema. VM MySQL should be set up using the following steps:
     * https://github.com/zhouzhb/queryengine/blob/val/doc/MySQL-VM.txt
     */
    @Test public void testMySqlScheme() {
        QueryController controller = new QueryController();
        String query = "select count(*) from \"mysql\".\"test\"";
        QueryResult res = controller.query(query);
        System.out.println(">>>>> result: " + res.getContent());
    }

    /**
     * Tests VM Druid schema. VM Druid should be set up using the following steps:
     * https://github.com/zhouzhb/queryengine/blob/val/doc/Druid-VM.txt
     */
    @Test public void testDruidScheme() {
        QueryController controller = new QueryController();
        String query = "select count(*) from \"druid\".\"test\"";
        QueryResult res = controller.query(query);
        System.out.println(">>>>> result: " + res.getContent());
    }
}
