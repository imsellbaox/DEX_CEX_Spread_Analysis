package com.dex.dex.pojo;


import lombok.Data;

import java.sql.Timestamp;

@Data
public class Task {
    private long ts ;
    private String  A_dex;
    private String  B_dex;
    private int depth_level;
    private String symbol;
    private boolean status;
    private boolean exsit;

    public boolean getStatus() {
        return  this.status;
    }

    public boolean getExsit() {
        return  this.exsit;
    }
}
