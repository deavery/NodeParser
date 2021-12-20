package com.deavery.service;

import com.deavery.model.dto.Md5OperationDto;

public interface Md5Service {
    void md5Parse(String hash, String operation);

    String md5Hash(String value);

    Md5OperationDto md5Generate(
            int count,
            int from,
            int to
    );

    int random(int min, int max, double skew);
}
