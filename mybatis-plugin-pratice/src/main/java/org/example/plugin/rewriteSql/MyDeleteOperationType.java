package org.example.plugin.rewriteSql;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.delete.Delete;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;

import java.util.List;

public class MyDeleteOperationType extends MyDefaultOperationType {
    @Override
    public String handle(MappedStatement mappedStatement, List<ParameterMapping> parameterMappings, MapperMethod.ParamMap parameterObject, String sql) {
        try {
            Statement statement = CCJSqlParserUtil.parse(sql);
            Delete delete = (Delete) statement;
            Table table = delete.getTable();
            Expression where = delete.getWhere();
            EqualsTo equalsTo = new EqualsTo();
            equalsTo.setLeftExpression(new Column(table, "status"));
            equalsTo.setRightExpression(new StringValue("1"));
            if (where != null) {
                delete.setWhere(new AndExpression(where, equalsTo));
            } else {
                delete.setWhere(equalsTo);
            }

            return delete.toString();
        } catch (Exception e) {
            throw new RuntimeException("解析sql异常", e);
        }
    }
}