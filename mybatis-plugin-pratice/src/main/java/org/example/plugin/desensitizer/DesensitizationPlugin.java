package org.example.plugin.desensitizer;


import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.sql.Statement;
import java.util.List;
import java.util.stream.Stream;

@Intercepts(@Signature(type = ResultSetHandler.class, method = "handleResultSets", args = Statement.class))
@Component
public class DesensitizationPlugin implements Interceptor {
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        List<Object> records = (List<Object>) invocation.proceed();
        records.forEach(this::desensitize);
        return records;
    }

    private void desensitize(Object obj) {
        Class<?> objClass = obj.getClass();
        MetaObject metaObject = SystemMetaObject.forObject(objClass);
        Stream.of(objClass.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Desensitization.class))
                .forEach(field -> doDesensitize(metaObject, field));
    }

    private void doDesensitize(MetaObject metaObject, Field field) {
        String name = field.getName();
        Object value = metaObject.getValue(name);
        if (String.class == metaObject.getGetterType(name) && value != null) {
            Desensitization desensitization = field.getAnnotation(Desensitization.class);
            DesensitizationStrategy strategy = desensitization.strategy();
            String newValue = strategy.getDesensitizer().apply((String) value);
            metaObject.setValue(name, newValue);
        }
    }
}
