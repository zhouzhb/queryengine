package com.huawei.cloudsop.us.queryengine.Connection;

import org.apache.calcite.avatica.ConnectionProperty;

import java.sql.Connection;
import java.sql.SQLException;

public abstract class ConnectionFactory {
    public abstract Connection createConnection() throws SQLException;

    public ConnectionFactory with(String property, Object value) {
        throw new UnsupportedOperationException();
    }

    public ConnectionFactory with(ConnectionProperty property, Object value) {
        throw new UnsupportedOperationException();
    }

    public ConnectionFactory with(ConnectionPostProcessor postProcessor) {
        throw new UnsupportedOperationException();
    }
}