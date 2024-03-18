package org.example.plugin.rewriteSql;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.*;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Properties;

@Intercepts({
        @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class}),
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class})
})
@Slf4j
public class RewriteInterceptor implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object[] args = invocation.getArgs();
        MappedStatement statement = (MappedStatement) args[0];

        Object parameterObject = args[1];
        BoundSql boundSql = statement.getBoundSql(parameterObject);
        String sql = boundSql.getSql();
        if (sql == null || sql.isEmpty()) {
            return invocation.proceed();
        }

        // 重写sql
        resetSql2Invocation(invocation, sql);

        return invocation.proceed();
    }

    private void resetSql2Invocation(Invocation invocation, String sql) {
        final Object[] args = invocation.getArgs();
        MappedStatement statement = (MappedStatement) args[0];
        MapperMethod.ParamMap parameterObject = (MapperMethod.ParamMap) args[1];
        final BoundSql boundSql = statement.getBoundSql(parameterObject);
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        //根据不同的sql类型重新构建新的sql语句
        String newsql = "";
        MyOperationType myOperationType = null;
        SqlCommandType sqlCommandType = statement.getSqlCommandType();
        switch (sqlCommandType) {
            case INSERT:
                myOperationType = new MyInsertOperationType();
                newsql = myOperationType.handle(statement, parameterMappings, parameterObject, sql);
                break;
            case UPDATE:
                myOperationType = new MyUpdateOperationType();
                newsql = myOperationType.handle(statement, parameterMappings, parameterObject, sql);
                break;
            case DELETE:
                myOperationType = new MyDeleteOperationType();
                newsql = myOperationType.handle(statement, parameterMappings, parameterObject, sql);
                break;
            case SELECT:
                myOperationType = new MySelectOperationType();
                newsql = myOperationType.handle(statement, parameterMappings, parameterObject, sql);
                break;
            default:
                break;
        }

        // 重新new一个查询语句对像
        BoundSql newBoundSql = new BoundSql(statement.getConfiguration(), newsql, parameterMappings,
                parameterObject);
        // 把新的查询放到statement里
        MappedStatement newStatement = copyFromMappedStatement(statement, new BoundSqlSqlSource(newBoundSql));

        // 重新设置新的参数
        args[0] = newStatement;
        System.out.println("sql语句：" + newsql);
    }

    //构造新的statement
    private MappedStatement copyFromMappedStatement(MappedStatement ms, SqlSource newSqlSource) {
        MappedStatement.Builder builder = new MappedStatement.Builder(ms.getConfiguration(), ms.getId(), newSqlSource,
                ms.getSqlCommandType());
        builder.resource(ms.getResource());
        builder.fetchSize(ms.getFetchSize());
        builder.statementType(ms.getStatementType());
        builder.keyGenerator(ms.getKeyGenerator());
        if (ms.getKeyProperties() != null && ms.getKeyProperties().length != 0) {
            StringBuilder keyProperties = new StringBuilder();
            for (String keyProperty : ms.getKeyProperties()) {
                keyProperties.append(keyProperty).append(",");
            }
            keyProperties.delete(keyProperties.length() - 1, keyProperties.length());
            builder.keyProperty(keyProperties.toString());
        }
        builder.timeout(ms.getTimeout());
        builder.parameterMap(ms.getParameterMap());
        builder.resultMaps(ms.getResultMaps());
        builder.resultSetType(ms.getResultSetType());
        builder.cache(ms.getCache());
        builder.flushCacheRequired(ms.isFlushCacheRequired());
        builder.useCache(ms.isUseCache());

        return builder.build();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
    }

    static class BoundSqlSqlSource implements SqlSource {
        private final BoundSql boundSql;

        public BoundSqlSqlSource(BoundSql boundSql) {
            this.boundSql = boundSql;
        }

        @Override
        public BoundSql getBoundSql(Object parameterObject) {
            return boundSql;
        }
    }
}
