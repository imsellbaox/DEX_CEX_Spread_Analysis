package com.dex.dex.controller;



import com.dex.dex.mapper.DataMapper;
import com.dex.dex.mapper.DepthsMapper;
import com.dex.dex.mapper.SubDataMapper;
import com.dex.dex.mapper.TaskMapper;
import com.dex.dex.pojo.Task;
import com.dex.dex.utils.MessageTempConfigReader;
import com.dex.dex.utils.SubPrice;
import com.dex.dex.utils.WebListen;
import com.neovisionaries.ws.client.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/dex")
public class TaskController {
    private static final String BINANCE  = "Binance";
    private static final String PANCAKESWAP = "PancakeSwap";
    private Map<String,WebListen> Webs = new HashMap<>();
    private Map<Long,SubPrice> Subtasks = new HashMap<>();
    @Autowired
    private TaskMapper taskMapper;
    @Autowired
    private DataMapper dataMapper;
    @Autowired
    private DepthsMapper depthsMapper;
    @Autowired
    private SubDataMapper subDataMapper;

    @PostMapping("task/insert")
    public boolean insert(@RequestBody Task task) {
        boolean ok = taskMapper.insertone(task);
        return ok;
    }


    @GetMapping("task/all")
    public List<Task> findAll() {
        return taskMapper.findall();
    }


    @PostMapping("task/start/{ts}")
    public boolean startTask(@PathVariable long ts) throws WebSocketException, IOException {
        Task task = taskMapper.Getbyts(ts);
        task.setStatus(true);
        StartOneTask(task);
        taskMapper.insertone(task);
        return true;
    }


    @GetMapping("task/getone/{ts}")
    public Task getone(@PathVariable long ts) {
        Task task = taskMapper.Getbyts(ts);
        return task;
    }


    @PostMapping("task/close/{ts}")
    public boolean CloseTask(@PathVariable long ts) {
        Webs.get(ts+"ws1").Stop();
        Webs.get(ts+"ws2").Stop();
        Subtasks.get(ts).exitThread();
        Task t =  taskMapper.Getbyts(ts);
        SubPrice subtask = new SubPrice(t,dataMapper,subDataMapper);
        subtask.Dothing();
        t.setStatus(false);
        taskMapper.insertone(t);
        return true;
    }
    @PostMapping("task/delete/{ts}")
    public boolean DeleteTask(@PathVariable long ts) {
        Task t = taskMapper.Getbyts(ts);
        if (t.getStatus() == true ){
            return false;
        }
        t.setExsit(false);
        taskMapper.insertone(t);
        return true;
    }




    public boolean StartOneTask(Task task) throws IOException, WebSocketException {
        WebSocketFactory factory = new WebSocketFactory();
        WebSocket ws1 = null;
        WebSocket ws2 = null;
        String BinanceUrl = "wss://fstream.binance.com/stream?streams="+task.getSymbol()+"@bookTicker/"+task.getSymbol()+"@depth"+task.getDepth_level()+"@100ms";
        String PacakeSwapUrl ="wss://fstream.apollox.finance/stream?streams="+task.getSymbol()+"@bookTicker/"+task.getSymbol()+"@depth"+task.getDepth_level()+"@100ms";
         String flag =MessageTempConfigReader.getTempConfig("Proxy.open");
        if (flag.equals("true")){
            ProxySettings settings = factory.getProxySettings();
            settings.setServer(MessageTempConfigReader.getTempConfig("Proxy.host"));
        }

        switch (task.getA_dex()){
            case BINANCE:
                 ws1 = factory.createSocket(BinanceUrl);
                break;
            case PANCAKESWAP:
                 ws1 = factory.createSocket(PacakeSwapUrl);
                break;
            default:
                System.out.println("------------WS1 Error--------------");
                break;
        }
        switch (task.getB_dex()){
            case BINANCE:
                ws2 = factory.createSocket(BinanceUrl);
                break;
            case PANCAKESWAP:
                ws2 = factory.createSocket(PacakeSwapUrl);
                break;
            default:
                System.out.println("------------WS2 Error--------------");
                break;
        }
        WebListen wl1 = new WebListen();
        WebListen wl2 = new WebListen();
        wl1.ConfigListen(ws1,task,task.getA_dex(),dataMapper,depthsMapper);
        wl2.ConfigListen(ws2,task,task.getB_dex(),dataMapper,depthsMapper);

        if (ws1 != null && ws2 != null) {
            ws1.connect();
            ws2.connect();
            Webs.put(task.getTs()+"ws1",wl1);
            Webs.put(task.getTs()+"ws2",wl2);
        }
        SubPrice subtask = new SubPrice(task,dataMapper,subDataMapper);
        subtask.start();
        Subtasks.put(task.getTs(),subtask);
        return true;
    }
}
