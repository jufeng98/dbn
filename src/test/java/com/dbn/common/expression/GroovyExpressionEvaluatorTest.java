package com.dbn.common.expression;

import lombok.SneakyThrows;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class GroovyExpressionEvaluatorTest {
    GroovyExpressionEvaluator expressionEvaluator = new GroovyExpressionEvaluator();

    @Test
    public void evaluateBooleanExpression() throws Exception{
        boolean result = expressionEvaluator.evaluateBooleanExpression("COLUMN_NAME = 'TEST1'", map("COLUMN_NAME", "TEST1"));
        Assert.assertTrue(result);

        result = expressionEvaluator.evaluateBooleanExpression("COLUMN_NAME != 'TEST1'", map("COLUMN_NAME", "TEST2"));
        Assert.assertTrue(result);

        result = expressionEvaluator.evaluateBooleanExpression("COLUMN_NAME = 'TEST1'", map("COLUMN_NAME", "TEST2"));
        Assert.assertFalse(result);

        result = expressionEvaluator.evaluateBooleanExpression("COLUMN_NAME IN ('TEST1',  'TEST2', 'TEST3')", map("COLUMN_NAME", "TEST1"));
        Assert.assertTrue(result);

        result = expressionEvaluator.evaluateBooleanExpression("COLUMN_NAME IN ('TEST1',  'TEST2', 'TEST3')", map("COLUMN_NAME", "TEST4"));
        Assert.assertFalse(result);


        result = expressionEvaluator.evaluateBooleanExpression("COLUMN_NAME    IN ('TEST1',  'TEST2', 'TEST3') AND COLUMN_SIZE IN (1, 2, 3)", map("COLUMN_NAME", "TEST3", "COLUMN_SIZE", 2));
        Assert.assertTrue(result);

        result = expressionEvaluator.evaluateBooleanExpression("COLUMN_NAME IN   ('TEST1',  'TEST2', 'TEST3') AND COLUMN_SIZE IN (1, 2, 3)", map("COLUMN_NAME", "TEST4", "COLUMN_SIZE", 4));
        Assert.assertFalse(result);

        result = expressionEvaluator.evaluateBooleanExpression("COLUMN_SIZE >= 10", map("COLUMN_SIZE", 10));
        Assert.assertTrue(result);

        result = expressionEvaluator.evaluateBooleanExpression("COLUMN_SIZE >= 10", map("COLUMN_SIZE", 11));
        Assert.assertTrue(result);

        result = expressionEvaluator.evaluateBooleanExpression("COLUMN_SIZE >= 10", map("COLUMN_SIZE", 9));
        Assert.assertFalse(result);


        result = expressionEvaluator.evaluateBooleanExpression("COLUMN_NAME LIKE 'TEST%'", map("COLUMN_NAME", "TEST1234"));
        Assert.assertTrue(result);

        result = expressionEvaluator.evaluateBooleanExpression("COLUMN_NAME LIKE 'TEST%'", map("COLUMN_NAME", "SOME_TEST_1234"));
        Assert.assertFalse(result);

        result = expressionEvaluator.evaluateBooleanExpression("COLUMN_NAME LIKE '%TEST%'", map("COLUMN_NAME", "SOME_TEST_1234"));
        Assert.assertTrue(result);

    }

    @Test
    public void evaluateExpression() {
    }

    @Test
    public void fromSql_AND_OR() {
        testSqlToGroovy(
                "(COLUMN_NAME = 'COLUMN_AND_OR_TRAP' or COLUMN_NAME = 'DATE_COLUMN') AND (COLUMN_TYPE = 'VARCHAR2' OR COLUMN_TYPE = 'DATE')",
                "(COLUMN_NAME == 'COLUMN_AND_OR_TRAP' || COLUMN_NAME == 'DATE_COLUMN') && (COLUMN_TYPE == 'VARCHAR2' || COLUMN_TYPE == 'DATE')");
    }

    @Test
    public void fromSql_OPERATORS() {
        testSqlToGroovy(
                "(COLUMN_NAME = 'COLUMN_AND_OR_TRAP' or COLUMN_NAME != 'DATE_COLUMN') AND (COLUMN_SIZE >= 10 OR COLUMN_SIZE != 3 OR COLUMN_SIZE<=0 OR COLUMN_SIZE==34)",
                "(COLUMN_NAME == 'COLUMN_AND_OR_TRAP' || COLUMN_NAME != 'DATE_COLUMN') && (COLUMN_SIZE >= 10 || COLUMN_SIZE != 3 || COLUMN_SIZE<=0 || COLUMN_SIZE==34)");
    }


    @Test
    public void fromSql_IN1() {
        testSqlToGroovy(
                "COLUMN_NAME IN ('TEST1', 'TEST2', 'TEST3')",
                "['TEST1', 'TEST2', 'TEST3'].contains(COLUMN_NAME)");
    }

    @Test
    public void fromSql_IN2() {
        testSqlToGroovy(
                "COLUMN_SIZE IN (1, 4, 5)",
                "[1, 4, 5].contains(COLUMN_SIZE)");
    }

    @Test
    public void fromSql_IN3() {
        testSqlToGroovy(
                "COLUMN_NAME IN ('TEST1', 'TEST2', 'TEST3') AND COLUMN_SIZE IN (1, 4, 5)",
                "['TEST1', 'TEST2', 'TEST3'].contains(COLUMN_NAME) && [1, 4, 5].contains(COLUMN_SIZE)");
    }


    @Test
    public void fromSql_LIKE1() {
        testSqlToGroovy(
                "COLUMN_NAME LIKE 'TEST1%'",
                "COLUMN_NAME.toLowerCase().indexOf('TEST1'.toLowerCase()) == 0");
    }

    @Test
    public void fromSql_LIKE2() {
        testSqlToGroovy(
                "COLUMN_NAME LIKE '%TEST1%'",
                "COLUMN_NAME.toLowerCase().indexOf('TEST1'.toLowerCase()) > 0");
    }

    @Test
    public void fromSql_LIKE3() {
        testSqlToGroovy(
                "COLUMN_SIZE >= 4 AND (COLUMN_NAME LIKE '%TEST1%' OR COLUMN_NAME LIKE    'TEST2%') AND COLUMN_TYPE = 'VARCHAR'",
                "COLUMN_SIZE >= 4 && (COLUMN_NAME.toLowerCase().indexOf('TEST1'.toLowerCase()) > 0 || COLUMN_NAME.toLowerCase().indexOf('TEST2'.toLowerCase()) == 0) && COLUMN_TYPE == 'VARCHAR'");
    }


    @SneakyThrows
    private void testSqlToGroovy(String in, String out){
        String groovyExpression = SqlToGroovyExpressionConverter.sqlToGroovy(in);

        System.out.println();
        System.out.println(in);
        System.out.println(groovyExpression);
        expressionEvaluator.evaluateBooleanExpression(in, map("COLUMN_NAME", "TEST", "COLUMN_TYPE", "VARCHAR", "COLUMN_SIZE", 3));

        Assert.assertEquals(out, groovyExpression);
    }
    
    private Map<String, Object> map(Object ... keyValues) {
        Map<String, Object> map = new HashMap<>();
        for (int i = 0, l = keyValues.length; i < l / 2; i++) {
            String key = (String) keyValues[i * 2];
            Object value = keyValues[i * 2 +1 ];
            map.put(key, value);
        }
        return map;
    }
}