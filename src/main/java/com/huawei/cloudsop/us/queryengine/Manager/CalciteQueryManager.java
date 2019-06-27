package com.huawei.cloudsop.us.queryengine.Manager;

import com.huawei.cloudsop.us.queryengine.Connection.ConnectionFactory;
import com.huawei.cloudsop.us.queryengine.Connection.ConnectionFactoryImpl;
import com.huawei.cloudsop.us.queryengine.Connection.ResultSetFormatter;
import org.apache.calcite.avatica.util.DateTimeUtils;
import org.apache.calcite.jdbc.CalciteConnection;
import org.apache.calcite.config.CalciteConnectionProperty;
import org.apache.calcite.util.Sources;

import java.net.URL;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.Properties;

public class CalciteQueryManager {
    public static final URL DRUID_MYSQL_MODEL =
            CalciteQueryManager.class.getResource("/druid-mysql-model.json");

    public String query(URL model, String sql) {
        ConnectionFactory factory =
                ConnectionFactoryImpl.EMPTY_CONNECTION_FACTORY
                        .with(CalciteConnectionProperty.MODEL,
                Sources.of(model).file().getAbsolutePath());

        try (Connection conn = factory.createConnection()) {
            CalciteConnection calciteConnection =
                    conn.unwrap(CalciteConnection.class);
            final Properties properties = calciteConnection.getProperties();

            DatabaseMetaData metaData = conn.getMetaData();
            ResultSet catalogs = metaData.getCatalogs();
            ResultSetMetaData catalogsmetaData = catalogs.getMetaData();
            int colCount = catalogsmetaData.getColumnCount();
            String colLabel = catalogsmetaData.getColumnLabel(1);
            String catalogName = catalogsmetaData.getCatalogName(1);
            String colClassName = catalogsmetaData.getColumnClassName(1);
            String tableName = catalogsmetaData.getTableName(1);
            //catalogsmetaData.


//            boolean catalogsNext = catalogs.next();
//            if (catalogsNext) {
//                catalogs.
//            }


            if (!properties
                    .containsKey(CalciteConnectionProperty.TIME_ZONE.camelName())) {
                // Do not override id some test has already set this property.
                properties.setProperty(
                        CalciteConnectionProperty.TIME_ZONE.camelName(),
                        DateTimeUtils.UTC_ZONE.getID());
            }

            try (Statement statement = calciteConnection.createStatement();
                 ResultSet res =  statement.executeQuery(sql)) {
                return (new ResultSetFormatter().resultSet(res)).string();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }


}


