/**
 * @author: Lu Miao
 * @date: 2025-03-19
 * @description: 产品列表类型处理器
 */
package org.hust.cse.supplychain.basic.modules.contract.dao.handler;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import org.hust.cse.supplychain.basic.modules.contract.po.entity.Product;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;


@MappedTypes(List.class)
@MappedJdbcTypes(JdbcType.VARCHAR)
public class ListProductsTypeHandler extends BaseTypeHandler<List<Product>> {

    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, List<Product> products, JdbcType jdbcType) throws SQLException {
        JSONArray productsJson = JSONArray.parseArray(JSONObject.toJSONString(products));
        preparedStatement.setString(i, productsJson.toJSONString());
    }

    @Override
    public List<Product> getNullableResult(ResultSet resultSet, String columnName) throws SQLException {
        return JSONObject.parseArray(resultSet.getString(columnName),Product.class);
    }

    @Override
    public List<Product> getNullableResult(ResultSet resultSet, int columnIndex) throws SQLException {
        return JSONObject.parseArray(resultSet.getString(columnIndex),Product.class);
    }

    @Override
    public List<Product> getNullableResult(CallableStatement callableStatement, int columnIndex) throws SQLException {
        return JSONObject.parseArray(callableStatement.getString(columnIndex),Product.class);
    }
}
