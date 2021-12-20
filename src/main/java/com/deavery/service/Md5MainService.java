package com.deavery.service;

import com.deavery.model.dto.Md5ResultDto;
import lombok.SneakyThrows;
import okhttp3.Response;

import java.util.List;

public interface Md5MainService {
    String createOperation(int count);

    @SneakyThrows
    Response md5Generate(int count, int min, int max);

    Md5ResultDto parseList(List<String> parse, String operation, int nodeLimit);
}
