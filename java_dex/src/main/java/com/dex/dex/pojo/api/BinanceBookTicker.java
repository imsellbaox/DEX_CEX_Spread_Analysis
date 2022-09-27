package com.dex.dex.pojo.api;

import lombok.Data;

@Data
public class BinanceBookTicker {
    private String e;
    private String s;
    private  double B;
    private double A;
    private long T;
}
