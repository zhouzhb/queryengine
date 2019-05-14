package com.huawei.cloudsop.us.queryengine.Connection;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.apache.calcite.avatica.ConnectionProperty;
import org.apache.calcite.runtime.FlatLists;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

public class ConnectionFactoryImpl extends ConnectionFactory {
    public static final ConnectionFactory EMPTY_CONNECTION_FACTORY =
            new ConnectionFactoryImpl(ImmutableMap.of(), ImmutableList.of());

    private final ImmutableMap<String, String> map;
    private final ImmutableList<ConnectionPostProcessor> postProcessors;

    private ConnectionFactoryImpl(ImmutableMap<String, String> map,
                                 ImmutableList<ConnectionPostProcessor> postProcessors) {
        this.map = Objects.requireNonNull(map);
        this.postProcessors = Objects.requireNonNull(postProcessors);
    }

    @Override public boolean equals(Object obj) {
        return this == obj
                || obj.getClass() == ConnectionFactoryImpl.class
                && ((ConnectionFactoryImpl) obj).map.equals(map)
                && ((ConnectionFactoryImpl) obj).postProcessors.equals(postProcessors);
    }

    @Override public int hashCode() {
        return Objects.hash(map, postProcessors);
    }

    public Connection createConnection() throws SQLException {
        final Properties info = new Properties();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            info.setProperty(entry.getKey(), entry.getValue());
        }
        Connection connection =
                DriverManager.getConnection("jdbc:calcite:", info);
        for (ConnectionPostProcessor postProcessor : postProcessors) {
            connection = postProcessor.apply(connection);
        }
        return connection;
    }

    public ConnectionFactory with(String property, Object value) {
        return new ConnectionFactoryImpl(
                FlatLists.append(this.map, property, value.toString()),
                postProcessors);
    }

    public ConnectionFactory with(ConnectionProperty property, Object value) {
        if (!property.type().valid(value, property.valueClass())) {
            throw new IllegalArgumentException();
        }
        return with(property.camelName(), value.toString());
    }

    public ConnectionFactory with(
            ConnectionPostProcessor postProcessor) {
        ImmutableList.Builder<ConnectionPostProcessor> builder =
                ImmutableList.builder();
        builder.addAll(postProcessors);
        builder.add(postProcessor);
        return new ConnectionFactoryImpl(map, builder.build());
    }
}