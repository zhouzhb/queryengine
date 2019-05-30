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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Tests that MySQL and Druid datasources have the same data.
 * Requirements:
 * - MySQL VM is set up using steps at https://github.com/zhouzhb/queryengine/blob/val/MySQL-VM.txt
 * - Druid VM is set up using steps at https://github.com/zhouzhb/queryengine/blob/val/Druid-VM.txt
 */
@Ignore
public class DataSourceEquivalenceTest {

    private final String Q1
        = "select count(*) as c, \"strInc1\""
        + " from __DB__ __TIME__"
        + " and \"strRep1\" = 'strstrstrstrstrstr1'"
        + " group by \"strInc1\"";

    private final String Q2
        = "select sum(\"dblInc1\") / sum(\"dblRep1\") as \"profitRatio\","
        + " \"strInc1\""
        + " from __DB__ __TIME__"
        + " and \"dblRep1\" > 0"
        + " group by \"strInc1\"";

    private final String Q3
        = "select sum(\"dblInc1\") + sum(\"dblRep1\"),"
        + " \"strInc1\""
        + " from __DB__ __TIME__"
        + " group by \"strInc1\"";

    private final String Q4
        = "select 100 * sum(\"dblInc1\") / (sum(\"dblInc1\") + sum(\"dblRep1\")), \"strInc1\""
        + " from __DB__ __TIME__"
        + " group by \"strInc1\"";

    private final String Q5
        = "select distinct \"strInc1\", \"strRep1\""
        + " from __DB__ __TIME__";

    private final String Q6
        = "select sum(\"dblInc1\") + sum(\"dblRep1\"),"
        + " \"strInc1\""
        + " from __DB__ __TIME__"
        + " and (\"dblInc1\" > 0 or \"dblRep1\" > 0)"
        + " group by \"strInc1\"";

    private final String Q7
        = "select sum(\"dblRep1\") / sum(\"dblInc1\") as \"profitRatio\","
        + " \"strInc1\""
        + " from __DB__ __TIME__"
        + " group by \"strInc1\"";

    private final String Q8
        = "select count(*) as c,"
        + " \"strInc1\""
        + " from __DB__ __TIME__"
        + " group by \"strInc1\"";

    private final String Q9
        = "select count(*) as c, \"strInc1\", \"strRep1\""
        + " from __DB__ __TIME__"
        + " and \"strRep1\" = 'strstrstrstrstrstr1'"
        + " group by \"strInc1\", \"strRep1\", floor(\"time\" to DAY)";

    private final String Q10
        = "select sum(\"dblInc1\") + sum(\"dblRep1\"),"
        + " \"strInc1\", \"strRep1\""
        + " from __DB__ __TIME__"
        + " and (\"dblRep1\" > 0 or \"dblInc1\" > 0)"
        + " group by \"strInc1\", \"strRep1\", floor(\"time\" to DAY)";

    private final String Q11
        = "select sum(case when \"dblInc1\" <> -1 then \"dblInc1\" else 0 end)"
        + " + sum(case when \"dblRep1\" <> -1 then \"dblRep1\" else 0 end),"
        + " \"strInc1\", \"strRep1\""
        + " from __DB__ __TIME__"
        + " and (\"dblRep1\" > 0 or \"dblInc1\" > 0)"
        + " group by \"strInc1\", \"strRep1\", floor(\"time\" to DAY)";

    private final String Q12
        = "select count(*) as c, \"strInc1\", \"strRep1\""
        + " from __DB__ __TIME__"
        + " and \"intInc1\" = 1020"
        + " group by \"strInc1\", \"strRep1\", floor(\"time\" to DAY)";

    private final String Q13
        = "select sum(\"dblInc1\") + sum(\"dblRep1\"),"
        + " \"strInc1\", \"strRep1\""
        + " from __DB__ __TIME__"
        + " and \"intInc1\" > 1000"
        + " group by \"strInc1\", \"strRep1\"";

    private final String Q14
        = "select count(*), \"strInc1\", \"strRep1\""
        + " from __DB__ __TIME__"
        + " and \"intInc1\" > 1000"
        + " group by \"strInc1\", \"strRep1\", \"store_state\"";

    private final String Q15
        = "select sum(\"dblInc1\") + sum(\"dblRep1\"),"
        + " \"strInc1\""
        + " from __DB__ __TIME__"
        + " and \"intInc1\" > 1000"
        + " group by \"strInc1\"";

    private final String Q16
        = "select count(*)"
        + " from __DB__ __TIME__"
        + " and \"dblRep1\" > 0";

    private final String Q17
        = "select floor(\"time\" to HOUR) as \"ts\","
        + " 100 * sum(\"dblInc1\") / (sum(\"dblInc1\") + sum(\"dblRep1\"))"
        + " from __DB__ __TIME__"
        + " group by floor(\"time\" to HOUR)";

    private final String Q18
        = "select count(distinct \"strInc1\")"
        + " from __DB__ __TIME__"
        + " and \"dblRep1\" > 0";

    private final String Q19
        = "select count(distinct \"strInc1\")"
        + " from __DB__ __TIME__";

    private final String Q20
        = "select floor(\"time\" to DAY) as \"ts\", count(*) as \"total\""
        + " from __DB__ __TIME__"
        + " group by floor(\"time\" to DAY), \"strInc1\"";

    private final String Q21
        = "select floor(\"time\" to HOUR) as \"ts\", count(*) as \"total\""
        + " from __DB__ __TIME__"
        + " group by floor(\"time\" to HOUR), \"strInc1\"";

    private final String Q22
        = "select count(distinct \"strInc1\")"
        + " from __DB__ __TIME__"
        + " and (\"dblRep1\" > 0 or \"dblInc1\" > 0)";

    private final String Q23
        = "select floor(\"time\" to HOUR) as \"ts\","
        + " sum(\"dblInc1\") / sum(\"dblRep1\")"
        + " from __DB__ __TIME__"
        + " group by floor(\"time\" to HOUR)";

    private final String Q24
        = "select count(distinct \"strInc1\")"
        + " from __DB__ __TIME__"
        + " and \"strRep1\" = 'strstrstrstrstrstr1'";

    private final String Q25
        = "select floor(\"time\" to HOUR) as \"ts\","
        + " sum(\"dblInc1\") / sum(\"dblRep1\")"
        + " from __DB__ __TIME__"
        + " and \"dblRep1\" > 0"
        + " group by floor(\"time\" to HOUR)";

    private final String Q26
        = "select \"intInc1\", \"strInc1\", \"strRep2\", \"SKU\", \"SRP\","
        + " \"gross_weight\", \"net_weight\", \"recyclable_package\", \"low_fat\", \"units_per_case\","
        + " \"cases_per_pallet\", \"shelf_width\", \"shelf_height\", \"shelf_depth\", \"product_class_id\","
        + " \"product_subcategory\""
        + " from __DB__ __TIME__"
        + " and \"strRep1\" = 'strstrstrstrstrstr1'"
        + " order by \"strInc1\", \"strRep2\", \"SKU\", \"SRP\""
        + " limit 10";

    private final String Q27
        = "select \"intInc1\", \"strInc1\", \"strRep2\""
        + " from __DB__ __TIME__";

    private final String Q28
        = "select count(*), sum(\"dblInc1\")"
        + " from __DB__ __TIME__"
        + " group by \"strInc1\"";

    private final String Q29
        = "select count(*) as \"t\""
        + " from __DB__ __TIME__"
        + " group by \"lname\", \"intInc1\", \"city\""
        + " order by count(*) desc";

    private final String Q30
        = "select avg(\"dblInc1\"), avg(\"dblRep1\")"
        + " from __DB__ __TIME__"
        + " group by \"city\", \"state_province\"";

    private final String Q31
        = "select count(*)"
        + " from __DB__ __TIME__";

    private final String Q32
        = "select \"intInc1\", \"strInc1\", \"strRep2\","
        + " \"SKU\", \"SRP\", \"gross_weight\", \"net_weight\""
        + " from __DB__ __TIME__";

    private final String Q33
        = "select count(*) as \"c\", \"intInc1\",\"strInc1\",\"strRep2\""
        + " from __DB__ __TIME__"
        + " group by \"intInc1\",\"strInc1\",\"strRep2\""
        + " order by \"c\" desc";

    private final String Q34
        = "select \"time\", \"intInc1\", \"strInc1\","
        + " \"strRep2\", \"SKU\", \"SRP\","
        + " \"gross_weight\",\"net_weight\""
        + " from __DB__ __TIME__"
        + " order by \"strInc1\", \"strRep2\", \"SKU\", \"SRP\" desc"
        + " limit 10";

//    private final int MAX_SIGNIFICANT_DIGITS = 4;
//    private final int MAX_DIFF_PERCENTAGE = 5;
//
//    /**
//     * Compares 2 content values from {@link QueryResult} and decides if they are close enough. We define them to be close enough if all these conditions are satisfied:
//     * - If number of different rows is no more than {@link DataSourceEquivalenceTest#MAX_DIFF_PERCENTAGE}.
//     * - If a row has floating point value, only {@link DataSourceEquivalenceTest#MAX_SIGNIFICANT_DIGITS} significant digits are used.
//     * - If a row has number value in billions/millions/thostrstrstrstrstrstr1nds, replace it with a string ...B/...M/...K.
//     * - If a row has time value, convert it to Unix epoch time.
//     * @param content1 The first content to compare
//     * @param content2 The second content to compare
//     * @return True if contents are close enough, false otherwise
//     */
//    public boolean areCloseEnough(String content1, String content2) {
//        // Convert contents to sets
//        Set<String> set1 = rowsToSet(content1);
//        Set<String> set2 = rowsToSet(content2);
//
//        // Verify they have a lot of the same rows
//        Set<String> diff = Sets.symmetricDifference(set1, set2);
//        int diffCount = diff.size();
//        int setCount = set1.size() + set2.size();
//        double diffPercentage = 100d * diffCount / setCount;
//        return diffPercentage < MAX_DIFF_PERCENTAGE;
//    }
//
//    /**
//     * Convert rows of data as a string where rows are separated by new lines and columns by semicolon to a set.
//     * @param rowsString
//     * @return set of rows
//     */
//    private Set<String> rowsToSet(final String rowsString) {
//        Set<String> ret = Sets.newHashSet();
//        String[] rows = rowsString.split("\\n");
//        for (String row : rows) {
//            String[] cols = row.split(";");
//            String rowSetElement = "";
//            for (String col : cols) {
//                String[] nameValuePair = col.split("=");
//                String colName = nameValuePair[0];
//                String colValue = nameValuePair[1];
//
//                // convert time to Unix epoch time
//                try {
//                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                    sdf.setTimeZone(java.util.TimeZone.getTimeZone("UTC"));
//                    Date dt = sdf.parse(colValue);
//                    long epoch = dt.getTime();
//                    colValue = Long.toString(epoch);
//                } catch (ParseException e) {
//                }
//
//                // if column value is a number - simplify its value
//                try {
//                    double doubleVal = Double.valueOf(colValue).doubleValue();
//                    String newColVal;
//                    if (doubleVal > 1000000000) {
//                        newColVal = (int) (doubleVal / 1000000000) + "B";
//                    } else if (doubleVal > 1000000) {
//                        newColVal = (int) (doubleVal / 1000000) + "M";
//                    } else if (doubleVal > 1000) {
//                        newColVal = (int) (doubleVal / 1000) + "K";
//                    } else {
//                        newColVal = String.format("%." + MAX_SIGNIFICANT_DIGITS + "f", doubleVal);
//                    }
//                    rowSetElement += colName + "=" + newColVal + ";";
//                } catch (NumberFormatException nfe) {
//                    rowSetElement += colName + colValue.toUpperCase() + ";";
//                }
//            }
//            ret.add(rowSetElement);
//        }
//        return ret;
//    }

    @Test public void testDruidMySqlEquivalence1() {
        QueryController controller = new QueryController();
        String mySqlQuery = generateMySqlQueryFromTemplate(Q1);
        String druidQuery = generateDruidQueryFromTemplate(Q1);
        QueryResult mySqlResult = controller.query(mySqlQuery);
        QueryResult druidResult = controller.query(druidQuery);
        assertEquals(mySqlResult.getContent(), druidResult.getContent());
        assertNotNull(mySqlResult.getContent());
        assertNotNull(druidResult.getContent());
    }

    @Test public void testDruidMySqlEquivalence2() {
        QueryController controller = new QueryController();
        String mySqlQuery = generateMySqlQueryFromTemplate(Q2);
        String druidQuery = generateDruidQueryFromTemplate(Q2);//select sum("dblInc1") / sum("dblRep1") as "profitRatio", "strInc1" from "druid"."test"  where "__time" >= '2019-01-01T00:00:00Z' and "__time" < '2020-01-01T00:00:00Z'  and "dblRep1" > 0 group by "strInc1"
        QueryResult mySqlResult = controller.query(mySqlQuery);
        QueryResult druidResult = controller.query(druidQuery);
        assertEquals(mySqlResult.getContent(), druidResult.getContent());
        assertNotNull(mySqlResult.getContent());
        assertNotNull(druidResult.getContent());
    }

    @Test public void testDruidMySqlEquivalence3() {
        QueryController controller = new QueryController();
        String mySqlQuery = generateMySqlQueryFromTemplate(Q3);
        String druidQuery = generateDruidQueryFromTemplate(Q3);
        QueryResult mySqlResult = controller.query(mySqlQuery);
        QueryResult druidResult = controller.query(druidQuery);
        assertEquals(mySqlResult.getContent(), druidResult.getContent());
        assertNotNull(mySqlResult.getContent());
        assertNotNull(druidResult.getContent());
    }

    @Test public void testDruidMySqlEquivalence4() {
        QueryController controller = new QueryController();
        String mySqlQuery = generateMySqlQueryFromTemplate(Q4);
        String druidQuery = generateDruidQueryFromTemplate(Q4);
        QueryResult mySqlResult = controller.query(mySqlQuery);
        QueryResult druidResult = controller.query(druidQuery);
        assertEquals(mySqlResult.getContent(), druidResult.getContent());
        assertNotNull(mySqlResult.getContent());
        assertNotNull(druidResult.getContent());
    }

    @Test public void testDruidMySqlEquivalence5() {
        QueryController controller = new QueryController();
        String mySqlQuery = generateMySqlQueryFromTemplate(Q5);
        String druidQuery = generateDruidQueryFromTemplate(Q5);
        QueryResult mySqlResult = controller.query(mySqlQuery);
        QueryResult druidResult = controller.query(druidQuery);
        assertEquals(mySqlResult.getContent(), druidResult.getContent());
        assertNotNull(mySqlResult.getContent());
        assertNotNull(druidResult.getContent());
    }

    @Test public void testDruidMySqlEquivalence6() {
        QueryController controller = new QueryController();
        String mySqlQuery = generateMySqlQueryFromTemplate(Q6);
        String druidQuery = generateDruidQueryFromTemplate(Q6);
        QueryResult mySqlResult = controller.query(mySqlQuery);
        QueryResult druidResult = controller.query(druidQuery);
        assertEquals(mySqlResult.getContent(), druidResult.getContent());
        assertNotNull(mySqlResult.getContent());
        assertNotNull(druidResult.getContent());
    }

    @Test public void testDruidMySqlEquivalence7() {
        QueryController controller = new QueryController();
        String mySqlQuery = generateMySqlQueryFromTemplate(Q7);
        String druidQuery = generateDruidQueryFromTemplate(Q7);
        QueryResult mySqlResult = controller.query(mySqlQuery);
        QueryResult druidResult = controller.query(druidQuery);
        assertEquals(mySqlResult.getContent(), druidResult.getContent());
        assertNotNull(mySqlResult.getContent());
        assertNotNull(druidResult.getContent());
    }

    @Test public void testDruidMySqlEquivalence8() {
        QueryController controller = new QueryController();
        String mySqlQuery = generateMySqlQueryFromTemplate(Q8);
        String druidQuery = generateDruidQueryFromTemplate(Q8);
        QueryResult mySqlResult = controller.query(mySqlQuery);
        QueryResult druidResult = controller.query(druidQuery);
        assertEquals(mySqlResult.getContent(), druidResult.getContent());
        assertNotNull(mySqlResult.getContent());
        assertNotNull(druidResult.getContent());
    }

    @Test public void testDruidMySqlEquivalence9() {
        QueryController controller = new QueryController();
        String mySqlQuery = generateMySqlQueryFromTemplate(Q9);
        String druidQuery = generateDruidQueryFromTemplate(Q9);
        QueryResult mySqlResult = controller.query(mySqlQuery);
        QueryResult druidResult = controller.query(druidQuery);
        assertEquals(mySqlResult.getContent(), druidResult.getContent());
        assertNotNull(mySqlResult.getContent());
        assertNotNull(druidResult.getContent());
    }

    @Test public void testDruidMySqlEquivalence10() {
        QueryController controller = new QueryController();
        String mySqlQuery = generateMySqlQueryFromTemplate(Q10);
        String druidQuery = generateDruidQueryFromTemplate(Q10);
        QueryResult mySqlResult = controller.query(mySqlQuery);
        QueryResult druidResult = controller.query(druidQuery);
        assertEquals(mySqlResult.getContent(), druidResult.getContent());
        assertNotNull(mySqlResult.getContent());
        assertNotNull(druidResult.getContent());
    }

    private String generateDruidQueryFromTemplate(final String queryTemplate) {
        return queryTemplate.replace("__DB__", "\"druid\".\"test\"")
            .replace("__TIME__", " where \"__time\" >= '2019-01-01T00:00:00Z'"
                + " and \"__time\" < '2020-01-01T00:00:00Z' ")
            .replace("\"time\"", "\"__time\"");
    }

    private String generateMySqlQueryFromTemplate(final String queryTemplate) {
        return queryTemplate.replace("__DB__", "\"mysql\".\"test\"")
            .replace("__TIME__", " where \"time484c39f77ace4808a5a48f6b481bafbe\" >= '2019-01-01 00:00:00 UTC'"
                + " and \"time484c39f77ace4808a5a48f6b481bafbe\" < '2020-01-01 00:00:00 UTC' ")
            .replace("\"time\"", "\"time484c39f77ace4808a5a48f6b481bafbe\"");
    }

}
