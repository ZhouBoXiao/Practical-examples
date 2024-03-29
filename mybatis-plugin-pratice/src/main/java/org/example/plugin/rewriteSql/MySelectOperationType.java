package org.example.plugin.rewriteSql;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.FromItem;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;

import java.util.List;

public class MySelectOperationType extends MyDefaultOperationType {
    @Override
    public String handle(MappedStatement mystatement, List<ParameterMapping> parameterMappings, MapperMethod.ParamMap parameterObject, String sql) {
        try {
            Statement statement = CCJSqlParserUtil.parse(sql);
            Select select = (Select) statement;
            PlainSelect plain = select.getPlainSelect();

            FromItem fromItem = plain.getFromItem();
            String tableName = fromItem.toString();
            Table table = new Table();
            table.setAlias(fromItem.getAlias());
            table.setName(tableName);

            Expression where = plain.getWhere();
            EqualsTo status = new EqualsTo();
            status.setLeftExpression(new Column(table, "status"));
            StringValue stringValue = new StringValue("0");
            status.setRightExpression(stringValue);
            if (where != null) {
                AndExpression lastwhere = new AndExpression(where, status);
                plain.setWhere(lastwhere);
            } else {
                plain.setWhere(status);
            }
            return select.toString();
        } catch (Exception e) {
            throw new RuntimeException("解析sql异常", e);
        }
    }
}