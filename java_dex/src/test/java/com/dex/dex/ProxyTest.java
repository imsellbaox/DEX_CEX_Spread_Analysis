package com.dex.dex;

import com.alibaba.fastjson.JSONObject;
import com.dex.dex.pojo.Depths;
import com.dex.dex.pojo.DexData;
import com.dex.dex.pojo.api.BinanceBookTicker;
import com.dex.dex.pojo.api.BinancePack;
import com.dex.dex.pojo.api.Depth;
import com.neovisionaries.ws.client.*;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class ProxyTest {

    static long T_indexAT = 0;
    static long T_indexAD = 0;
    static long date1;
    static long date2;

    public static void main(String[] args) throws IOException, WebSocketException {
        WebSocketFactory factory = new WebSocketFactory();
///btcusdt@depth5@100ms
        ProxySettings settings = factory.getProxySettings();
        settings.setServer("http://127.0.0.1:7890");
        WebSocket ws1 = factory.createSocket("wss://fstream.binance.com/stream?streams=btcusdt@bookTicker/btcusdt@depth5@100ms")
                .setMissingCloseFrameAllowed(false);
        ws1.addListener(new WebSocketAdapter() {
            @Override
            public void onTextMessage(WebSocket ws, String message) {
                DexData dexdata = new DexData();
                Depths depths = new Depths();
                BinancePack user = JSONObject.parseObject(message, BinancePack.class);
                if (user.getStream().contains("@bookTicker")){
                    BinanceBookTicker data = JSONObject.parseObject(user.getData(), BinanceBookTicker.class);
                    if ( T_indexAT + 1000 < data.getT() ){
                        date1 = DealTimeStamp(data.getT());
                        dexdata.setTs(date1);
                        dexdata.setPrice((data.getA()+data.getB())/2);
                        System.out.println(dexdata);
                        T_indexAT = date1;
                    }
                }
                if (user.getStream().contains("@depth")){
                    Depth data = JSONObject.parseObject(user.getData(), Depth.class);
                    if (data.getT() > T_indexAD + 1000){
                        date2 = DealTimeStamp(data.getT());
                        depths.setTs(date2);
                        System.out.println(depths);
                        T_indexAD = date2;
                    }
                }
            }
        });
        ws1.connect();


    }
    public static long DealTimeStamp(long T){
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String str = sdf.format(T);
        return  Timestamp.valueOf(str).getTime();
    }
}
