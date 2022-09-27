package com.dex.dex.utils;

import com.alibaba.fastjson.JSONObject;
import com.dex.dex.mapper.DataMapper;
import com.dex.dex.mapper.DepthsMapper;
import com.dex.dex.pojo.Depths;
import com.dex.dex.pojo.DexData;
import com.dex.dex.pojo.api.BinanceBookTicker;
import com.dex.dex.pojo.api.BinancePack;
import com.dex.dex.pojo.api.Depth;
import com.neovisionaries.ws.client.WebSocket;
import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.fastjson.JSONObject;
import com.dex.dex.mapper.DataMapper;
import com.dex.dex.mapper.DepthsMapper;
import com.dex.dex.pojo.Depths;
import com.dex.dex.pojo.DexData;
import com.dex.dex.pojo.Task;
import com.dex.dex.pojo.api.BinanceBookTicker;
import com.dex.dex.pojo.api.BinancePack;
import com.dex.dex.pojo.api.Depth;
import com.dex.dex.utils.MessageTempConfigReader;
import com.dex.dex.utils.RedisUtil;
import com.neovisionaries.ws.client.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.sql.Timestamp;

public class WebListen {
    long T_indexAT = 0;
    long T_indexAD = 0;
    long date1;
    long date2;
    BinanceBookTicker data1;
    Depth data2;
    boolean close = false;
    long closetime;
    public void ConfigListen(WebSocket ws,Task task,String dex,DataMapper dataMapper,DepthsMapper depthsMapper){
        ws.addListener(new WebSocketAdapter() {
            @Override
            public void onTextMessage(WebSocket ws, String message) {
                DexData dexdata = new DexData();
                Depths depths = new Depths();
                BinancePack user = JSONObject.parseObject(message, BinancePack.class);
                System.out.println(T_indexAT+"-------"+dex);
                if (user.getStream().contains("@bookTicker")){
                    BinanceBookTicker data = JSONObject.parseObject(user.getData(), BinanceBookTicker.class);
                    if ( T_indexAT + 1000 < data.getT() ){
                         date1 = DealTimeStamp(data.getT());
                         dexdata.setTs(System.currentTimeMillis());
                        dexdata.setDatatime(date1);
                        dexdata.setAdex(dex);
                        dexdata.setSymbol(task.getSymbol());
                        dexdata.setPrice((data.getA()+data.getB())/2);
                        dexdata.setDepth(5);
                        dexdata.setDosub(false);
                        dataMapper.insertone(dexdata);     // todo 修改  时序数据库 相同时间会覆盖数据
                        T_indexAT = date1;
                    }
                }
                if (user.getStream().contains("@depth")){
                    Depth data = JSONObject.parseObject(user.getData(), Depth.class);
                    if (data.getT() > T_indexAD + 1000){
                        date2 = DealTimeStamp(data.getT());
                        depths.setTs(System.currentTimeMillis());
                        depths.setDatatime(date2);
                        depths.setBuy(data.getB());
                        depths.setSell(data.getA());
                        depths.setDex(dex);
                        depthsMapper.insertone(depths);
                        T_indexAD = date2;
                    }
                }
                if(close){
                    if (user.getStream().contains("@bookTicker")){
                       data1 = JSONObject.parseObject(user.getData(), BinanceBookTicker.class);
                    }
                    if (user.getStream().contains("@depth")){
                        data2 = JSONObject.parseObject(user.getData(), Depth.class);
                    }
                    if (data1 != null && data2 !=null){
                        if (  closetime<data1.getT() && closetime<data2.getT()){
                            ws.disconnect();
                        }
                    }
                }
            }
        });
    }
    public static long DealTimeStamp(long T){
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String str = sdf.format(T);
        return  Timestamp.valueOf(str).getTime();
    }
    public void Stop(){
        this.close = true;
        closetime = System.currentTimeMillis();
    }


}
