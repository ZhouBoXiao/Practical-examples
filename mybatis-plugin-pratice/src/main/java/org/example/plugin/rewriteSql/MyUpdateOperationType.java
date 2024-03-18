package org.example.plugin.rewriteSql;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.JdbcParameter;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.update.Update;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;

import java.util.List;

public class MyUpdateOperationType extends MyDefaultOperationType {
    @Override
    public String handle(MappedStatement mappedStatement, List<ParameterMapping> parameterMappings, MapperMethod.ParamMap parameterObject, String sql) {
        try {
            Statement statement = CCJSqlParserUtil.parse(sql);
            Update update = (Update) statement;
            Table table = update.getTable();
            List<Column> columns = update.getColumns();

            List<String> needEncs = encList(table.getName(), columns);
            if (needEncs.isEmpty()) {
                return update.toString();
            }
            List<Expression> expressions = update.getExpressions();

            for (String col : needEncs) {
                String newCol = col + "_enc";
                Column cipherText = new Column(table, newCol);

                Expression expression = new JdbcParameter();
                expressions.add(expression);

                //添加参数key
                ParameterMapping parameterMapping = new ParameterMapping.Builder(mappedStatement.getConfiguration(), newCol, String.class).build();
                if (isNotIn(parameterMapping, parameterMappings)) {
                    int index = parameterMappings.size() - (parameterMappings.size() - columns.size());
                    parameterMappings.add(index, parameterMapping);
                }
                columns.add(cipherText);

                //添加参数值
                parameterObject.put(newCol, getColumnValue(parameterObject, parameterMappings, columns, col) + "123");
            }

            Expression where = update.getWhere();
            EqualsTo status = new EqualsTo();
            status.setLeftExpression(new Column(table, "status"));
            StringValue stringValue = new StringValue("0");
            status.setRightExpression(stringValue);
            if (where != null) {
                AndExpression lastWhere = new AndExpression(where, status);
                update.setWhere(lastWhere);
            } else {
                update.setWhere(status);
            }

            update.setExpressions(expressions);
            return update.toString();
        } catch (Exception e) {
            throw new RuntimeException("解析sql异常", e);
        }
    }
}
