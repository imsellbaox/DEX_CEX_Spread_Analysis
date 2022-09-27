package com.dex.dex.controller;

import com.dex.dex.mapper.DataMapper;
import com.dex.dex.mapper.DepthsMapper;
import com.dex.dex.mapper.SubDataMapper;
import com.dex.dex.pojo.Depths;
import com.dex.dex.pojo.DexData;
import com.dex.dex.pojo.Sub;
import com.dex.dex.pojo.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/dex")
public class DataController {
    @Autowired
    private DataMapper dataMapper;
    @Autowired
    private SubDataMapper subDataMapper;
    @Autowired
    private DepthsMapper depthsMapper;

    @GetMapping("data/all")
    public List<DexData> findAll() {
        return dataMapper.findall();
    }

    @PostMapping("data/pricesub/second")
    public List<Sub> findAllSubs(@RequestBody Task task){
        String abdex = task.getA_dex()+'/'+task.getB_dex();
        List<Sub> subs = subDataMapper.findallsubs(abdex);
        List<Depths> Adepths = depthsMapper.findallbydex(task.getA_dex());
        List<Depths> Bdepths = depthsMapper.findallbydex(task.getB_dex());
        depthDatainit(subs,Adepths,Bdepths);
        return subs;
    }
    @PostMapping("data/pricesub/min")
    public List<Sub> findAllSubm(@RequestBody Task task){
        String abdex = task.getA_dex()+'/'+task.getB_dex();
        List<Sub> findallsubs = subDataMapper.findallsubs(abdex);
        List<Sub> submin = new ArrayList<>();
        long index = 0;
        for (Sub s:findallsubs){
            if (s.getTs() >= index){
                submin.add(s);
                index = s.getTs() + 59000;
            }
        }
        List<Depths> Adepths = depthsMapper.findallbydex(task.getA_dex());
        List<Depths> Bdepths = depthsMapper.findallbydex(task.getB_dex());
        List<Sub> result = depthDatainit(submin, Adepths, Bdepths);
        return result;
    }
    @PostMapping("data/pricesub/eqmin")
    public List<Sub> findAllSubeqm(@RequestBody Task task){
        String abdex = task.getA_dex()+'/'+task.getB_dex();
        List<Sub> findallsubs = subDataMapper.findallsubs(abdex);
        List<Sub> submin = new ArrayList<>();
        long index = 0;
        for (Sub s:findallsubs){
            if (s.getTs() >= index){
                submin.add(s);
                index = s.getTs() + 59000*15;
            }
        }
        List<Depths> Adepths = depthsMapper.findallbydex(task.getA_dex());
        List<Depths> Bdepths = depthsMapper.findallbydex(task.getB_dex());
        List<Sub> result = depthDatainit(submin, Adepths, Bdepths);
        return result;
    }
    @PostMapping("data/pricesub/hour")
    public List<Sub> findAllSubmh(@RequestBody Task task){
        String abdex = task.getA_dex()+'/'+task.getB_dex();
        List<Sub> findallsubs = subDataMapper.findallsubs(abdex);
        List<Sub> submin = new ArrayList<>();
        long index = 0;
        for (Sub s:findallsubs){
            if (s.getTs() >= index){
                submin.add(s);
                index = s.getTs() + 59000*60;
            }
        }
        List<Depths> Adepths = depthsMapper.findallbydex(task.getA_dex());
        List<Depths> Bdepths = depthsMapper.findallbydex(task.getB_dex());
        List<Sub> result = depthDatainit(submin, Adepths, Bdepths);
        return result;
    }

    public List<Sub> depthDatainit (List<Sub> subs,List<Depths> Adepths,List<Depths> Bdepths ){
        for (Sub s :subs){
            for (Depths d :Adepths){
                if (s.getTs() == d.getDatatime()){
                    s.setAdexdd(d.getDex()+":<br> 买:"+d.getBuy()+"<br> 卖:"+d.getSell()+"<br>");
                }
            }
        }
        for (Sub s :subs){
            for (Depths d :Bdepths){
                if (s.getTs() == d.getDatatime()){
                    s.setBdexdd(d.getDex()+":<br> 买:"+d.getBuy()+"<br> 卖:"+d.getSell()+"<br>");
                }
            }
        }
        return subs;
    }
}

