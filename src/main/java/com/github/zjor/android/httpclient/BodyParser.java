package com.github.zjor.android.httpclient;

import okhttp3.Response;

public interface BodyParser<T> {

    T parse(Response response) throws ParseException;

}
