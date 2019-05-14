package com.huawei.cloudsop.us.queryengine.Connection;

import java.sql.Connection;
import java.sql.SQLException;

public interface ConnectionPostProcessor {
    Connection apply(Connection connection) throws SQLException;
}