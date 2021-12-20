package com.deavery.dao;

import com.deavery.model.Md5Operation;
import com.deavery.model.Random;

import java.util.List;

public interface Md5Dao {

    String createOperation(Md5Operation operation, boolean generate);

    void pushResult(String uuid, List<Random> hashList);

    Md5Operation readOperation(String operation);

    List<Random> readOperationResult(String operation);
}
