package com.deavery.service.impl;

import com.deavery.service.Md5ParseThread;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

@AllArgsConstructor
@Slf4j
public class Md5ParseThreadImpl extends Thread implements Md5ParseThread {

    private Semaphore semaphore;
    private String hash;
    private String operation;

    @SneakyThrows
    public void run()
    {
        try {

            semaphore.acquire();

            OkHttpClient client = new OkHttpClient().newBuilder()
                    .connectTimeout(2, TimeUnit.HOURS)
                    .writeTimeout(2, TimeUnit.HOURS)
                    .readTimeout(2, TimeUnit.HOURS)
                    .build();

            HttpUrl httpUrl = new HttpUrl.Builder()
                    .scheme("http")
                    .host("localhost")
                    .port(8088)
                    .addPathSegment("md5")
                    .addPathSegment("natural")
                    .addQueryParameter("hash", hash)
                    .addQueryParameter("operation", operation)
                    .build();

            Request request = new Request.Builder()
                    .url(httpUrl)
                    .method("GET", null)
                    .build();
            Response response = client.newCall(request).execute();
            log.info(response.body().string() + " Hash: " + hash + " Operation: " + operation);

            semaphore.release();
        } catch (InterruptedException e) {
            log.error("Parse thread ERROR!  MD5HASH: " + hash);
        }
    }
}
