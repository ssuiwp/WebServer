package com.webserver.http;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * 保存所有和Http协议相关的定义
 */
public class HttpContext {
    /**
        资源后缀与Content-Type头的对应关系
        key:资源后缀名  value : Content-Type对应的值
     */
    private static Map<String, String> mimeMapping = new HashMap<>();


    //初始化静态资源
    static {
        initMimeMapping();//初始化mimeMapping
    }

    /**
     * 初始化mimeMapping
     */
    private static void initMimeMapping(){
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream("webapps/Extension.txt")))) {
            String d;
            while ((d = br.readLine()) != null) {
                String[] split = d.split(":");
                mimeMapping.put(split[0], split[1]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *获取后缀名对应的Content-type
     * @param ext 后缀名
     * @return Content-type
     */
    public static String getMimeType(String ext){
        return mimeMapping.get(ext);
    }
}
