package com.example.aviator;

import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.Expression;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TestAviator {
    @Test
    public void baseExpression() {
        Long r = (Long) AviatorEvaluator.execute("2 * (3 + 5)");
        Expression expression = AviatorEvaluator.compile("2 * (3 + 5)");
        r = (Long) expression.execute();
        String r1 = (String) AviatorEvaluator.execute("'hello' + ' world'");
        Assertions.assertEquals("hello world", r1);
        Boolean r2 = (Boolean) AviatorEvaluator.execute("100 > 80 && 30 < 40");
        Long r3 = (Long) AviatorEvaluator.execute("100 > 80 ? 30 : 40");
        Assertions.assertTrue((Boolean) AviatorEvaluator.execute("'hello' =~ /[\\w]+/"));
    }

    @Test
    public void expressionVar() {
        Long a = 12L;
        Map<String, Object> env = new HashMap<>();
        env.put("a", a);
        Assertions.assertTrue((Boolean) AviatorEvaluator.execute("a > 10", env));

        // 返回4
        Long r1 = (Long) AviatorEvaluator.execute("math.round(4.3)");

        // 返回5
        Long r2 = (Long) AviatorEvaluator.execute("string.length('hello')");

        // ArrayList：[1,2,3]
        Object r3 = AviatorEvaluator.execute("seq.list(1,2,3)");
    }

    @Test
    public void testFunction() {
        // 注册
        AviatorEvaluator.addFunction(new AddFunction());

        // 使用
        long sum = (Long) AviatorEvaluator.getInstance().execute("add(3,4)");
        Assertions.assertEquals(7, sum);
    }

    @Test
    public void aviatorScript() throws IOException {
        // 返回1
        Object r = AviatorEvaluator.execute("if (true) { return 1; } else { return 2; }");
        Map<String, Object> env = new HashMap<>();
        env.put("a", 30);
        Expression exp = AviatorEvaluator.getInstance().compileScript("./hello.av", true);
        Object result = exp.execute(env);
        Assertions.assertEquals(10L, result);
    }
}
