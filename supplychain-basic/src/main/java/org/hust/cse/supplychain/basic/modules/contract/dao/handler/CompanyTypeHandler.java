
/**
 * @author: Lu Miao
 * @date: 2025-03-05
 * @description: 公司类型处理器
 */
package org.hust.cse.supplychain.basic.modules.contract.dao.handler;

import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import org.hust.cse.supplychain.basic.modules.contract.po.entity.Company;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


@MappedTypes(Company.class)
@MappedJdbcTypes(JdbcType.VARCHAR)
public class CompanyTypeHandler extends BaseTypeHandler<Company> {

    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, Company Company, JdbcType jdbcType) throws SQLException {
        JSONObject CompanyJson = (JSONObject)JSONObject.toJSON(Company);
        preparedStatement.setString(i, CompanyJson.toJSONString());
    }

    @Override
    public Company getNullableResult(ResultSet resultSet, String columnName) throws SQLException {
        return JSONObject.parseObject(resultSet.getString(columnName), Company.class);
    }

    @Override
    public Company getNullableResult(ResultSet resultSet, int columnIndex) throws SQLException {
        return JSONObject.parseObject(resultSet.getString(columnIndex), Company.class);
    }

    @Override
    public Company getNullableResult(CallableStatement callableStatement, int columnIndex) throws SQLException {
        return JSONObject.parseObject(callableStatement.getString(columnIndex), Company.class);
    }
}
