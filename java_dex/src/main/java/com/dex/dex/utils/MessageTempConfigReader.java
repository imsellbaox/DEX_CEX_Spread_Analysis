package com.dex.dex.utils;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import org.springframework.core.io.ClassPathResource;

/**
 * 短信模板配置
 * @author weisian
 * @date
 */
public class MessageTempConfigReader {

    private static Properties prop = null;

    static{
        getResource();
    }
    /**
     * 读取指定配置
     * @return
     */
    public static String getTempConfig(String tempCode){
        return (String) prop.get(tempCode);
    }

    public static Properties getResource() {
        if (prop == null) {
            synchronized (MessageTempConfigReader.class) {
                if (prop == null) {
                    try(InputStream in = new ClassPathResource(
                            "Proxy.properties")
                            .getInputStream();) {

                        prop = new Properties();
//						prop.load(in);
                        prop.load(new InputStreamReader(in,"UTF-8"));   // 解决中文读取乱码问题
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            return prop;
        } else {
            return prop;
        }
    }
}

