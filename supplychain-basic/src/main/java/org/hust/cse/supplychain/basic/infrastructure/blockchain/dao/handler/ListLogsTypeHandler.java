package org.hust.cse.supplychain.basic.infrastructure.blockchain.dao.handler;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import org.hust.cse.supplychain.basic.infrastructure.blockchain.po.Log;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@MappedTypes(List.class)
@MappedJdbcTypes(JdbcType.VARCHAR)
public class ListLogsTypeHandler extends BaseTypeHandler<List<Log>> {
    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, List<Log> logs, JdbcType jdbcType) throws SQLException {
        JSONArray logsJson = JSONArray.parseArray(JSONObject.toJSONString(logs));
        preparedStatement.setString(i, logsJson.toJSONString());
    }

    @Override
    public List<Log> getNullableResult(ResultSet resultSet, String columnName) throws SQLException {
        return JSONObject.parseArray(resultSet.getString(columnName),Log.class);
    }

    @Override
    public List<Log> getNullableResult(ResultSet resultSet, int columnIndex) throws SQLException {
        return JSONObject.parseArray(resultSet.getString(columnIndex),Log.class);
    }

    @Override
    public List<Log> getNullableResult(CallableStatement callableStatement, int columnIndex) throws SQLException {
        return JSONObject.parseArray(callableStatement.getString(columnIndex),Log.class);
    }
}
