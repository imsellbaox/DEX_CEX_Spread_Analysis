package com.dex.dex.utils;

import com.dex.dex.mapper.DataMapper;
import com.dex.dex.mapper.SubDataMapper;
import com.dex.dex.pojo.DexData;
import com.dex.dex.pojo.Sub;
import com.dex.dex.pojo.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
@RestController
@Component
public class SubPrice extends Thread {
    Task task;

    private DataMapper dataMapper;
    private SubDataMapper subDataMapper;
    List<DexData> Adex = new ArrayList<>();
    List<DexData> Bdex = new ArrayList<>();
    long Ta = 0;
    long Tb = 0;


    public volatile boolean exit = false;

    public SubPrice() {
        this.task = null;
    }

    public SubPrice(Task t ,DataMapper dm,SubDataMapper su) {
        this.task = t;
        this.subDataMapper=su;
        this.dataMapper = dm;

    }

    @Override
    public void run() {
        if (!exit) {
                Dothing();
                try {
                    Thread.sleep(60000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
        }else{
            this.stop();
        }
    }

    public  void Dothing(){
        try {
            Adex = dataMapper.findbydex(task.getA_dex(),Ta);
        } catch (Exception e) {
            System.out.println(e);
        }
        try {
            Bdex = dataMapper.findbydex(task.getB_dex(),Tb);
        } catch (Exception e) {
            System.out.println(e);
        }
        if (Adex.size() != 0 && Bdex.size() != 0){
//                RetoData(Adex);
//                RetoData(Bdex);
            Ta = Adex.get(0).getDatatime();
            Tb = Bdex.get(0).getDatatime();
            if (Ta >= Tb){
                Ta = Tb;
            }else{
                Tb = Ta;
            }
            Getsub(Adex,Bdex);
        }
    }

    public void exitThread() {
        this.exit=true;
    }

    public  void Getsub(List<DexData> As,List<DexData> Bs){
        List<DexData> dosubArray = new ArrayList<>();
            for (DexData a :As){
                for (DexData b :Bs){
                    if (a.getDatatime() == b.getDatatime()){
                        Sub s =new Sub();
                        s.setTs(a.getDatatime());
                        s.setPricesub((a.getPrice()-b.getPrice()));
                        s.setSymbol(task.getSymbol());
                        s.setAbdex(a.getAdex()+"/"+b.getAdex());
                        subDataMapper.insertonesub(s);
                        dosubArray.add(a);
                        dosubArray.add(b);
                    }
                }
            }
        RetoData(dosubArray);
    }
    public void RetoData(List<DexData> Data){
        for (DexData d : Data){
            d.setDosub(true);
            dataMapper.insertone(d);
        }
    }


}
