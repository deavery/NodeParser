package com.deavery.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Random {
    int value;
    String hash;
}
