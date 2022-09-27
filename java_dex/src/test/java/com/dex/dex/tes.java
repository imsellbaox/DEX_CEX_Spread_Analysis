package com.dex.dex;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class tes {

    public static void main(String[] args) throws ParseException {
//        long a = 1663749342064l;
//        Timestamp test = new Timestamp(a);
//        System.out.println(test);
//        double a = 1.532;
//        double b = 1.542;
//        System.out.println((a + b)/2);
        while (true) {
            System.out.println( DealTimeStamp(1663760232000l));
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


    }
    public static long DealTimeStamp(long T){
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String str = sdf.format(T);
        return  Timestamp.valueOf(str).getTime();
    }
}
