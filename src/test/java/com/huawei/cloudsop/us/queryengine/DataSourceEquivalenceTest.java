package com.huawei.cloudsop.us.queryengine;

import com.huawei.cloudsop.us.queryengine.Connection.QueryResult;
import com.huawei.cloudsop.us.queryengine.Controller.QueryController;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DataSourceEquivalenceTest {

    private String q1 = "select count(*) as c,"
        + " \"brand_name\" from __DB__"
        + " where \"timestamp\" >= '1996-01-01 00:00:00 UTC'"
        + " and \"timestamp\" < '1997-02-01 00:00:00 UTC'"
        + " and \"product_id\" = 1020"
        + " group by \"brand_name\"";

    private String q2 = "select sum(\"store_sales\") / sum(\"store_cost\") as \"profitRatio\","
        + " \"brand_name\" from __DB__"
        + " where \"timestamp\" >= '1996-01-01 00:00:00 UTC'"
        + " and \"timestamp\" < '1997-02-01 00:00:00 UTC'"
        + " and \"store_cost\" > 0"
        + " group by \"brand_name\"";

    private String q3 = "select sum(\"store_sales\") + sum(\"store_cost\"),"
        + " \"brand_name\" from __DB__"
        + " where \"timestamp\" >= '1996-01-01 00:00:00 UTC'"
        + " and \"timestamp\" < '1997-02-01 00:00:00 UTC'"
        + " group by \"brand_name\"";

    private String q4 = "select 100 * sum(\"store_sales\") / (sum(\"store_sales\")"
        + " + sum(\"store_cost\")), \"brand_name\" from __DB__"
        + " where \"timestamp\" >= '1996-01-01 00:00:00 UTC'"
        + " and \"timestamp\" < '1997-02-01 00:00:00 UTC'"
        + " group by \"brand_name\"";

    private String q5 = "select distinct \"brand_name\", \"country\" from __DB__"
        + " where \"timestamp\" >= '1996-01-01 00:00:00 UTC'"
        + " and \"timestamp\" < '1997-02-01 00:00:00 UTC'";

    private String q6 = "select sum(\"store_sales\") + sum(\"store_cost\"), \"brand_name\" from __DB__"
        + " where \"timestamp\" >= '1996-01-01 00:00:00 UTC'"
        + " and \"timestamp\" < '1997-02-01 00:00:00 UTC'"
        + " and (\"store_sales\" > 0 or \"store_cost\" > 0)"
        + " group by \"brand_name\"";

    private String q7 = "select sum(\"store_sales\") / sum(\"store_cost\") as \"profitRatio\", \"brand_name\" from __DB__"
        + " where \"timestamp\" >= '1996-01-01 00:00:00 UTC'"
        + " and \"timestamp\" < '1997-02-01 00:00:00 UTC'"
        + " group by \"brand_name\"";

    private String q8 = "select count(*) as c, \"brand_name\" from __DB__"
        + " where \"timestamp\" >= '1996-01-01 00:00:00 UTC'"
        + " and \"timestamp\" < '1997-02-01 00:00:00 UTC'"
        + " group by \"brand_name\"";

    private String q9 = "select count(*) as c, \"brand_name\", \"country\" from __DB__"
        + " where \"timestamp\" >= '1996-01-01 00:00:00 UTC'"
        + " and \"timestamp\" < '1997-02-01 00:00:00 UTC'"
        + " and \"product_id\" = 1020"
        + " group by \"brand_name\", \"country\", floor(\"timestamp\" to DAY)";

    private String q10 = "select sum(\"store_sales\") + sum(\"store_cost\"), \"brand_name\", \"country\" from __DB__"
        + "where \"timestamp\" >= '1996-01-01 00:00:00 UTC'"
        + " and \"timestamp\" < '1997-02-01 00:00:00 UTC'"
        + " and (\"store_cost\" > 0 or \"store_sales\" > 0)"
        + " group by \"brand_name\", \"country\", floor(\"timestamp\" to DAY)";



    @Test public void testDruidMySqlEquivalence1() {
        QueryController controller = new QueryController();
        String mySqlQuery = q1.replace("__DB__", "\"foodmart-mysql\".\"foodmart\"");
        String druidQuery = q1.replace("__DB__", "\"foodmart\".\"foodmart\"");;
        QueryResult druidResult = controller.query(mySqlQuery);
        QueryResult mySqlResult = controller.query(druidQuery);
        assertEquals(mySqlResult.getContent(), druidResult.getContent());
    }

    @Test public void testDruidMySqlEquivalence2() {
        QueryController controller = new QueryController();
        String mySqlQuery = q2.replace("__DB__", "\"foodmart-mysql\".\"foodmart\"");
        String druidQuery = q2.replace("__DB__", "\"foodmart\".\"foodmart\"");;
        QueryResult druidResult = controller.query(mySqlQuery);
        QueryResult mySqlResult = controller.query(druidQuery);
        assertEquals(mySqlResult.getContent(), druidResult.getContent());
    }

    @Test public void testDruidMySqlEquivalence3(){
        QueryController controller = new QueryController();
        String mySqlQuery = q3.replace("__DB__", "\"foodmart-mysql\".\"foodmart\"");
        String druidQuery = q3.replace("__DB__", "\"foodmart\".\"foodmart\"");;
        QueryResult druidResult = controller.query(mySqlQuery);
        QueryResult mySqlResult = controller.query(druidQuery);
        assertEquals(mySqlResult.getContent(), druidResult.getContent());
    }

    @Test public void testDruidMySqlEquivalence4() {
        QueryController controller = new QueryController();
        String mySqlQuery = q4.replace("__DB__", "\"foodmart-mysql\".\"foodmart\"");
        String druidQuery = q4.replace("__DB__", "\"foodmart\".\"foodmart\"");;
        QueryResult druidResult = controller.query(mySqlQuery);
        QueryResult mySqlResult = controller.query(druidQuery);
        assertEquals(mySqlResult.getContent(), druidResult.getContent());
    }

    @Test public void testDruidMySqlEquivalence5() {
        QueryController controller = new QueryController();
        String mySqlQuery = q5.replace("__DB__", "\"foodmart-mysql\".\"foodmart\"");
        String druidQuery = q5.replace("__DB__", "\"foodmart\".\"foodmart\"");;
        QueryResult druidResult = controller.query(mySqlQuery);
        QueryResult mySqlResult = controller.query(druidQuery);
        assertEquals(mySqlResult.getContent(), druidResult.getContent());
    }

    @Test public void testDruidMySqlEquivalence6() {
        QueryController controller = new QueryController();
        String mySqlQuery = q6.replace("__DB__", "\"foodmart-mysql\".\"foodmart\"");
        String druidQuery = q6.replace("__DB__", "\"foodmart\".\"foodmart\"");;
        QueryResult druidResult = controller.query(mySqlQuery);
        QueryResult mySqlResult = controller.query(druidQuery);
        assertEquals(mySqlResult.getContent(), druidResult.getContent());
    }

    @Test public void testDruidMySqlEquivalence7() {
        QueryController controller = new QueryController();
        String mySqlQuery = q7.replace("__DB__", "\"foodmart-mysql\".\"foodmart\"");
        String druidQuery = q7.replace("__DB__", "\"foodmart\".\"foodmart\"");;
        QueryResult druidResult = controller.query(mySqlQuery);
        QueryResult mySqlResult = controller.query(druidQuery);
        assertEquals(mySqlResult.getContent(), druidResult.getContent());
    }

    @Test public void testDruidMySqlEquivalence8() {
        QueryController controller = new QueryController();
        String mySqlQuery = q8.replace("__DB__", "\"foodmart-mysql\".\"foodmart\"");
        String druidQuery = q8.replace("__DB__", "\"foodmart\".\"foodmart\"");;
        QueryResult druidResult = controller.query(mySqlQuery);
        QueryResult mySqlResult = controller.query(druidQuery);
        assertEquals(mySqlResult.getContent(), druidResult.getContent());
    }

    @Test public void testDruidMySqlEquivalence9() {
        QueryController controller = new QueryController();
        String mySqlQuery = q9.replace("__DB__", "\"foodmart-mysql\".\"foodmart\"");
        String druidQuery = q9.replace("__DB__", "\"foodmart\".\"foodmart\"");;
        QueryResult druidResult = controller.query(mySqlQuery);
        QueryResult mySqlResult = controller.query(druidQuery);
        assertEquals(mySqlResult.getContent(), druidResult.getContent());
    }

    @Test public void testDruidMySqlEquivalence10() {
        QueryController controller = new QueryController();
        String mySqlQuery = q10.replace("__DB__", "\"foodmart-mysql\".\"foodmart\"");
        String druidQuery = q10.replace("__DB__", "\"foodmart\".\"foodmart\"");;
        QueryResult druidResult = controller.query(mySqlQuery);
        QueryResult mySqlResult = controller.query(druidQuery);
        assertEquals(mySqlResult.getContent(), druidResult.getContent());
    }
}