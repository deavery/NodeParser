package com.deavery.model.dto;


import com.deavery.model.Md5Operation;
import com.deavery.model.Random;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class Md5ResultDto {
    long executionTime;
    Md5Operation operation;
    List<Random> random;
}
