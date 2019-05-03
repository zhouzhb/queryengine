package com.huawei.cloudsop.us.queryengine;

public class QueryResult {
    private final long id;
    private final String content;

    public QueryResult(long id, String content) {
        this.id = id;
        this.content = content;
    }

    public long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }
}
