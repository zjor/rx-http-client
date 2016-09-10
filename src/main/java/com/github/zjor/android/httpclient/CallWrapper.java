package com.github.zjor.android.httpclient;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import rx.AsyncEmitter;
import rx.Observable;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

public class CallWrapper<T> {

    private final Call call;

    private final BodyParser<T> parser;

    public CallWrapper(Call call, BodyParser<T> parser) {
        this.call = call;
        this.parser = parser;
    }

    private T parse(Response response) throws HttpNotOkException, ParseException {
        if (response.isSuccessful()) {
            return parser.parse(response);
        } else {
            throw new HttpNotOkException(response);
        }
    }

    public T sync() throws HttpNotOkException, ParseException, IOException {
        return parse(call.execute());
    }

    public void async(Callback callback) {
        call.enqueue(callback);
    }

    public Future<T> future() {
        final CompletableFuture<T> future = new CompletableFuture<>();
        call.enqueue(new Callback() {
            public void onFailure(Call call, IOException e) {
                future.completeExceptionally(e);
            }

            public void onResponse(Call call, Response response) throws IOException {
                try {
                    future.complete(parse(response));
                } catch (Exception e) {
                    future.completeExceptionally(e);
                }
            }
        });
        return future;
    }

    public Observable<T> observable() {
        return Observable.fromEmitter(emitter -> call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                emitter.onError(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    emitter.onNext(parse(response));
                    emitter.onCompleted();
                } catch (Exception e) {
                    emitter.onError(e);
                }
            }
        }), AsyncEmitter.BackpressureMode.LATEST);
    }


}
