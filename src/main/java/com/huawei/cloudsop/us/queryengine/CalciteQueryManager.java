package com.huawei.cloudsop.us.queryengine;

import org.apache.calcite.avatica.util.DateTimeUtils;
import org.apache.calcite.jdbc.CalciteConnection;
import org.apache.calcite.config.CalciteConnectionProperty;
import org.apache.calcite.util.Sources;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

public class CalciteQueryManager {
    /** URL of the "druid-foodmart" model. */
    public static final URL FOODMART =
            CalciteQueryManager.class.getResource("/druid-foodmart-model.json");

    public static final URL MYSQL_FOODMART =
        CalciteQueryManager.class.getResource("/mysql-foodmart-model.json");

    /** URL of the "druid-wiki" model
     * and the "wikiticker" data set. */
    public static final URL WIKI =
            CalciteQueryManager.class.getResource("/druid-wiki-model.json");

    /** URL of the "druid-wiki-no-columns" model
     * and the "wikiticker" data set. */
    public static final URL WIKI_AUTO =
            CalciteQueryManager.class.getResource("/druid-wiki-no-columns-model.json");

    /** URL of the "druid-wiki-no-tables" model
     * and the "wikiticker" data set. */
    public static final URL WIKI_AUTO2 =
            CalciteQueryManager.class.getResource("/druid-wiki-no-tables-model.json");

    public String query(URL model, String sql) {
        ConnectionFactory factory =
                ConnectionFactoryImpl.EMPTY_CONNECTION_FACTORY
                        .with(CalciteConnectionProperty.MODEL,
                Sources.of(model).file().getAbsolutePath());

        try (Connection conn = factory.createConnection()) {
            CalciteConnection calciteConnection =
                    conn.unwrap(CalciteConnection.class);
            final Properties properties = calciteConnection.getProperties();

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


