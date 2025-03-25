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
public class ListRecordsTypeHandler extends BaseTypeHandler<List<Record>>{
    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, List<Record> records, JdbcType jdbcType) throws SQLException {
        JSONArray recordsJson = JSONArray.parseArray(JSONObject.toJSONString(records));
        preparedStatement.setString(i, recordsJson.toJSONString());
    }

    @Override
    public List<Record> getNullableResult(ResultSet resultSet, String columnName) throws SQLException {
        return JSONObject.parseArray(resultSet.getString(columnName),Record.class);
    }

    @Override
    public List<Record> getNullableResult(ResultSet resultSet, int columnIndex) throws SQLException {
        return JSONObject.parseArray(resultSet.getString(columnIndex),Record.class);
    }

    @Override
    public List<Record> getNullableResult(CallableStatement callableStatement, int columnIndex) throws SQLException {
        return JSONObject.parseArray(callableStatement.getString(columnIndex),Record.class);
    }

}