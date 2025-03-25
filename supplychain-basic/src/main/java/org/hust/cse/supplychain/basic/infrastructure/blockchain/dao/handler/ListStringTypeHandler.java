package org.hust.cse.supplychain.basic.infrastructure.blockchain.dao.handler;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@MappedTypes(List.class)
@MappedJdbcTypes(JdbcType.VARCHAR)
public class ListStringTypeHandler extends BaseTypeHandler<List<String>> {
    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, List<String> str, JdbcType jdbcType) throws SQLException {
        JSONArray strJson = JSONArray.parseArray(JSONObject.toJSONString(str));
        preparedStatement.setString(i, strJson.toJSONString());
    }

    @Override
    public List<String> getNullableResult(ResultSet resultSet, String columnName) throws SQLException {
        return JSONObject.parseArray(resultSet.getString(columnName),String.class);
    }

    @Override
    public List<String> getNullableResult(ResultSet resultSet, int columnIndex) throws SQLException {
        return JSONObject.parseArray(resultSet.getString(columnIndex),String.class);
    }

    @Override
    public List<String> getNullableResult(CallableStatement callableStatement, int columnIndex) throws SQLException {
        return JSONObject.parseArray(callableStatement.getString(columnIndex),String.class);
    }
}
