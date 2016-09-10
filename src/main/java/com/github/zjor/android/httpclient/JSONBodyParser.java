package com.github.zjor.android.httpclient;

import com.google.gson.Gson;
import okhttp3.Response;

import java.lang.reflect.Type;

public class JSONBodyParser<T> implements BodyParser<T> {

    private Class<T> clazz;

    private Type type;

    private final Gson gson = new Gson();

    public JSONBodyParser(Class<T> clazz) {
        this.clazz = clazz;
    }

    public JSONBodyParser(Type type) {
        this.type = type;
    }

    @Override
    public T parse(Response response) throws ParseException {
        try {
            String body = response.body().string();
            if (clazz != null) {
                return gson.fromJson(body, clazz);
            } else {
                return gson.fromJson(body, type);
            }
        } catch (Exception e) {
            throw new ParseException(e.getMessage(), e);
        }
    }
}
