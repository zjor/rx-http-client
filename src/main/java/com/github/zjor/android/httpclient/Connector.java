package com.github.zjor.android.httpclient;

import okhttp3.OkHttpClient;
import okhttp3.Request;

import java.lang.reflect.Type;

public class Connector {

    private final OkHttpClient client;

    public Connector(OkHttpClient client) {
        this.client = client;
    }

    public <T> CallWrapper<T> request(Request request, BodyParser<T> parser) {
        System.out.println("Requesting: " + request.url());
        return new CallWrapper<>(client.newCall(request), parser);
    }

    public <T> CallWrapper<T> get(String url, Class<T> responseClass) {
        return request(new Request.Builder().url(url).get().build(), new JSONBodyParser<>(responseClass));
    }

    public <T> CallWrapper<T> get(String url, Type responseType) {
        return request(new Request.Builder().url(url).get().build(), new JSONBodyParser<>(responseType));
    }


}
