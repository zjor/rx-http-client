package com.github.zjor.android.httpclient;

import okhttp3.Response;

public class HttpNotOkException extends Exception {

    public final int code;

    public final Response response;

    public HttpNotOkException(Response response) {
        super(response.message());
        this.code = response.code();
        this.response = response;
    }

}
