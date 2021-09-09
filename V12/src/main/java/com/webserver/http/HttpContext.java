package com.webserver.http;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * 保存所有和Http协议相关的定义
 */
public class HttpContext {
    /*
        资源后缀与Content-Type头的对应关系
        key:资源后缀名  value : Content-Type对应的值
     */
    private static Map<String, String> mimeMapping = new HashMap<>();

    static {
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
    public static String getMimeType(File file){
        return mimeMapping.get(file.getName().substring(file.getName().lastIndexOf(".")+1));
    }
}
