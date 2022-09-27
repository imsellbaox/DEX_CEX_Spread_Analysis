package com.dex.dex;

import com.dex.dex.mapper.TaskMapper;
import com.dex.dex.pojo.Task;
import com.dex.dex.utils.MessageTempConfigReader;
import org.apache.tomcat.jni.Time;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class Test {


    public static void main(String[] args) {
//        String tempConfig = MessageTempConfigReader.getTempConfig("Proxy.open");
//        System.out.println(tempConfig);
        String  a= "[['19080.20','10.866'],['19080.10','0.202'],['19080.00','0.341'],['19079.40','1.957'],['19079.20','2.137']]";
        List<String> list = Arrays.asList(a.split(","));
        System.out.println(list);
    }

    public long datatimeToTimestamp(LocalDateTime ldt){
        long timestamp = ldt.toInstant(ZoneOffset.of("+8")).toEpochMilli();
        return timestamp;
    }
}
