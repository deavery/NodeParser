package com.deavery.service.impl;

import com.deavery.dao.Md5Dao;
import com.deavery.exception.runtime.IllegalParamsException;
import com.deavery.model.Md5Operation;
import com.deavery.model.dto.Md5OperationDto;
import com.deavery.model.Random;
import com.deavery.service.Md5Service;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
@Slf4j
@Transactional(isolation = Isolation.SERIALIZABLE)
public class Md5ServiceImpl implements Md5Service {

    private final Md5Dao md5Dao;

    @Override
    public void md5Parse(String hash, String operation) {
        for (int i = 0; i <= 99999999; i++) {
            String value = md5Hash(String.valueOf(i));
            if (Objects.equals(hash, value)) {
                List<Random> randomList = new ArrayList<>();
                randomList.add(new Random(i, value));
                md5Dao.pushResult(operation, randomList);
                return;
            }
        }
    }

    @Override
    public String md5Hash(String value) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(value.getBytes());
            BigInteger no = new BigInteger(1, messageDigest);
            StringBuilder hashText = new StringBuilder(no.toString(16));
            while (hashText.length() < 32) {
                hashText.insert(0, "0");
            }
            return hashText.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Md5OperationDto md5Generate(
            int count,
            int min,
            int max
    ) {
        int maxValue = 99999999;
        if (count < 1 || count > 100000) throw new IllegalParamsException();
        if (min > maxValue || max > maxValue) throw new IllegalParamsException();
        if (min < 0 || max < 0) throw new IllegalParamsException();
        List<Random> randomList = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            int value;
            if (i%2 == 0) value = random(min, max, 0.88);
            else value = (int) (Math.random() * (max+1)) + min;
            randomList.add(new Random(value, md5Hash(String.valueOf(value))));
        }
        String uuid = md5Dao.createOperation(new Md5Operation("",count, min, max), true);
        md5Dao.pushResult(uuid, randomList);
        return new Md5OperationDto(new Md5Operation(uuid, count, min, max), randomList);
    }

    @Override //Box–Muller transform
    public int random(int min, int max, double skew) {
        double u = 0, v = 0;
        while (u == 0) u = Math.random();
        while (v == 0) v = Math.random();
        double num = Math.sqrt( -2.0 * Math.log( u ) ) * Math.cos( 2.0 * Math.PI * v );
        num = num / 10.0 + 0.5;
        if (num > 1 || num < 0) num = random(min, max, skew);
        else{
            num = Math.pow(num, skew);
            num *= max - min;
            num += min;
        }
        return (int) num;
    }
}
