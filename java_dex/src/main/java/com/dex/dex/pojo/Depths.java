package com.dex.dex.pojo;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class Depths {
    private long ts;
    private long datatime;
    private String dex;
    private String buy;
    private String sell;

}
