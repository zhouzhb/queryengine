package com.huawei.cloudsop.us.queryengine;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ControllerTest {

    @Test public void testDruidMySqlJoinController() {
            DruidMySqlJoinController controller = new DruidMySqlJoinController();
        QueryResult result = controller.druidmysqljoin("","select\"t1\".*from\"mysql\".\"foodmart\"as\"t1\"join\"druid\".\"foodmart\"as\"t2\"on\"t1\".\"postal_code\"=\"t2\".\"postal_code\"and\"t2\".\"timestamp\">='1900-01-01T00:00:00.000Z'and\"t2\".\"timestamp\"<'1997-01-01T12:00:00.000Z'and\"t1\".\"postal_code\"=12422");
        assertEquals(31902, result.getContent().length());
    }
}