package com.handson.docker.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.okhttp.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ElasticsearchUtil {
    @Value("${elastic.base.url}")
    private String baseUrl;

    @Autowired
    ObjectMapper om;
    private static final Logger LOGGER = LoggerFactory.getLogger(ElasticsearchUtil.class);

    public boolean crateIndex(String indexName) {
        try {
            OkHttpClient client = new OkHttpClient();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, "{}");
            Request request = new Request.Builder()
                    .url(baseUrl + indexName)
                    .method("PUT", body)
                    .addHeader("Content-Type", "application/json")
                    .build();
            Response response = client.newCall(request).execute();
            return (response.code()<300);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteIndex(String indexName) {
        try {
            OkHttpClient client = new OkHttpClient();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, "{}");
            Request request = new Request.Builder()
                    .url(baseUrl + indexName)
                    .method("DELETE", body)
                    .addHeader("Content-Type", "application/json")
                    .build();
            Response response = client.newCall(request).execute();
            return (response.code()<300);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}