package com.deavery.service.impl;

import com.deavery.dao.Md5Dao;
import com.deavery.exception.runtime.IllegalAccressException;
import com.deavery.exception.runtime.IllegalParamsException;
import com.deavery.model.Md5Operation;
import com.deavery.model.dto.Md5ResultDto;
import com.deavery.service.Md5MainService;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

@Service
@AllArgsConstructor
@Slf4j
@Transactional(isolation = Isolation.SERIALIZABLE)
public class Md5MainServiceImpl implements Md5MainService {

    private final Md5Dao md5Dao;

    @Override
    public String createOperation(int count) {
        return md5Dao.createOperation(new Md5Operation("", count, -1, -1), false);
    }

    @Override
    @SneakyThrows
    public Response md5Generate(int count, int min, int max) {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .connectTimeout(2, TimeUnit.HOURS)
                .writeTimeout(2, TimeUnit.HOURS)
                .readTimeout(2, TimeUnit.HOURS)
                .build();

        HttpUrl httpUrl = new HttpUrl.Builder()
                .scheme("https")
                .host("localhost")
                .port(8088)
                .addPathSegment("md5")
                .addPathSegment("natural")
                .addPathSegment("generate")
                .addQueryParameter("count", String.valueOf(count))
                .addQueryParameter("min", String.valueOf(min))
                .addQueryParameter("max", String.valueOf(max))
                .build();

        Request request = new Request.Builder()
                .url(httpUrl)
                .method("GET", null)
                .build();
        return client.newCall(request).execute();
    }

    @SneakyThrows
    @Override
    public Md5ResultDto parseList(List<String> parse, String operation, int nodeLimit) {

        if (nodeLimit < 1) throw new IllegalAccressException();

        Semaphore semaphore = new Semaphore(nodeLimit);

        List<Thread> parsers = new ArrayList<>();

        long timeStart = System.nanoTime();
        for (String hash : parse)
            parsers.add(
                    new Md5ParseThreadImpl(semaphore, hash, operation)
            );

        for (Thread thread : parsers) thread.start();
        for (Thread thread : parsers) thread.join();

        return new Md5ResultDto((System.nanoTime()-timeStart)/1000, md5Dao.readOperation(operation), md5Dao.readOperationResult(operation));
    }

}
