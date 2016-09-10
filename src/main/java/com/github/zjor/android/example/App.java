package com.github.zjor.android.example;

import com.github.zjor.android.httpclient.Connector;
import com.github.zjor.android.httpclient.HttpNotOkException;
import com.github.zjor.android.httpclient.ParseException;
import com.google.gson.reflect.TypeToken;
import okhttp3.OkHttpClient;
import rx.Observable;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.MessageFormat;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class App {

    private static final String GITHUB_API_URL = "https://api.github.com";

    private static final Type JUSER_LIST_TYPE = new TypeToken<List<JUser>>() {
    }.getType();

    private Connector connector = new Connector(new OkHttpClient.Builder().build());

    private String getUsersSinceRequest(int since) {
        return MessageFormat.format("{0}/users?since={1}", GITHUB_API_URL, since);
    }

    public void runSync(int since) throws ParseException, HttpNotOkException, IOException {
        List<JUser> users = (List<JUser>) connector.get(getUsersSinceRequest(since), JUSER_LIST_TYPE).sync();
        System.out.println(users);
    }

    public void runFuture(int since) throws InterruptedException, ExecutionException, TimeoutException {
        Future<Object> future = connector.get(getUsersSinceRequest(since), JUSER_LIST_TYPE).future();
        List<JUser> users = (List<JUser>) future.get(5, TimeUnit.SECONDS);
        System.out.println(users);
    }

    public void runStream(Observable<Integer> requests) {
        requests.flatMap(since -> connector.get(getUsersSinceRequest(since), JUSER_LIST_TYPE).observable()).subscribe(users -> {
            System.out.println(users);
        });
    }

    public static void main(String[] args) throws Exception {
        App app = new App();

        System.out.println("Running request synchronously");
        app.runSync(10);

        System.out.println("Running request in the future");
        app.runFuture(15);

        Observable<Integer> requests = Observable.from(new Delayed[]{
                new Delayed(1, 100),
                new Delayed(5, 125),
                new Delayed(10, 150),
                new Delayed(15, 200),
        }).flatMap(item -> Observable.timer(item.delay, TimeUnit.MILLISECONDS).map(x -> (Integer) item.data));
        System.out.println("Running stream of requests");
        app.runStream(requests.throttleLast(50, TimeUnit.MILLISECONDS));

    }

    static class Delayed<T> {

        public final T data;
        public final long delay;

        public Delayed(T data, long delay) {
            this.data = data;
            this.delay = delay;
        }
    }
}
