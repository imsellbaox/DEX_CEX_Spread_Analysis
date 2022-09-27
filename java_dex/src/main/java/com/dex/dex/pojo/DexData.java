package com.dex.dex.pojo;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class DexData {
    private long ts;
    private long datatime;
    private String  adex;
    private String  symbol;
    private double price;
    private int depth;
    private Boolean dosub;
}
