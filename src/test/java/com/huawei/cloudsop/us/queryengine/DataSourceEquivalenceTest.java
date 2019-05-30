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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Tests that MySQL and Druid datasources have the same data.
 * Requirements:
 * - MySQL VM is set up using steps at https://github.com/zhouzhb/queryengine/blob/val/doc/MySQL-VM.txt
 * - Druid VM is set up using steps at https://github.com/zhouzhb/queryengine/blob/val/doc/Druid-VM.txt
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
        = "select count(*) as c, \"strInc1\", floor(\"time\" to DAY)"
        + " from __DB__ __TIME__"
        + " and \"strRep1\" = 'strstrstrstrstrstr1'"
        + " group by \"strInc1\", floor(\"time\" to DAY)";

    private final String Q13
        = "select sum(\"dblInc1\") + sum(\"dblRep1\"),"
        + " \"strInc1\", \"strRep1\""
        + " from __DB__ __TIME__"
        + " and \"strRep1\" > 'strstrstrstrstrstr1'"
        + " group by \"strInc1\", \"strRep1\"";

    private final String Q14
        = "select count(*), \"strRep1\", \"strRep2\""
        + " from __DB__ __TIME__"
        + " and \"strInc1\" > 'strstrstrstrstrstr1'"
        + " group by \"strRep1\", \"strRep2\"";

    private final String Q15
        = "select sum(\"dblInc1\") + sum(\"dblRep1\"),"
        + " \"strInc1\""
        + " from __DB__ __TIME__"
        + " and \"strRep1\" > 'strstrstrstrstrstr1'"
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
        = "select \"strInc1\", \"strRep1\""
        + " from __DB__ __TIME__"
        + " and \"strRep1\" = 'strstrstrstrstrstr1'"
        + " order by \"strInc1\", \"strRep1\""
        + " limit 10";

    private final String Q27
        = "select \"strInc1\", \"strRep2\""
        + " from __DB__ __TIME__";

    private final String Q28
        = "select count(*), sum(\"dblInc1\")"
        + " from __DB__ __TIME__"
        + " group by \"strInc1\"";

    private final String Q29
        = "select count(*) as \"t\""
        + " from __DB__ __TIME__"
        + " group by \"strRep1\", \"strRep2\", \"strRep3\""
        + " order by count(*) desc";

    private final String Q30
        = "select avg(\"dblInc1\"), avg(\"dblRep1\")"
        + " from __DB__ __TIME__"
        + " group by \"strRep1\", \"strRep2\"";

    private final String Q31
        = "select count(*)"
        + " from __DB__ __TIME__";

    private final String Q32
        = "select \"strRep1\", \"strRep2\""
        + " from __DB__ __TIME__";

    private final String Q33
        = "select count(*) as \"c\", \"strRep1\", \"strRep2\""
        + " from __DB__ __TIME__"
        + " group by \"strRep1\", \"strRep2\""
        + " order by \"c\" desc";

    private final String Q34
        = "select \"strRep1\", \"strRep2\""
        + " from __DB__ __TIME__"
        + " order by \"strRep1\", \"strRep2\" desc"
        + " limit 10";

    @Test public void testDruidMySqlEquivalence1() {
        QueryController controller = new QueryController();
        String mySqlQuery = generateMySqlQueryFromTemplate(Q1);
        String druidQuery = generateDruidQueryFromTemplate(Q1);
        QueryResult mySqlResult = controller.query(mySqlQuery);
        QueryResult druidResult = controller.query(druidQuery);
        assertEquals(mySqlResult.getContent(), druidResult.getContent());
        assertNotNull(mySqlResult.getContent());
        assertNotNull(druidResult.getContent());
        assertFalse("".equals(mySqlResult.getContent()));
        assertFalse("".equals(druidResult.getContent()));
    }

    @Test public void testDruidMySqlEquivalence2() {
        QueryController controller = new QueryController();
        String mySqlQuery = generateMySqlQueryFromTemplate(Q2);
        String druidQuery = generateDruidQueryFromTemplate(Q2);
        QueryResult mySqlResult = controller.query(mySqlQuery);
        QueryResult druidResult = controller.query(druidQuery);
        assertEquals(mySqlResult.getContent(), druidResult.getContent());
        assertNotNull(mySqlResult.getContent());
        assertNotNull(druidResult.getContent());
        assertFalse("".equals(mySqlResult.getContent()));
        assertFalse("".equals(druidResult.getContent()));
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
        assertFalse("".equals(mySqlResult.getContent()));
        assertFalse("".equals(druidResult.getContent()));
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
        assertFalse("".equals(mySqlResult.getContent()));
        assertFalse("".equals(druidResult.getContent()));
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
        assertFalse("".equals(mySqlResult.getContent()));
        assertFalse("".equals(druidResult.getContent()));
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
        assertFalse("".equals(mySqlResult.getContent()));
        assertFalse("".equals(druidResult.getContent()));
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
        assertFalse("".equals(mySqlResult.getContent()));
        assertFalse("".equals(druidResult.getContent()));
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
        assertFalse("".equals(mySqlResult.getContent()));
        assertFalse("".equals(druidResult.getContent()));
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
        assertFalse("".equals(mySqlResult.getContent()));
        assertFalse("".equals(druidResult.getContent()));
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
        assertFalse("".equals(mySqlResult.getContent()));
        assertFalse("".equals(druidResult.getContent()));
    }

    @Test public void testDruidMySqlEquivalence11() {
        QueryController controller = new QueryController();
        String mySqlQuery = generateMySqlQueryFromTemplate(Q11);
        String druidQuery = generateDruidQueryFromTemplate(Q11);
        QueryResult mySqlResult = controller.query(mySqlQuery);
        QueryResult druidResult = controller.query(druidQuery);
        assertEquals(mySqlResult.getContent(), druidResult.getContent());
        assertNotNull(mySqlResult.getContent());
        assertNotNull(druidResult.getContent());
        assertFalse("".equals(mySqlResult.getContent()));
        assertFalse("".equals(druidResult.getContent()));
    }

    @Test public void testDruidMySqlEquivalence12() {
        QueryController controller = new QueryController();
        String mySqlQuery = generateMySqlQueryFromTemplate(Q12);
        String druidQuery = generateDruidQueryFromTemplate(Q12);
        String mySqlResult = controller.query(mySqlQuery).getContent();
        String druidResult = controller.query(druidQuery).getContent();
        assertNotNull(mySqlResult);
        assertNotNull(druidResult);
        assertFalse("".equals(mySqlResult));
        assertFalse("".equals(druidResult));
        assertTrue(areEqualAsSets(mySqlResult, druidResult));
    }

    @Test public void testDruidMySqlEquivalence13() {
        QueryController controller = new QueryController();
        String mySqlQuery = generateMySqlQueryFromTemplate(Q13);
        String druidQuery = generateDruidQueryFromTemplate(Q13);
        String mySqlResult = controller.query(mySqlQuery).getContent();
        String druidResult = controller.query(druidQuery).getContent();
        assertNotNull(mySqlResult);
        assertNotNull(druidResult);
        assertFalse("".equals(mySqlResult));
        assertFalse("".equals(druidResult));
        assertTrue(areEqualAsSets(mySqlResult, druidResult));
    }

    @Test public void testDruidMySqlEquivalence14() {
        QueryController controller = new QueryController();
        String mySqlQuery = generateMySqlQueryFromTemplate(Q14);
        String druidQuery = generateDruidQueryFromTemplate(Q14);
        String mySqlResult = controller.query(mySqlQuery).getContent();
        String druidResult = controller.query(druidQuery).getContent();
        assertNotNull(mySqlResult);
        assertNotNull(druidResult);
        assertFalse("".equals(mySqlResult));
        assertFalse("".equals(druidResult));
        assertTrue(areEqualAsSets(mySqlResult, druidResult));
    }

    @Test public void testDruidMySqlEquivalence15() {
        QueryController controller = new QueryController();
        String mySqlQuery = generateMySqlQueryFromTemplate(Q15);
        String druidQuery = generateDruidQueryFromTemplate(Q15);
        String mySqlResult = controller.query(mySqlQuery).getContent();
        String druidResult = controller.query(druidQuery).getContent();
        assertNotNull(mySqlResult);
        assertNotNull(druidResult);
        assertFalse("".equals(mySqlResult));
        assertFalse("".equals(druidResult));
        assertTrue(areEqualAsSets(mySqlResult, druidResult));
    }

    @Test public void testDruidMySqlEquivalence16() {
        QueryController controller = new QueryController();
        String mySqlQuery = generateMySqlQueryFromTemplate(Q16);
        String druidQuery = generateDruidQueryFromTemplate(Q16);
        String mySqlResult = controller.query(mySqlQuery).getContent();
        String druidResult = controller.query(druidQuery).getContent();
        assertNotNull(mySqlResult);
        assertNotNull(druidResult);
        assertFalse("".equals(mySqlResult));
        assertFalse("".equals(druidResult));
        assertTrue(areEqualAsSets(mySqlResult, druidResult));
    }

    @Test public void testDruidMySqlEquivalence17() {
        QueryController controller = new QueryController();
        String mySqlQuery = generateMySqlQueryFromTemplate(Q17);
        String druidQuery = generateDruidQueryFromTemplate(Q17);
        String mySqlResult = controller.query(mySqlQuery).getContent();
        String druidResult = controller.query(druidQuery).getContent();
        assertNotNull(mySqlResult);
        assertNotNull(druidResult);
        assertFalse("".equals(mySqlResult));
        assertFalse("".equals(druidResult));
        assertTrue(areEqualAsSets(mySqlResult, druidResult));
    }

    @Test public void testDruidMySqlEquivalence18() {
        QueryController controller = new QueryController();
        String mySqlQuery = generateMySqlQueryFromTemplate(Q18);
        String druidQuery = generateDruidQueryFromTemplate(Q18);
        String mySqlResult = controller.query(mySqlQuery).getContent();
        String druidResult = controller.query(druidQuery).getContent();
        assertNotNull(mySqlResult);
        assertNotNull(druidResult);
        assertFalse("".equals(mySqlResult));
        assertFalse("".equals(druidResult));
        assertTrue(areEqualAsSets(mySqlResult, druidResult));
    }

    @Test public void testDruidMySqlEquivalence19() {
        QueryController controller = new QueryController();
        String mySqlQuery = generateMySqlQueryFromTemplate(Q19);
        String druidQuery = generateDruidQueryFromTemplate(Q19);
        String mySqlResult = controller.query(mySqlQuery).getContent();
        String druidResult = controller.query(druidQuery).getContent();
        assertNotNull(mySqlResult);
        assertNotNull(druidResult);
        assertFalse("".equals(mySqlResult));
        assertFalse("".equals(druidResult));
        assertTrue(areEqualAsSets(mySqlResult, druidResult));
    }

    @Test public void testDruidMySqlEquivalence20() {
        QueryController controller = new QueryController();
        String mySqlQuery = generateMySqlQueryFromTemplate(Q20);
        String druidQuery = generateDruidQueryFromTemplate(Q20);
        String mySqlResult = controller.query(mySqlQuery).getContent();
        String druidResult = controller.query(druidQuery).getContent();
        assertNotNull(mySqlResult);
        assertNotNull(druidResult);
        assertFalse("".equals(mySqlResult));
        assertFalse("".equals(druidResult));
        assertTrue(areEqualAsSets(mySqlResult, druidResult));
    }

    @Test public void testDruidMySqlEquivalence21() {
        QueryController controller = new QueryController();
        String mySqlQuery = generateMySqlQueryFromTemplate(Q21);
        String druidQuery = generateDruidQueryFromTemplate(Q21);
        String mySqlResult = controller.query(mySqlQuery).getContent();
        String druidResult = controller.query(druidQuery).getContent();
        assertNotNull(mySqlResult);
        assertNotNull(druidResult);
        assertFalse("".equals(mySqlResult));
        assertFalse("".equals(druidResult));
        assertTrue(areEqualAsSets(mySqlResult, druidResult));
    }

    @Test public void testDruidMySqlEquivalence22() {
        QueryController controller = new QueryController();
        String mySqlQuery = generateMySqlQueryFromTemplate(Q22);
        String druidQuery = generateDruidQueryFromTemplate(Q22);
        String mySqlResult = controller.query(mySqlQuery).getContent();
        String druidResult = controller.query(druidQuery).getContent();
        assertNotNull(mySqlResult);
        assertNotNull(druidResult);
        assertFalse("".equals(mySqlResult));
        assertFalse("".equals(druidResult));
        assertTrue(areEqualAsSets(mySqlResult, druidResult));
    }

    @Test public void testDruidMySqlEquivalence23() {
        QueryController controller = new QueryController();
        String mySqlQuery = generateMySqlQueryFromTemplate(Q23);
        String druidQuery = generateDruidQueryFromTemplate(Q23);
        String mySqlResult = controller.query(mySqlQuery).getContent();
        String druidResult = controller.query(druidQuery).getContent();
        assertNotNull(mySqlResult);
        assertNotNull(druidResult);
        assertFalse("".equals(mySqlResult));
        assertFalse("".equals(druidResult));
        assertTrue(areEqualAsSets(mySqlResult, druidResult));
    }

    @Test public void testDruidMySqlEquivalence24() {
        QueryController controller = new QueryController();
        String mySqlQuery = generateMySqlQueryFromTemplate(Q24);
        String druidQuery = generateDruidQueryFromTemplate(Q24);
        String mySqlResult = controller.query(mySqlQuery).getContent();
        String druidResult = controller.query(druidQuery).getContent();
        assertNotNull(mySqlResult);
        assertNotNull(druidResult);
        assertFalse("".equals(mySqlResult));
        assertFalse("".equals(druidResult));
        assertTrue(areEqualAsSets(mySqlResult, druidResult));
    }

    @Test public void testDruidMySqlEquivalence25() {
        QueryController controller = new QueryController();
        String mySqlQuery = generateMySqlQueryFromTemplate(Q25);
        String druidQuery = generateDruidQueryFromTemplate(Q25);
        String mySqlResult = controller.query(mySqlQuery).getContent();
        String druidResult = controller.query(druidQuery).getContent();
        assertNotNull(mySqlResult);
        assertNotNull(druidResult);
        assertFalse("".equals(mySqlResult));
        assertFalse("".equals(druidResult));
        assertTrue(areEqualAsSets(mySqlResult, druidResult));
    }

    @Test public void testDruidMySqlEquivalence26() {
        QueryController controller = new QueryController();
        String mySqlQuery = generateMySqlQueryFromTemplate(Q26);
        String druidQuery = generateDruidQueryFromTemplate(Q26);
        String mySqlResult = controller.query(mySqlQuery).getContent();
        String druidResult = controller.query(druidQuery).getContent();
        assertNotNull(mySqlResult);
        assertNotNull(druidResult);
        assertFalse("".equals(mySqlResult));
        assertFalse("".equals(druidResult));
        assertTrue(areEqualAsSets(mySqlResult, druidResult));
    }

    @Test public void testDruidMySqlEquivalence27() {
        QueryController controller = new QueryController();
        String mySqlQuery = generateMySqlQueryFromTemplate(Q27);
        String druidQuery = generateDruidQueryFromTemplate(Q27);
        String mySqlResult = controller.query(mySqlQuery).getContent();
        String druidResult = controller.query(druidQuery).getContent();
        assertNotNull(mySqlResult);
        assertNotNull(druidResult);
        assertFalse("".equals(mySqlResult));
        assertFalse("".equals(druidResult));
        assertEquals(mySqlResult, druidResult);
    }

    @Test public void testDruidMySqlEquivalence28() {
        QueryController controller = new QueryController();
        String mySqlQuery = generateMySqlQueryFromTemplate(Q28);
        String druidQuery = generateDruidQueryFromTemplate(Q28);
        String mySqlResult = controller.query(mySqlQuery).getContent();
        String druidResult = controller.query(druidQuery).getContent();
        assertNotNull(mySqlResult);
        assertNotNull(druidResult);
        assertFalse("".equals(mySqlResult));
        assertFalse("".equals(druidResult));
        assertEquals(mySqlResult, druidResult);
    }

    @Test public void testDruidMySqlEquivalence29() {
        QueryController controller = new QueryController();
        String mySqlQuery = generateMySqlQueryFromTemplate(Q29);
        String druidQuery = generateDruidQueryFromTemplate(Q29);
        String mySqlResult = controller.query(mySqlQuery).getContent();
        String druidResult = controller.query(druidQuery).getContent();
        assertNotNull(mySqlResult);
        assertNotNull(druidResult);
        assertFalse("".equals(mySqlResult));
        assertFalse("".equals(druidResult));
        assertEquals(mySqlResult, druidResult);
    }

    @Test public void testDruidMySqlEquivalence30() {
        QueryController controller = new QueryController();
        String mySqlQuery = generateMySqlQueryFromTemplate(Q30);
        String druidQuery = generateDruidQueryFromTemplate(Q30);
        String mySqlResult = controller.query(mySqlQuery).getContent();
        String druidResult = controller.query(druidQuery).getContent();
        assertNotNull(mySqlResult);
        assertNotNull(druidResult);
        assertFalse("".equals(mySqlResult));
        assertFalse("".equals(druidResult));
        assertEquals(mySqlResult, druidResult);
    }

    @Test public void testDruidMySqlEquivalence31() {
        QueryController controller = new QueryController();
        String mySqlQuery = generateMySqlQueryFromTemplate(Q31);
        String druidQuery = generateDruidQueryFromTemplate(Q31);
        String mySqlResult = controller.query(mySqlQuery).getContent();
        String druidResult = controller.query(druidQuery).getContent();
        assertNotNull(mySqlResult);
        assertNotNull(druidResult);
        assertFalse("".equals(mySqlResult));
        assertFalse("".equals(druidResult));
        assertEquals(mySqlResult, druidResult);
    }

    @Test public void testDruidMySqlEquivalence32() {
        QueryController controller = new QueryController();
        String mySqlQuery = generateMySqlQueryFromTemplate(Q32);
        String druidQuery = generateDruidQueryFromTemplate(Q32);
        String mySqlResult = controller.query(mySqlQuery).getContent();
        String druidResult = controller.query(druidQuery).getContent();
        assertNotNull(mySqlResult);
        assertNotNull(druidResult);
        assertFalse("".equals(mySqlResult));
        assertFalse("".equals(druidResult));
        assertEquals(mySqlResult, druidResult);
    }

    @Test public void testDruidMySqlEquivalence33() {
        QueryController controller = new QueryController();
        String mySqlQuery = generateMySqlQueryFromTemplate(Q33);
        String druidQuery = generateDruidQueryFromTemplate(Q33);
        String mySqlResult = controller.query(mySqlQuery).getContent();
        String druidResult = controller.query(druidQuery).getContent();
        assertNotNull(mySqlResult);
        assertNotNull(druidResult);
        assertFalse("".equals(mySqlResult));
        assertFalse("".equals(druidResult));
        assertEquals(mySqlResult, druidResult);
    }

    @Test public void testDruidMySqlEquivalence34() {
        QueryController controller = new QueryController();
        String mySqlQuery = generateMySqlQueryFromTemplate(Q34);
        String druidQuery = generateDruidQueryFromTemplate(Q34);
        String mySqlResult = controller.query(mySqlQuery).getContent();
        String druidResult = controller.query(druidQuery).getContent();
        assertNotNull(mySqlResult);
        assertNotNull(druidResult);
        assertFalse("".equals(mySqlResult));
        assertFalse("".equals(druidResult));
        assertEquals(mySqlResult, druidResult);
    }

    private final int MAX_SIGNIFICANT_DIGITS = 4;

    private boolean areEqualAsSets(String content1, String content2) {
        Set<String> set1 = rowsToSet(content1);
        Set<String> set2 = rowsToSet(content2);
        return set1.equals(set2);
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

                // convert time to Unix epoch time
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
