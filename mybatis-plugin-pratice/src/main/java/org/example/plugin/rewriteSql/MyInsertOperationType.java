package org.example.plugin.rewriteSql;

import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.insert.Insert;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;

import java.util.List;

public class MyInsertOperationType extends MyDefaultOperationType {
    @Override
    public String handle(MappedStatement mappedStatement, List<ParameterMapping> parameterMappings, MapperMethod.ParamMap parameterObject, String sql) {
        try {
            Statement statement = CCJSqlParserUtil.parse(sql);
            Insert insert = (Insert) statement;
            Table table = insert.getTable();
            ExpressionList<Column> columns = insert.getColumns();

            List<String> needEncs = encList(table.getName(), columns);
            if (needEncs.isEmpty()) {
                return insert.toString();
            }

            for (String col : needEncs) {
                String newCol = col + "_enc";
                Column cipherText = new Column(newCol);
                //在insert里添加一个字段
                columns.add(cipherText);

                // 将新增的字段添加到mybatis框架的字段集合中
                ParameterMapping parameterMapping = new ParameterMapping.Builder(mappedStatement.getConfiguration(), newCol, String.class).build();
                if (isNotIn(parameterMapping, parameterMappings)) {
                    parameterMappings.add(parameterMapping);
                }
                //添加参数值
                parameterObject.put(newCol, getColumnValue(parameterObject, parameterMappings, columns, col) + "123");
            }

            insert.setColumns(columns);
            return insert.toString();
        } catch (Exception e) {
            throw new RuntimeException("解析sql异常", e);
        }
    }
}