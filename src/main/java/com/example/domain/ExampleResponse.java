package com.example.domain;

/**
 * Created by phoeniix on 11/12/16.
 */
public class ExampleResponse {
    private final long id;
    private final String content;

    public ExampleResponse(long id, String content) {
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
