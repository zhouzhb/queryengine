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

@Ignore
public class DataSourceEquivalenceTest {

    private final String Q1 = "select count(*) as c, \"brand_name\" from __DB__"
        + " where \"timestamp\" >= '1996-01-01 00:00:00 UTC'"
        + " and \"timestamp\" < '1997-02-01 00:00:00 UTC'"
        + " and \"product_id\" = 1020"
        + " group by \"brand_name\"";

    private final String Q2 = "select sum(\"store_sales\") / sum(\"store_cost\") as \"profitRatio\","
        + " \"brand_name\" from __DB__"
        + " where \"timestamp\" >= '1996-01-01 00:00:00 UTC'"
        + " and \"timestamp\" < '1997-02-01 00:00:00 UTC'"
        + " and \"store_cost\" > 0"
        + " group by \"brand_name\"";

    private final String Q3 = "select sum(\"store_sales\") + sum(\"store_cost\"),"
        + " \"brand_name\" from __DB__"
        + " where \"timestamp\" >= '1996-01-01 00:00:00 UTC'"
        + " and \"timestamp\" < '1997-02-01 00:00:00 UTC'"
        + " group by \"brand_name\"";

    private final String Q4 = "select 100 * sum(\"store_sales\") / (sum(\"store_sales\")"
        + " + sum(\"store_cost\")), \"brand_name\" from __DB__"
        + " where \"timestamp\" >= '1996-01-01 00:00:00 UTC'"
        + " and \"timestamp\" < '1997-02-01 00:00:00 UTC'"
        + " group by \"brand_name\"";

    private final String Q5 = "select distinct \"brand_name\","
        + " \"country\" from __DB__"
        + " where \"timestamp\" >= '1996-01-01 00:00:00 UTC'"
        + " and \"timestamp\" < '1997-02-01 00:00:00 UTC'";

    private final String Q6 = "select sum(\"store_sales\") + sum(\"store_cost\"),"
        + " \"brand_name\" from __DB__"
        + " where \"timestamp\" >= '1996-01-01 00:00:00 UTC'"
        + " and \"timestamp\" < '1997-02-01 00:00:00 UTC'"
        + " and (\"store_sales\" > 0 or \"store_cost\" > 0)"
        + " group by \"brand_name\"";

    private final String Q7 = "select sum(\"store_sales\") / sum(\"store_cost\") as \"profitRatio\","
        + " \"brand_name\" from __DB__"
        + " where \"timestamp\" >= '1996-01-01 00:00:00 UTC'"
        + " and \"timestamp\" < '1997-02-01 00:00:00 UTC'"
        + " group by \"brand_name\"";

    private final String Q8 = "select count(*) as c, \"brand_name\" from __DB__"
        + " where \"timestamp\" >= '1996-01-01 00:00:00 UTC'"
        + " and \"timestamp\" < '1997-02-01 00:00:00 UTC'"
        + " group by \"brand_name\"";

    private final String Q9 = "select count(*) as c, \"brand_name\", \"country\" from __DB__"
        + " where \"timestamp\" >= '1996-01-01 00:00:00 UTC'"
        + " and \"timestamp\" < '1997-02-01 00:00:00 UTC'"
        + " and \"country\" = 'USA'"
        + " group by \"brand_name\", \"country\", floor(\"timestamp\" to DAY)";

    private final String Q10 = "select sum(\"store_sales\") + sum(\"store_cost\"),"
        + " \"brand_name\", \"country\" from __DB__"
        + "where \"timestamp\" >= '1996-01-01 00:00:00 UTC'"
        + " and \"timestamp\" < '1997-02-01 00:00:00 UTC'"
        + " and (\"store_cost\" > 0 or \"store_sales\" > 0)"
        + " group by \"brand_name\", \"country\", floor(\"timestamp\" to DAY)";

    private final String Q11 = "select sum(case when \"store_sales\" <> -1 then \"store_sales\" else 0 end)"
        + " + sum(case when \"store_cost\" <> -1 then \"store_cost\" else 0 end),"
        + " \"brand_name\", \"country\" from __DB__"
        + " where \"timestamp\" >= '1996-01-01 00:00:00 UTC'"
        + " and \"timestamp\" < '1997-02-01 00:00:00 UTC'"
        + " and (\"store_cost\" > 0 or \"store_sales\" > 0)"
        + " group by \"brand_name\", \"country\", floor(\"timestamp\" to DAY)";

    private final String Q12 = "select count(*) as c, \"brand_name\", \"country\" from __DB__"
        + " where \"timestamp\" >= '1996-01-01 00:00:00 UTC'"
        + " and \"timestamp\" < '1997-02-01 00:00:00 UTC'"
        + " and \"product_id\" = 1020"
        + " group by \"brand_name\", \"country\", floor(\"timestamp\" to DAY)";

    private final String Q13 = "select sum(\"store_sales\") + sum(\"store_cost\"),"
        + " \"brand_name\", \"country\" from __DB__"
        + " where \"timestamp\" >= '1996-01-01 00:00:00 UTC'"
        + " and \"timestamp\" < '1997-02-01 00:00:00 UTC'"
        + " and \"product_id\" > 1000"
        + " group by \"brand_name\", \"country\"";

    private final String Q14 = "select count(*), \"brand_name\", \"country\" from __DB__"
        + " where \"timestamp\" >= '1996-01-01 00:00:00 UTC'"
        + " and \"timestamp\" < '1997-02-01 00:00:00 UTC'"
        + " and \"product_id\" > 1000"
        + " group by \"brand_name\", \"country\", \"store_state\"";

    private final String Q15 = "select sum(\"store_sales\") + sum(\"store_cost\"),"
        + " \"brand_name\" from __DB__"
        + " where \"timestamp\" >= '1996-01-01 00:00:00 UTC'"
        + " and \"timestamp\" < '1997-02-01 00:00:00 UTC'"
        + " and \"product_id\" > 1000"
        + " group by \"brand_name\"";

    private final String Q16 = "select count(*) from __DB__"
        + " where \"timestamp\" >= '1996-01-01 00:00:00 UTC'"
        + " and \"timestamp\" < '1997-02-01 00:00:00 UTC'"
        + " and \"store_cost\" > 0";

    private final String Q17 = "select floor(\"timestamp\" to HOUR) as \"ts\","
        + " 100 * sum(\"store_sales\") / (sum(\"store_sales\") + sum(\"store_cost\")) from __DB__"
        + " where \"timestamp\" >= '1996-01-01 00:00:00 UTC'"
        + " and \"timestamp\" < '1997-02-01 00:00:00 UTC'"
        + " group by floor(\"timestamp\" to HOUR)";

    private final String Q18 = "select count(distinct \"brand_name\") from __DB__"
        + " where \"timestamp\" >= '1996-01-01 00:00:00 UTC'"
        + " and \"timestamp\" < '1997-02-01 00:00:00 UTC'"
        + " and \"store_cost\" > 0";

    private final String Q19 = "select count(distinct \"brand_name\") from __DB__"
        + " where \"timestamp\" >= '1996-01-01 00:00:00 UTC'"
        + " and \"timestamp\" < '1997-02-01 00:00:00 UTC'";

    private final String Q20 = "select floor(\"timestamp\" to DAY) as \"ts\", count(*) as \"total\" from __DB__"
        + " where \"timestamp\" >= '1996-01-01 00:00:00 UTC'"
        + " and \"timestamp\" < '1997-02-01 00:00:00 UTC'"
        + " group by floor(\"timestamp\" to DAY), \"brand_name\"";

    private final String Q21 = "select floor(\"timestamp\" to HOUR) as \"ts\", count(*) as \"total\" from __DB__"
        + " where \"timestamp\" >= '1996-01-01 00:00:00 UTC'"
        + " and \"timestamp\" < '1997-02-01 00:00:00 UTC'"
        + " group by floor(\"timestamp\" to HOUR), \"brand_name\"";

    private final String Q22 = "select count(distinct \"brand_name\") from __DB__"
        + " where \"timestamp\" >= '1996-01-01 00:00:00 UTC'"
        + " and \"timestamp\" < '1997-02-01 00:00:00 UTC'"
        + " and (\"store_cost\" > 0 or \"store_sales\" > 0)";

    private final String Q23 = "select floor(\"timestamp\" to HOUR) as \"ts\","
        + " sum(\"store_sales\") / sum(\"store_cost\") from __DB__"
        + " where \"timestamp\" >= '1996-01-01 00:00:00 UTC'"
        + " and \"timestamp\" < '1997-02-01 00:00:00 UTC'"
        + " group by floor(\"timestamp\" to HOUR)";

    private final String Q24 = "select count(distinct \"brand_name\") from __DB__"
        + " where \"timestamp\" >= '1996-01-01 00:00:00 UTC'"
        + " and \"timestamp\" < '1997-02-01 00:00:00 UTC'"
        + " and \"country\" = 'USA'";

    private final String Q25 = "select floor(\"timestamp\" to HOUR) as \"ts\","
        + " sum(\"store_sales\") / sum(\"store_cost\") from __DB__"
        + " where \"timestamp\" >= '1996-01-01 00:00:00 UTC'"
        + " and \"timestamp\" < '1997-02-01 00:00:00 UTC'"
        + " and \"store_cost\" > 0"
        + " group by floor(\"timestamp\" to HOUR)";

    private final String Q26 = "select \"product_id\", \"brand_name\", \"product_name\", \"SKU\", \"SRP\","
        + " \"gross_weight\", \"net_weight\", \"recyclable_package\", \"low_fat\", \"units_per_case\","
        + " \"cases_per_pallet\", \"shelf_width\", \"shelf_height\", \"shelf_depth\", \"product_class_id\","
        + " \"product_subcategory\" from __DB__"
        + " where \"timestamp\" >= '1996-01-01 00:00:00 UTC'"
        + " and \"timestamp\" < '1997-02-01 00:00:00 UTC'"
        + " and \"country\" = 'USA'"
        + " order by \"brand_name\", \"product_name\", \"SKU\", \"SRP\" limit 10";

    private final String Q27 = "select \"product_id\", \"brand_name\", \"product_name\" from __DB__"
        + " where \"timestamp\" >= '1996-01-01 00:00:00 UTC'"
        + " and \"timestamp\" < '1997-02-01 00:00:00 UTC'";

    private final String Q28 = "select count(*), sum(\"store_sales\") from __DB__"
        + " where \"timestamp\" >= '1996-01-01 00:00:00 UTC'"
        + " and \"timestamp\" < '1997-02-01 00:00:00 UTC'"
        + " group by \"brand_name\"";

    private final String Q29 = "select count(*) as \"t\" from __DB__"
        + " where \"timestamp\" >= '1996-01-01 00:00:00 UTC'"
        + " and \"timestamp\" < '1997-02-01 00:00:00 UTC'"
        + " group by \"lname\", \"product_id\", \"city\" order by count(*) desc";

    private final String Q30 = "select avg(\"store_sales\"), avg(\"store_cost\") from __DB__"
        + " where \"timestamp\" >= '1996-01-01 00:00:00 UTC'"
        + " and \"timestamp\" < '1997-02-01 00:00:00 UTC'"
        + " group by \"city\", \"state_province\"";

    private final String Q31 = "select count(*) from __DB__"
        + " where \"timestamp\" >= '1996-01-01 00:00:00 UTC'"
        + " and \"timestamp\" < '1997-02-01 00:00:00 UTC'";

    private final String Q32 = "select \"product_id\",\"brand_name\",\"product_name\",\"SKU\",\"SRP\","
        + "\"gross_weight\",\"net_weight\" from __DB__"
        + " where \"timestamp\" >= '1996-01-01 00:00:00 UTC'"
        + " and \"timestamp\" < '1997-02-01 00:00:00 UTC'";

    private final String Q33 = "select count(*) as \"c\", \"product_id\",\"brand_name\",\"product_name\" from __DB__"
        + " where \"timestamp\" >= '1996-01-01 00:00:00 UTC'"
        + " and \"timestamp\" < '1997-02-01 00:00:00 UTC'"
        + " group by \"product_id\",\"brand_name\",\"product_name\" order by \"c\" desc";

    private final String Q34 = "select \"timestamp\",\"product_id\",\"brand_name\",\"product_name\",\"SKU\",\"SRP\","
        + "\"gross_weight\",\"net_weight\" from __DB__"
        + " where \"timestamp\" >= '1996-01-01 00:00:00 UTC'"
        + " and \"timestamp\" < '1997-02-01 00:00:00 UTC' order by \"brand_name\", \"product_name\", \"SKU\", \"SRP\" desc limit 10";

    private final int MAX_SIGNIFICANT_DIGITS = 4;
    private final int MAX_DIFF_PERCENTAGE = 5;

    /**
     * Compares 2 content values from {@link QueryResult} and decides if they are close enough. We define them to be close enough if all these conditions are satisfied:
     * - If number of different rows is no more than {@link DataSourceEquivalenceTest#MAX_DIFF_PERCENTAGE}.
     * - If a row has floating point value, only {@link DataSourceEquivalenceTest#MAX_SIGNIFICANT_DIGITS} significant digits are used.
     * - If a row has number value in billions/millions/thousands, replace it with a string ...B/...M/...K.
     * - If a row has timestamp value, convert it to Unix epoch time.
     * @param content1 The first content to compare
     * @param content2 The second content to compare
     * @return True if contents are close enough, false otherwise
     */
    public boolean areCloseEnough(String content1, String content2) {
        // Convert contents to sets
        Set<String> set1 = rowsToSet(content1);
        Set<String> set2 = rowsToSet(content2);

        // Verify they have a lot of the same rows
        Set<String> diff = Sets.symmetricDifference(set1, set2);
        int diffCount = diff.size();
        int setCount = set1.size() + set2.size();
        double diffPercentage = 100d * diffCount / setCount;
        return diffPercentage < MAX_DIFF_PERCENTAGE;
    }

    /**
     * Convert rows of data as a string where rows are separated by new lines and columns by semicolon to a set.
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
                        newColVal = String.format("%." + MAX_SIGNIFICANT_DIGITS + "f", doubleVal);
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
        String mySqlQuery = Q1.replace("__DB__", "\"foodmart-mysql\".\"foodmart\"");
        String druidQuery = Q1.replace("__DB__", "\"foodmart\".\"foodmart\"");
        QueryResult druidResult = controller.query(mySqlQuery);
        QueryResult mySqlResult = controller.query(druidQuery);
        assertTrue(areCloseEnough(mySqlResult.getContent(), druidResult.getContent()));
    }

    @Test public void testDruidMySqlEquivalence2() {
        QueryController controller = new QueryController();
        String mySqlQuery = Q2.replace("__DB__", "\"foodmart-mysql\".\"foodmart\"");
        String druidQuery = Q2.replace("__DB__", "\"foodmart\".\"foodmart\"");
        QueryResult druidResult = controller.query(mySqlQuery);
        QueryResult mySqlResult = controller.query(druidQuery);
        assertTrue(areCloseEnough(mySqlResult.getContent(), druidResult.getContent()));
    }

    @Test public void testDruidMySqlEquivalence3() {
        QueryController controller = new QueryController();
        String mySqlQuery = Q3.replace("__DB__", "\"foodmart-mysql\".\"foodmart\"");
        String druidQuery = Q3.replace("__DB__", "\"foodmart\".\"foodmart\"");
        QueryResult druidResult = controller.query(mySqlQuery);
        QueryResult mySqlResult = controller.query(druidQuery);
        assertTrue(areCloseEnough(mySqlResult.getContent(), druidResult.getContent()));
    }

    @Test public void testDruidMySqlEquivalence4() {
        QueryController controller = new QueryController();
        String mySqlQuery = Q4.replace("__DB__", "\"foodmart-mysql\".\"foodmart\"");
        String druidQuery = Q4.replace("__DB__", "\"foodmart\".\"foodmart\"");
        QueryResult druidResult = controller.query(mySqlQuery);
        QueryResult mySqlResult = controller.query(druidQuery);
        assertTrue(areCloseEnough(mySqlResult.getContent(), druidResult.getContent()));
    }

    @Test public void testDruidMySqlEquivalence5() {
        QueryController controller = new QueryController();
        String mySqlQuery = Q5.replace("__DB__", "\"foodmart-mysql\".\"foodmart\"");
        String druidQuery = Q5.replace("__DB__", "\"foodmart\".\"foodmart\"");
        QueryResult druidResult = controller.query(mySqlQuery);
        QueryResult mySqlResult = controller.query(druidQuery);
        assertTrue(areCloseEnough(mySqlResult.getContent(), druidResult.getContent()));
    }

    @Test public void testDruidMySqlEquivalence6() {
        QueryController controller = new QueryController();
        String mySqlQuery = Q6.replace("__DB__", "\"foodmart-mysql\".\"foodmart\"");
        String druidQuery = Q6.replace("__DB__", "\"foodmart\".\"foodmart\"");
        QueryResult druidResult = controller.query(mySqlQuery);
        QueryResult mySqlResult = controller.query(druidQuery);
        assertTrue(areCloseEnough(mySqlResult.getContent(), druidResult.getContent()));
    }

    @Test public void testDruidMySqlEquivalence7() {
        QueryController controller = new QueryController();
        String mySqlQuery = Q7.replace("__DB__", "\"foodmart-mysql\".\"foodmart\"");
        String druidQuery = Q7.replace("__DB__", "\"foodmart\".\"foodmart\"");
        QueryResult druidResult = controller.query(mySqlQuery);
        QueryResult mySqlResult = controller.query(druidQuery);
        assertTrue(areCloseEnough(mySqlResult.getContent(), druidResult.getContent()));
    }

    @Test public void testDruidMySqlEquivalence8() {
        QueryController controller = new QueryController();
        String mySqlQuery = Q8.replace("__DB__", "\"foodmart-mysql\".\"foodmart\"");
        String druidQuery = Q8.replace("__DB__", "\"foodmart\".\"foodmart\"");
        QueryResult druidResult = controller.query(mySqlQuery);
        QueryResult mySqlResult = controller.query(druidQuery);
        assertTrue(areCloseEnough(mySqlResult.getContent(), druidResult.getContent()));
    }

    @Test public void testDruidMySqlEquivalence9() {
        QueryController controller = new QueryController();
        String mySqlQuery = Q9.replace("__DB__", "\"foodmart-mysql\".\"foodmart\"");
        String druidQuery = Q9.replace("__DB__", "\"foodmart\".\"foodmart\"");
        QueryResult druidResult = controller.query(mySqlQuery);
        QueryResult mySqlResult = controller.query(druidQuery);
        assertTrue(areCloseEnough(mySqlResult.getContent(), druidResult.getContent()));
    }

    @Test public void testDruidMySqlEquivalence10() {
        QueryController controller = new QueryController();
        String mySqlQuery = Q10.replace("__DB__", "\"foodmart-mysql\".\"foodmart\"");
        String druidQuery = Q10.replace("__DB__", "\"foodmart\".\"foodmart\"");
        QueryResult druidResult = controller.query(mySqlQuery);
        QueryResult mySqlResult = controller.query(druidQuery);
        assertTrue(areCloseEnough(mySqlResult.getContent(), druidResult.getContent()));
    }

    @Test public void testDruidMySqlEquivalence11() {
        QueryController controller = new QueryController();
        String mySqlQuery = Q11.replace("__DB__", "\"foodmart-mysql\".\"foodmart\"");
        String druidQuery = Q11.replace("__DB__", "\"foodmart\".\"foodmart\"");
        QueryResult druidResult = controller.query(mySqlQuery);
        QueryResult mySqlResult = controller.query(druidQuery);
        assertTrue(areCloseEnough(mySqlResult.getContent(), druidResult.getContent()));
    }

    @Test public void testDruidMySqlEquivalence12() {
        QueryController controller = new QueryController();
        String mySqlQuery = Q12.replace("__DB__", "\"foodmart-mysql\".\"foodmart\"");
        String druidQuery = Q12.replace("__DB__", "\"foodmart\".\"foodmart\"");
        QueryResult druidResult = controller.query(mySqlQuery);
        QueryResult mySqlResult = controller.query(druidQuery);
        assertTrue(areCloseEnough(mySqlResult.getContent(), druidResult.getContent()));
    }

    @Test public void testDruidMySqlEquivalence13() {
        QueryController controller = new QueryController();
        String mySqlQuery = Q13.replace("__DB__", "\"foodmart-mysql\".\"foodmart\"");
        String druidQuery = Q13.replace("__DB__", "\"foodmart\".\"foodmart\"");
        QueryResult druidResult = controller.query(mySqlQuery);
        QueryResult mySqlResult = controller.query(druidQuery);
        assertTrue(areCloseEnough(mySqlResult.getContent(), druidResult.getContent()));
    }

    @Test public void testDruidMySqlEquivalence14() {
        QueryController controller = new QueryController();
        String mySqlQuery = Q14.replace("__DB__", "\"foodmart-mysql\".\"foodmart\"");
        String druidQuery = Q14.replace("__DB__", "\"foodmart\".\"foodmart\"");
        QueryResult druidResult = controller.query(mySqlQuery);
        QueryResult mySqlResult = controller.query(druidQuery);
        assertTrue(areCloseEnough(mySqlResult.getContent(), druidResult.getContent()));
    }

    @Test public void testDruidMySqlEquivalence15() {
        QueryController controller = new QueryController();
        String mySqlQuery = Q15.replace("__DB__", "\"foodmart-mysql\".\"foodmart\"");
        String druidQuery = Q15.replace("__DB__", "\"foodmart\".\"foodmart\"");
        QueryResult druidResult = controller.query(mySqlQuery);
        QueryResult mySqlResult = controller.query(druidQuery);
        assertTrue(areCloseEnough(mySqlResult.getContent(), druidResult.getContent()));
    }

    @Test public void testDruidMySqlEquivalence16() {
        QueryController controller = new QueryController();
        String mySqlQuery = Q16.replace("__DB__", "\"foodmart-mysql\".\"foodmart\"");
        String druidQuery = Q16.replace("__DB__", "\"foodmart\".\"foodmart\"");
        QueryResult druidResult = controller.query(mySqlQuery);
        QueryResult mySqlResult = controller.query(druidQuery);
        assertTrue(areCloseEnough(mySqlResult.getContent(), druidResult.getContent()));
    }

    @Test public void testDruidMySqlEquivalence17() {
        QueryController controller = new QueryController();
        String mySqlQuery = Q17.replace("__DB__", "\"foodmart-mysql\".\"foodmart\"");
        String druidQuery = Q17.replace("__DB__", "\"foodmart\".\"foodmart\"");
        QueryResult druidResult = controller.query(mySqlQuery);
        QueryResult mySqlResult = controller.query(druidQuery);
        assertTrue(areCloseEnough(mySqlResult.getContent(), druidResult.getContent()));
    }

    @Test public void testDruidMySqlEquivalence18() {
        QueryController controller = new QueryController();
        String mySqlQuery = Q18.replace("__DB__", "\"foodmart-mysql\".\"foodmart\"");
        String druidQuery = Q18.replace("__DB__", "\"foodmart\".\"foodmart\"");
        QueryResult druidResult = controller.query(mySqlQuery);
        QueryResult mySqlResult = controller.query(druidQuery);
        assertTrue(areCloseEnough(mySqlResult.getContent(), druidResult.getContent()));
    }

    @Test public void testDruidMySqlEquivalence19() {
        QueryController controller = new QueryController();
        String mySqlQuery = Q19.replace("__DB__", "\"foodmart-mysql\".\"foodmart\"");
        String druidQuery = Q19.replace("__DB__", "\"foodmart\".\"foodmart\"");
        QueryResult druidResult = controller.query(mySqlQuery);
        QueryResult mySqlResult = controller.query(druidQuery);
        assertTrue(areCloseEnough(mySqlResult.getContent(), druidResult.getContent()));
    }

    @Test public void testDruidMySqlEquivalence20() {
        QueryController controller = new QueryController();
        String mySqlQuery = Q20.replace("__DB__", "\"foodmart-mysql\".\"foodmart\"");
        String druidQuery = Q20.replace("__DB__", "\"foodmart\".\"foodmart\"");
        QueryResult druidResult = controller.query(mySqlQuery);
        QueryResult mySqlResult = controller.query(druidQuery);
        assertTrue(areCloseEnough(mySqlResult.getContent(), druidResult.getContent()));
    }

    @Test public void testDruidMySqlEquivalence21() {
        QueryController controller = new QueryController();
        String mySqlQuery = Q21.replace("__DB__", "\"foodmart-mysql\".\"foodmart\"");
        String druidQuery = Q21.replace("__DB__", "\"foodmart\".\"foodmart\"");
        QueryResult druidResult = controller.query(mySqlQuery);
        QueryResult mySqlResult = controller.query(druidQuery);
        assertTrue(areCloseEnough(mySqlResult.getContent(), druidResult.getContent()));
    }

    @Test public void testDruidMySqlEquivalence22() {
        QueryController controller = new QueryController();
        String mySqlQuery = Q22.replace("__DB__", "\"foodmart-mysql\".\"foodmart\"");
        String druidQuery = Q22.replace("__DB__", "\"foodmart\".\"foodmart\"");
        QueryResult druidResult = controller.query(mySqlQuery);
        QueryResult mySqlResult = controller.query(druidQuery);
        assertTrue(areCloseEnough(mySqlResult.getContent(), druidResult.getContent()));
    }

    @Test public void testDruidMySqlEquivalence23() {
        QueryController controller = new QueryController();
        String mySqlQuery = Q23.replace("__DB__", "\"foodmart-mysql\".\"foodmart\"");
        String druidQuery = Q23.replace("__DB__", "\"foodmart\".\"foodmart\"");
        QueryResult druidResult = controller.query(mySqlQuery);
        QueryResult mySqlResult = controller.query(druidQuery);
        assertTrue(areCloseEnough(mySqlResult.getContent(), druidResult.getContent()));
    }

    @Test public void testDruidMySqlEquivalence24() {
        QueryController controller = new QueryController();
        String mySqlQuery = Q24.replace("__DB__", "\"foodmart-mysql\".\"foodmart\"");
        String druidQuery = Q24.replace("__DB__", "\"foodmart\".\"foodmart\"");
        QueryResult druidResult = controller.query(mySqlQuery);
        QueryResult mySqlResult = controller.query(druidQuery);
        assertTrue(areCloseEnough(mySqlResult.getContent(), druidResult.getContent()));
    }

    @Test public void testDruidMySqlEquivalence25() {
        QueryController controller = new QueryController();
        String mySqlQuery = Q25.replace("__DB__", "\"foodmart-mysql\".\"foodmart\"");
        String druidQuery = Q25.replace("__DB__", "\"foodmart\".\"foodmart\"");
        QueryResult druidResult = controller.query(mySqlQuery);
        QueryResult mySqlResult = controller.query(druidQuery);
        assertTrue(areCloseEnough(mySqlResult.getContent(), druidResult.getContent()));
    }

    @Test public void testDruidMySqlEquivalence26() {
        QueryController controller = new QueryController();
        String mySqlQuery = Q26.replace("__DB__", "\"foodmart-mysql\".\"foodmart\"");
        String druidQuery = Q26.replace("__DB__", "\"foodmart\".\"foodmart\"");
        QueryResult druidResult = controller.query(mySqlQuery);
        QueryResult mySqlResult = controller.query(druidQuery);
        assertTrue(areCloseEnough(mySqlResult.getContent(), druidResult.getContent()));
    }

    @Test public void testDruidMySqlEquivalence27() {
        QueryController controller = new QueryController();
        String mySqlQuery = Q27.replace("__DB__", "\"foodmart-mysql\".\"foodmart\"");
        String druidQuery = Q27.replace("__DB__", "\"foodmart\".\"foodmart\"");
        QueryResult druidResult = controller.query(mySqlQuery);
        QueryResult mySqlResult = controller.query(druidQuery);
        assertTrue(areCloseEnough(mySqlResult.getContent(), druidResult.getContent()));
    }

    @Test public void testDruidMySqlEquivalence28() {
        QueryController controller = new QueryController();
        String mySqlQuery = Q28.replace("__DB__", "\"foodmart-mysql\".\"foodmart\"");
        String druidQuery = Q28.replace("__DB__", "\"foodmart\".\"foodmart\"");
        QueryResult druidResult = controller.query(mySqlQuery);
        QueryResult mySqlResult = controller.query(druidQuery);
        assertTrue(areCloseEnough(mySqlResult.getContent(), druidResult.getContent()));
    }

    @Test public void testDruidMySqlEquivalence29() {
        QueryController controller = new QueryController();
        String mySqlQuery = Q29.replace("__DB__", "\"foodmart-mysql\".\"foodmart\"");
        String druidQuery = Q29.replace("__DB__", "\"foodmart\".\"foodmart\"");
        QueryResult druidResult = controller.query(mySqlQuery);
        QueryResult mySqlResult = controller.query(druidQuery);
        assertTrue(areCloseEnough(mySqlResult.getContent(), druidResult.getContent()));
    }

    @Test public void testDruidMySqlEquivalence30() {
        QueryController controller = new QueryController();
        String mySqlQuery = Q30.replace("__DB__", "\"foodmart-mysql\".\"foodmart\"");
        String druidQuery = Q30.replace("__DB__", "\"foodmart\".\"foodmart\"");
        QueryResult druidResult = controller.query(mySqlQuery);
        QueryResult mySqlResult = controller.query(druidQuery);
        assertTrue(areCloseEnough(mySqlResult.getContent(), druidResult.getContent()));
    }

    @Test public void testDruidMySqlEquivalence31() {
        QueryController controller = new QueryController();
        String mySqlQuery = Q31.replace("__DB__", "\"foodmart-mysql\".\"foodmart\"");
        String druidQuery = Q31.replace("__DB__", "\"foodmart\".\"foodmart\"");
        QueryResult druidResult = controller.query(mySqlQuery);
        QueryResult mySqlResult = controller.query(druidQuery);
        assertTrue(areCloseEnough(mySqlResult.getContent(), druidResult.getContent()));
    }

    @Test public void testDruidMySqlEquivalence32() {
        QueryController controller = new QueryController();
        String mySqlQuery = Q32.replace("__DB__", "\"foodmart-mysql\".\"foodmart\"");
        String druidQuery = Q32.replace("__DB__", "\"foodmart\".\"foodmart\"");
        QueryResult druidResult = controller.query(mySqlQuery);
        QueryResult mySqlResult = controller.query(druidQuery);
        assertTrue(areCloseEnough(mySqlResult.getContent(), druidResult.getContent()));
    }

    @Test public void testDruidMySqlEquivalence33() {
        QueryController controller = new QueryController();
        String mySqlQuery = Q33.replace("__DB__", "\"foodmart-mysql\".\"foodmart\"");
        String druidQuery = Q33.replace("__DB__", "\"foodmart\".\"foodmart\"");
        QueryResult druidResult = controller.query(mySqlQuery);
        QueryResult mySqlResult = controller.query(druidQuery);
        assertTrue(areCloseEnough(mySqlResult.getContent(), druidResult.getContent()));
    }

    @Test public void testDruidMySqlEquivalence34() {
        QueryController controller = new QueryController();
        String mySqlQuery = Q34.replace("__DB__", "\"foodmart-mysql\".\"foodmart\"");
        String druidQuery = Q34.replace("__DB__", "\"foodmart\".\"foodmart\"");
        QueryResult druidResult = controller.query(mySqlQuery);
        QueryResult mySqlResult = controller.query(druidQuery);
        assertTrue(areCloseEnough(mySqlResult.getContent(), druidResult.getContent()));
    }
}
