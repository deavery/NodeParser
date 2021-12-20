package com.deavery.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Md5Operation {
    String operation;
    int count;
    int min;
    int max;
}
