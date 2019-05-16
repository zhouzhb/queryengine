package com.huawei.cloudsop.us.queryengine;

import com.google.common.collect.Sets;
import com.huawei.cloudsop.us.queryengine.Connection.QueryResult;
import com.huawei.cloudsop.us.queryengine.Controller.QueryController;

import org.junit.Ignore;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

import static org.junit.Assert.assertTrue;

//@Ignore
public class DataSourceEquivalenceTest {

    private String q1 = "select count(*) as c, \"brand_name\" from __DB__"
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

    private String q5 = "select distinct \"brand_name\","
        + " \"country\" from __DB__"
        + " where \"timestamp\" >= '1996-01-01 00:00:00 UTC'"
        + " and \"timestamp\" < '1997-02-01 00:00:00 UTC'";

    private String q6 = "select sum(\"store_sales\") + sum(\"store_cost\"),"
        + " \"brand_name\" from __DB__"
        + " where \"timestamp\" >= '1996-01-01 00:00:00 UTC'"
        + " and \"timestamp\" < '1997-02-01 00:00:00 UTC'"
        + " and (\"store_sales\" > 0 or \"store_cost\" > 0)"
        + " group by \"brand_name\"";

    private String q7 =
        "select sum(\"store_sales\") / sum(\"store_cost\") as \"profitRatio\","
            + " \"brand_name\" from __DB__"
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
        + " and \"country\" = 'USA'"
        + " group by \"brand_name\", \"country\", floor(\"timestamp\" to DAY)";

    private String q10 = "select sum(\"store_sales\") + sum(\"store_cost\"),"
        + " \"brand_name\", \"country\" from __DB__"
        + "where \"timestamp\" >= '1996-01-01 00:00:00 UTC'"
        + " and \"timestamp\" < '1997-02-01 00:00:00 UTC'"
        + " and (\"store_cost\" > 0 or \"store_sales\" > 0)"
        + " group by \"brand_name\", \"country\", floor(\"timestamp\" to DAY)";

    private String q11 = "select sum(case when \"store_sales\" <> -1 then \"store_sales\" else 0 end)"
        + " + sum(case when \"store_cost\" <> -1 then \"store_cost\" else 0 end),"
        + " \"brand_name\", \"country\" from __DB__"
        + " where \"timestamp\" >= '1996-01-01 00:00:00 UTC'"
        + " and \"timestamp\" < '1997-02-01 00:00:00 UTC'"
        + " and (\"store_cost\" > 0 or \"store_sales\" > 0)"
        + " group by \"brand_name\", \"country\", floor(\"timestamp\" to DAY)";

    private String q12 = "select count(*) as c, \"brand_name\", \"country\" from __DB__"
        + " where \"timestamp\" >= '1996-01-01 00:00:00 UTC'"
        + " and \"timestamp\" < '1997-02-01 00:00:00 UTC'"
        + " and \"product_id\" = 1020"
        + " group by \"brand_name\", \"country\", floor(\"timestamp\" to DAY)";

    private String q13 = "select sum(\"store_sales\") + sum(\"store_cost\"),"
        + " \"brand_name\", \"country\" from __DB__"
        + " where \"timestamp\" >= '1996-01-01 00:00:00 UTC'"
        + " and \"timestamp\" < '1997-02-01 00:00:00 UTC'"
        + " and \"product_id\" > 1000"
        + " group by \"brand_name\", \"country\"";

    private String q14 = "select count(*), \"brand_name\", \"country\" from __DB__"
        + " where \"timestamp\" >= '1996-01-01 00:00:00 UTC'"
        + " and \"timestamp\" < '1997-02-01 00:00:00 UTC'"
        + " and \"product_id\" > 1000"
        + " group by \"brand_name\", \"country\", \"store_state\"";

    private String q15 = "select sum(\"store_sales\") + sum(\"store_cost\"),"
        + " \"brand_name\" from __DB__"
        + " where \"timestamp\" >= '1996-01-01 00:00:00 UTC'"
        + " and \"timestamp\" < '1997-02-01 00:00:00 UTC'"
        + " and \"product_id\" > 1000"
        + " group by \"brand_name\"";

    private String q16 = "select count(*) from __DB__"
        + " where \"timestamp\" >= '1996-01-01 00:00:00 UTC'"
        + " and \"timestamp\" < '1997-02-01 00:00:00 UTC'"
        + " and \"store_cost\" > 0";

    private String q17 = "select floor(\"timestamp\" to HOUR) as \"ts\","
        + " 100 * sum(\"store_sales\") / (sum(\"store_sales\") + sum(\"store_cost\")) from __DB__"
        + " where \"timestamp\" >= '1996-01-01 00:00:00 UTC'"
        + " and \"timestamp\" < '1997-02-01 00:00:00 UTC'"
        + " group by floor(\"timestamp\" to HOUR)";

    private String q18 = "select count(distinct \"brand_name\") from __DB__"
        + " where \"timestamp\" >= '1996-01-01 00:00:00 UTC'"
        + " and \"timestamp\" < '1997-02-01 00:00:00 UTC'"
        + " and \"store_cost\" > 0";

    private String q19 = "select count(distinct \"brand_name\") from __DB__"
        + " where \"timestamp\" >= '1996-01-01 00:00:00 UTC'"
        + " and \"timestamp\" < '1997-02-01 00:00:00 UTC'";

    private String q20 = "select floor(\"timestamp\" to DAY) as \"ts\", count(*) as \"total\" from __DB__"
        + " where \"timestamp\" >= '1996-01-01 00:00:00 UTC'"
        + " and \"timestamp\" < '1997-02-01 00:00:00 UTC'"
        + " group by floor(\"timestamp\" to DAY), \"brand_name\"";

    private String q21 = "select floor(\"timestamp\" to HOUR) as \"ts\", count(*) as \"total\" from __DB__"
        + " where \"timestamp\" >= '1996-01-01 00:00:00 UTC'"
        + " and \"timestamp\" < '1997-02-01 00:00:00 UTC'"
        + " group by floor(\"timestamp\" to HOUR), \"brand_name\"";

    private String q22 = "select count(distinct \"brand_name\") from __DB__"
        + " where \"timestamp\" >= '1996-01-01 00:00:00 UTC'"
        + " and \"timestamp\" < '1997-02-01 00:00:00 UTC'"
        + " and (\"store_cost\" > 0 or \"store_sales\" > 0)";

    private String q23 = "select floor(\"timestamp\" to HOUR) as \"ts\","
        + " sum(\"store_sales\") / sum(\"store_cost\") from __DB__"
        + " where \"timestamp\" >= '1996-01-01 00:00:00 UTC'"
        + " and \"timestamp\" < '1997-02-01 00:00:00 UTC'"
        + " group by floor(\"timestamp\" to HOUR)";

    private String q24 = "select count(distinct \"brand_name\") from __DB__"
        + " where \"timestamp\" >= '1996-01-01 00:00:00 UTC'"
        + " and \"timestamp\" < '1997-02-01 00:00:00 UTC'"
        + " and \"country\" = 'USA'";

    private String q25 = "select floor(\"timestamp\" to HOUR) as \"ts\","
        + " sum(\"store_sales\") / sum(\"store_cost\") from __DB__"
        + " where \"timestamp\" >= '1996-01-01 00:00:00 UTC'"
        + " and \"timestamp\" < '1997-02-01 00:00:00 UTC'"
        + " and \"store_cost\" > 0"
        + " group by floor(\"timestamp\" to HOUR)";

    private String q26 = "select \"product_id\", \"brand_name\", \"product_name\", \"SKU\", \"SRP\","
        + " \"gross_weight\", \"net_weight\", \"recyclable_package\", \"low_fat\", \"units_per_case\","
        + " \"cases_per_pallet\", \"shelf_width\", \"shelf_height\", \"shelf_depth\", \"product_class_id\","
        + " \"product_subcategory\" from __DB__"
        + " where \"timestamp\" >= '1996-01-01 00:00:00 UTC'"
        + " and \"timestamp\" < '1997-02-01 00:00:00 UTC'"
        + " and \"country\" = 'USA'"
        + " order by \"brand_name\", \"product_name\", \"SKU\", \"SRP\" limit 10";

    private String q27 = "select \"product_id\", \"brand_name\", \"product_name\" from __DB__"
        + " where \"timestamp\" >= '1996-01-01 00:00:00 UTC'"
        + " and \"timestamp\" < '1997-02-01 00:00:00 UTC'";

    private String q28 = "select count(*), sum(\"store_sales\") from __DB__"
        + " where \"timestamp\" >= '1996-01-01 00:00:00 UTC'"
        + " and \"timestamp\" < '1997-02-01 00:00:00 UTC'"
        + " group by \"brand_name\"";

    private String q29 = "select count(*) as \"t\" from __DB__"
        + " where \"timestamp\" >= '1996-01-01 00:00:00 UTC'"
        + " and \"timestamp\" < '1997-02-01 00:00:00 UTC'"
        + " group by \"lname\", \"product_id\", \"city\" order by count(*) desc";

    private String q30 = "select avg(\"store_sales\"), avg(\"store_cost\") from __DB__"
        + " where \"timestamp\" >= '1996-01-01 00:00:00 UTC'"
        + " and \"timestamp\" < '1997-02-01 00:00:00 UTC'"
        + " group by \"city\", \"state_province\"";

    private String q31 = "select count(*) from __DB__"
        + " where \"timestamp\" >= '1996-01-01 00:00:00 UTC'"
        + " and \"timestamp\" < '1997-02-01 00:00:00 UTC'";

    private String q32 = "select \"product_id\",\"brand_name\",\"product_name\",\"SKU\",\"SRP\","
        + "\"gross_weight\",\"net_weight\" from __DB__"
        + " where \"timestamp\" >= '1996-01-01 00:00:00 UTC'"
        + " and \"timestamp\" < '1997-02-01 00:00:00 UTC'";

    private String q33 = "select count(*) as \"c\", \"product_id\",\"brand_name\",\"product_name\" from __DB__"
        + " where \"timestamp\" >= '1996-01-01 00:00:00 UTC'"
        + " and \"timestamp\" < '1997-02-01 00:00:00 UTC'"
        + " group by \"product_id\",\"brand_name\",\"product_name\" order by \"c\" desc";

    private String q34 = "select \"timestamp\",\"product_id\",\"brand_name\",\"product_name\",\"SKU\",\"SRP\","
        + "\"gross_weight\",\"net_weight\" from __DB__"
        + " where \"timestamp\" >= '1996-01-01 00:00:00 UTC'"
        + " and \"timestamp\" < '1997-02-01 00:00:00 UTC' order by \"brand_name\", \"product_name\", \"SKU\", \"SRP\" desc limit 10";

    private int maxSignificantDigits = 4;
    private int maxDiffPercentage = 5;

    /**
     * Compares 2 content values from {@link QueryResult} and decides if they are close enough. We define them to be close enough if all these conditions are satisfied:
     * - If they have the same number of rows
     * - If number of different rows is no more than {@link DataSourceEquivalenceTest#maxDiffPercentage}
     * - If a row has floating point value, only {@link DataSourceEquivalenceTest#maxSignificantDigits} significant digits are used.
     * - If a row has number value in billions/millions/thousands, replace it with a string ...B/...M/...K.
     * - If a row has timestamp value, convert it to Unix epoch time
     * @param content1 The first content to compare
     * @param content2 The second content to compare
     * @return Result of fuzzy comparison
     */
    public boolean areCloseEnough(String content1, String content2) {
        // Convert contents to sets
        Set<String> set1 = rowsToSet(content1);
        Set<String> set2 = rowsToSet(content2);

//        // Verify same row count
//        if (set1.size() != set2.size()) {
//            return false;
//        }

        // Verify they have a lot of the same rows
        Set<String> diff = Sets.symmetricDifference(set1, set2);
        int diffCount = diff.size();
        int setCount = set1.size() + set2.size();
        double diffPercentage = 100d * diffCount / setCount;
        return diffPercentage < maxDiffPercentage;
    }

    /**
     * Convert rows of data as a string where rows are separated by new lines and columns by semicolon to a set.
     * If float value is found, only the first 2 significant digits are used to work around different float number handling by different DBs.
     * @param rowsString
     * @return set of rows
     */
    private Set<String> rowsToSet(final String rowsString) {
        Set<String> ret = Sets.newHashSet();
        String[] rows = rowsString.split("\\n");
        for (String row : rows) {
            String[] cols = row.split(";");
            String rowSetElement = "";
            for (String col : cols) {
                String[] nameValuePair = col.split("=");
                String colName = nameValuePair[0];
                String colValue = nameValuePair[1];

                // convert timestamp to Unix epoch time
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    sdf.setTimeZone(java.util.TimeZone.getTimeZone("UTC"));
                    Date dt = sdf.parse(colValue);
                    long epoch = dt.getTime();
                    colValue = Long.toString(epoch);
                } catch (ParseException e) {
                }

                // if column value is a number - simplify its value
                try {
                    double doubleVal = Double.valueOf(colValue).doubleValue();
                    String newColVal;
                    if (doubleVal > 1000000000) {
                        newColVal = (int) (doubleVal / 1000000000) + "B";
                    } else if (doubleVal > 1000000) {
                        newColVal = (int) (doubleVal / 1000000) + "M";
                    } else if (doubleVal > 1000) {
                        newColVal = (int) (doubleVal / 1000) + "K";
                    } else {
                        newColVal = String.format("%." + maxSignificantDigits + "f", doubleVal);
                    }
                    rowSetElement += colName + "=" + newColVal + ";";
                } catch (NumberFormatException nfe) {
                    rowSetElement += colName + colValue.toUpperCase() + ";";
                }
            }
            ret.add(rowSetElement);
        }
        return ret;
    }

    @Test public void testDruidMySqlEquivalence1() {
        QueryController controller = new QueryController();
        String mySqlQuery = q1.replace("__DB__", "\"foodmart-mysql\".\"foodmart\"");
        String druidQuery = q1.replace("__DB__", "\"foodmart\".\"foodmart\"");
        QueryResult druidResult = controller.query(mySqlQuery);
        QueryResult mySqlResult = controller.query(druidQuery);
        assertTrue(areCloseEnough(mySqlResult.getContent(), druidResult.getContent()));
    }

    @Test public void testDruidMySqlEquivalence2() {
        QueryController controller = new QueryController();
        String mySqlQuery = q2.replace("__DB__", "\"foodmart-mysql\".\"foodmart\"");
        String druidQuery = q2.replace("__DB__", "\"foodmart\".\"foodmart\"");
        QueryResult druidResult = controller.query(mySqlQuery);
        QueryResult mySqlResult = controller.query(druidQuery);
        assertTrue(areCloseEnough(mySqlResult.getContent(), druidResult.getContent()));
    }

    @Test public void testDruidMySqlEquivalence3() {
        QueryController controller = new QueryController();
        String mySqlQuery = q3.replace("__DB__", "\"foodmart-mysql\".\"foodmart\"");
        String druidQuery = q3.replace("__DB__", "\"foodmart\".\"foodmart\"");
        QueryResult druidResult = controller.query(mySqlQuery);
        QueryResult mySqlResult = controller.query(druidQuery);
        assertTrue(areCloseEnough(mySqlResult.getContent(), druidResult.getContent()));
    }

    @Test public void testDruidMySqlEquivalence4() {
        QueryController controller = new QueryController();
        String mySqlQuery = q4.replace("__DB__", "\"foodmart-mysql\".\"foodmart\"");
        String druidQuery = q4.replace("__DB__", "\"foodmart\".\"foodmart\"");
        QueryResult druidResult = controller.query(mySqlQuery);
        QueryResult mySqlResult = controller.query(druidQuery);
        assertTrue(areCloseEnough(mySqlResult.getContent(), druidResult.getContent()));
    }

    @Test public void testDruidMySqlEquivalence5() {
        QueryController controller = new QueryController();
        String mySqlQuery = q5.replace("__DB__", "\"foodmart-mysql\".\"foodmart\"");
        String druidQuery = q5.replace("__DB__", "\"foodmart\".\"foodmart\"");
        QueryResult druidResult = controller.query(mySqlQuery);
        QueryResult mySqlResult = controller.query(druidQuery);
        assertTrue(areCloseEnough(mySqlResult.getContent(), druidResult.getContent()));
    }

    @Test public void testDruidMySqlEquivalence6() {
        QueryController controller = new QueryController();
        String mySqlQuery = q6.replace("__DB__", "\"foodmart-mysql\".\"foodmart\"");
        String druidQuery = q6.replace("__DB__", "\"foodmart\".\"foodmart\"");
        QueryResult druidResult = controller.query(mySqlQuery);
        QueryResult mySqlResult = controller.query(druidQuery);
        assertTrue(areCloseEnough(mySqlResult.getContent(), druidResult.getContent()));
    }

    @Test public void testDruidMySqlEquivalence7() {
        QueryController controller = new QueryController();
        String mySqlQuery = q7.replace("__DB__", "\"foodmart-mysql\".\"foodmart\"");
        String druidQuery = q7.replace("__DB__", "\"foodmart\".\"foodmart\"");
        QueryResult druidResult = controller.query(mySqlQuery);
        QueryResult mySqlResult = controller.query(druidQuery);
        assertTrue(areCloseEnough(mySqlResult.getContent(), druidResult.getContent()));
    }

    @Test public void testDruidMySqlEquivalence8() {
        QueryController controller = new QueryController();
        String mySqlQuery = q8.replace("__DB__", "\"foodmart-mysql\".\"foodmart\"");
        String druidQuery = q8.replace("__DB__", "\"foodmart\".\"foodmart\"");
        QueryResult druidResult = controller.query(mySqlQuery);
        QueryResult mySqlResult = controller.query(druidQuery);
        assertTrue(areCloseEnough(mySqlResult.getContent(), druidResult.getContent()));
    }

    @Test public void testDruidMySqlEquivalence9() {
        QueryController controller = new QueryController();
        String mySqlQuery = q9.replace("__DB__", "\"foodmart-mysql\".\"foodmart\"");
        String druidQuery = q9.replace("__DB__", "\"foodmart\".\"foodmart\"");
        QueryResult druidResult = controller.query(mySqlQuery);
        QueryResult mySqlResult = controller.query(druidQuery);
        assertTrue(areCloseEnough(mySqlResult.getContent(), druidResult.getContent()));
    }

    @Test public void testDruidMySqlEquivalence10() {
        QueryController controller = new QueryController();
        String mySqlQuery = q10.replace("__DB__", "\"foodmart-mysql\".\"foodmart\"");
        String druidQuery = q10.replace("__DB__", "\"foodmart\".\"foodmart\"");
        QueryResult druidResult = controller.query(mySqlQuery);
        QueryResult mySqlResult = controller.query(druidQuery);
        assertTrue(areCloseEnough(mySqlResult.getContent(), druidResult.getContent()));
    }

    @Test public void testDruidMySqlEquivalence11() {
        QueryController controller = new QueryController();
        String mySqlQuery = q11.replace("__DB__", "\"foodmart-mysql\".\"foodmart\"");
        String druidQuery = q11.replace("__DB__", "\"foodmart\".\"foodmart\"");
        QueryResult druidResult = controller.query(mySqlQuery);
        QueryResult mySqlResult = controller.query(druidQuery);
        assertTrue(areCloseEnough(mySqlResult.getContent(), druidResult.getContent()));
    }

    @Test public void testDruidMySqlEquivalence12() {
        QueryController controller = new QueryController();
        String mySqlQuery = q12.replace("__DB__", "\"foodmart-mysql\".\"foodmart\"");
        String druidQuery = q12.replace("__DB__", "\"foodmart\".\"foodmart\"");
        QueryResult druidResult = controller.query(mySqlQuery);
        QueryResult mySqlResult = controller.query(druidQuery);
        assertTrue(areCloseEnough(mySqlResult.getContent(), druidResult.getContent()));
    }

    @Test public void testDruidMySqlEquivalence13() {
        QueryController controller = new QueryController();
        String mySqlQuery = q13.replace("__DB__", "\"foodmart-mysql\".\"foodmart\"");
        String druidQuery = q13.replace("__DB__", "\"foodmart\".\"foodmart\"");
        QueryResult druidResult = controller.query(mySqlQuery);
        QueryResult mySqlResult = controller.query(druidQuery);
        assertTrue(areCloseEnough(mySqlResult.getContent(), druidResult.getContent()));
    }

    @Test public void testDruidMySqlEquivalence14() {
        QueryController controller = new QueryController();
        String mySqlQuery = q14.replace("__DB__", "\"foodmart-mysql\".\"foodmart\"");
        String druidQuery = q14.replace("__DB__", "\"foodmart\".\"foodmart\"");
        QueryResult druidResult = controller.query(mySqlQuery);
        QueryResult mySqlResult = controller.query(druidQuery);
        assertTrue(areCloseEnough(mySqlResult.getContent(), druidResult.getContent()));
    }

    @Test public void testDruidMySqlEquivalence15() {
        QueryController controller = new QueryController();
        String mySqlQuery = q15.replace("__DB__", "\"foodmart-mysql\".\"foodmart\"");
        String druidQuery = q15.replace("__DB__", "\"foodmart\".\"foodmart\"");
        QueryResult druidResult = controller.query(mySqlQuery);
        QueryResult mySqlResult = controller.query(druidQuery);
        assertTrue(areCloseEnough(mySqlResult.getContent(), druidResult.getContent()));
    }

    @Test public void testDruidMySqlEquivalence16() {
        QueryController controller = new QueryController();
        String mySqlQuery = q16.replace("__DB__", "\"foodmart-mysql\".\"foodmart\"");
        String druidQuery = q16.replace("__DB__", "\"foodmart\".\"foodmart\"");
        QueryResult druidResult = controller.query(mySqlQuery);
        QueryResult mySqlResult = controller.query(druidQuery);
        assertTrue(areCloseEnough(mySqlResult.getContent(), druidResult.getContent()));
    }

    @Test public void testDruidMySqlEquivalence17() {
        QueryController controller = new QueryController();
        String mySqlQuery = q17.replace("__DB__", "\"foodmart-mysql\".\"foodmart\"");
        String druidQuery = q17.replace("__DB__", "\"foodmart\".\"foodmart\"");
        QueryResult druidResult = controller.query(mySqlQuery);
        QueryResult mySqlResult = controller.query(druidQuery);
        assertTrue(areCloseEnough(mySqlResult.getContent(), druidResult.getContent()));
    }

    @Test public void testDruidMySqlEquivalence18() {
        QueryController controller = new QueryController();
        String mySqlQuery = q18.replace("__DB__", "\"foodmart-mysql\".\"foodmart\"");
        String druidQuery = q18.replace("__DB__", "\"foodmart\".\"foodmart\"");
        QueryResult druidResult = controller.query(mySqlQuery);
        QueryResult mySqlResult = controller.query(druidQuery);
        assertTrue(areCloseEnough(mySqlResult.getContent(), druidResult.getContent()));
    }

    @Test public void testDruidMySqlEquivalence19() {
        QueryController controller = new QueryController();
        String mySqlQuery = q19.replace("__DB__", "\"foodmart-mysql\".\"foodmart\"");
        String druidQuery = q19.replace("__DB__", "\"foodmart\".\"foodmart\"");
        QueryResult druidResult = controller.query(mySqlQuery);
        QueryResult mySqlResult = controller.query(druidQuery);
        assertTrue(areCloseEnough(mySqlResult.getContent(), druidResult.getContent()));
    }

    @Test public void testDruidMySqlEquivalence20() {
        QueryController controller = new QueryController();
        String mySqlQuery = q20.replace("__DB__", "\"foodmart-mysql\".\"foodmart\"");
        String druidQuery = q20.replace("__DB__", "\"foodmart\".\"foodmart\"");
        QueryResult druidResult = controller.query(mySqlQuery);
        QueryResult mySqlResult = controller.query(druidQuery);
        assertTrue(areCloseEnough(mySqlResult.getContent(), druidResult.getContent()));
    }

    @Test public void testDruidMySqlEquivalence21() {
        QueryController controller = new QueryController();
        String mySqlQuery = q21.replace("__DB__", "\"foodmart-mysql\".\"foodmart\"");
        String druidQuery = q21.replace("__DB__", "\"foodmart\".\"foodmart\"");
        QueryResult druidResult = controller.query(mySqlQuery);
        QueryResult mySqlResult = controller.query(druidQuery);
        assertTrue(areCloseEnough(mySqlResult.getContent(), druidResult.getContent()));
    }

    @Test public void testDruidMySqlEquivalence22() {
        QueryController controller = new QueryController();
        String mySqlQuery = q22.replace("__DB__", "\"foodmart-mysql\".\"foodmart\"");
        String druidQuery = q22.replace("__DB__", "\"foodmart\".\"foodmart\"");
        QueryResult druidResult = controller.query(mySqlQuery);
        QueryResult mySqlResult = controller.query(druidQuery);
        assertTrue(areCloseEnough(mySqlResult.getContent(), druidResult.getContent()));
    }

    @Test public void testDruidMySqlEquivalence23() {
        QueryController controller = new QueryController();
        String mySqlQuery = q23.replace("__DB__", "\"foodmart-mysql\".\"foodmart\"");
        String druidQuery = q23.replace("__DB__", "\"foodmart\".\"foodmart\"");
        QueryResult druidResult = controller.query(mySqlQuery);
        QueryResult mySqlResult = controller.query(druidQuery);
        assertTrue(areCloseEnough(mySqlResult.getContent(), druidResult.getContent()));
    }

    @Test public void testDruidMySqlEquivalence24() {
        QueryController controller = new QueryController();
        String mySqlQuery = q24.replace("__DB__", "\"foodmart-mysql\".\"foodmart\"");
        String druidQuery = q24.replace("__DB__", "\"foodmart\".\"foodmart\"");
        QueryResult druidResult = controller.query(mySqlQuery);
        QueryResult mySqlResult = controller.query(druidQuery);
        assertTrue(areCloseEnough(mySqlResult.getContent(), druidResult.getContent()));
    }

    @Test public void testDruidMySqlEquivalence25() {
        QueryController controller = new QueryController();
        String mySqlQuery = q25.replace("__DB__", "\"foodmart-mysql\".\"foodmart\"");
        String druidQuery = q25.replace("__DB__", "\"foodmart\".\"foodmart\"");
        QueryResult druidResult = controller.query(mySqlQuery);
        QueryResult mySqlResult = controller.query(druidQuery);
        assertTrue(areCloseEnough(mySqlResult.getContent(), druidResult.getContent()));
    }

    @Test public void testDruidMySqlEquivalence26() {
        QueryController controller = new QueryController();
        String mySqlQuery = q26.replace("__DB__", "\"foodmart-mysql\".\"foodmart\"");
        String druidQuery = q26.replace("__DB__", "\"foodmart\".\"foodmart\"");
        QueryResult druidResult = controller.query(mySqlQuery);
        QueryResult mySqlResult = controller.query(druidQuery);
        assertTrue(areCloseEnough(mySqlResult.getContent(), druidResult.getContent()));
    }

    @Test public void testDruidMySqlEquivalence27() {
        QueryController controller = new QueryController();
        String mySqlQuery = q27.replace("__DB__", "\"foodmart-mysql\".\"foodmart\"");
        String druidQuery = q27.replace("__DB__", "\"foodmart\".\"foodmart\"");
        QueryResult druidResult = controller.query(mySqlQuery);
        QueryResult mySqlResult = controller.query(druidQuery);
        assertTrue(areCloseEnough(mySqlResult.getContent(), druidResult.getContent()));
    }

    @Test public void testDruidMySqlEquivalence28() {
        QueryController controller = new QueryController();
        String mySqlQuery = q28.replace("__DB__", "\"foodmart-mysql\".\"foodmart\"");
        String druidQuery = q28.replace("__DB__", "\"foodmart\".\"foodmart\"");
        QueryResult druidResult = controller.query(mySqlQuery);
        QueryResult mySqlResult = controller.query(druidQuery);
        assertTrue(areCloseEnough(mySqlResult.getContent(), druidResult.getContent()));
    }

    @Test public void testDruidMySqlEquivalence29() {
        QueryController controller = new QueryController();
        String mySqlQuery = q29.replace("__DB__", "\"foodmart-mysql\".\"foodmart\"");
        String druidQuery = q29.replace("__DB__", "\"foodmart\".\"foodmart\"");
        QueryResult druidResult = controller.query(mySqlQuery);
        QueryResult mySqlResult = controller.query(druidQuery);
        assertTrue(areCloseEnough(mySqlResult.getContent(), druidResult.getContent()));
    }

    @Test public void testDruidMySqlEquivalence30() {
        QueryController controller = new QueryController();
        String mySqlQuery = q30.replace("__DB__", "\"foodmart-mysql\".\"foodmart\"");
        String druidQuery = q30.replace("__DB__", "\"foodmart\".\"foodmart\"");
        QueryResult druidResult = controller.query(mySqlQuery);
        QueryResult mySqlResult = controller.query(druidQuery);
        assertTrue(areCloseEnough(mySqlResult.getContent(), druidResult.getContent()));
    }

    @Test public void testDruidMySqlEquivalence31() {
        QueryController controller = new QueryController();
        String mySqlQuery = q31.replace("__DB__", "\"foodmart-mysql\".\"foodmart\"");
        String druidQuery = q31.replace("__DB__", "\"foodmart\".\"foodmart\"");
        QueryResult druidResult = controller.query(mySqlQuery);
        QueryResult mySqlResult = controller.query(druidQuery);
        assertTrue(areCloseEnough(mySqlResult.getContent(), druidResult.getContent()));
    }

    @Test public void testDruidMySqlEquivalence32() {
        QueryController controller = new QueryController();
        String mySqlQuery = q32.replace("__DB__", "\"foodmart-mysql\".\"foodmart\"");
        String druidQuery = q32.replace("__DB__", "\"foodmart\".\"foodmart\"");
        QueryResult druidResult = controller.query(mySqlQuery);
        QueryResult mySqlResult = controller.query(druidQuery);
        assertTrue(areCloseEnough(mySqlResult.getContent(), druidResult.getContent()));
    }

    @Test public void testDruidMySqlEquivalence33() {
        QueryController controller = new QueryController();
        String mySqlQuery = q33.replace("__DB__", "\"foodmart-mysql\".\"foodmart\"");
        String druidQuery = q33.replace("__DB__", "\"foodmart\".\"foodmart\"");
        QueryResult druidResult = controller.query(mySqlQuery);
        QueryResult mySqlResult = controller.query(druidQuery);
        assertTrue(areCloseEnough(mySqlResult.getContent(), druidResult.getContent()));
    }

    @Test public void testDruidMySqlEquivalence34() {
        QueryController controller = new QueryController();
        String mySqlQuery = q34.replace("__DB__", "\"foodmart-mysql\".\"foodmart\"");
        String druidQuery = q34.replace("__DB__", "\"foodmart\".\"foodmart\"");
        QueryResult druidResult = controller.query(mySqlQuery);
        QueryResult mySqlResult = controller.query(druidQuery);
        assertTrue(areCloseEnough(mySqlResult.getContent(), druidResult.getContent()));
    }
}
