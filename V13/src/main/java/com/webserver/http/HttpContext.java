package com.webserver.http;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.*;
import java.util.HashMap;
import java.util.List;
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
        try {
            initMimeMapping();//初始化mimeMapping
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化mimeMapping
     */
    private static void initMimeMapping() throws DocumentException {
//        SAXReader reader = new SAXReader();
//        Document doc = reader.read("config/web.xml");
//        Element root = doc.getRootElement();
        Element root = new SAXReader().read("config/web.xml").getRootElement();
        List<Element> mapping = root.elements("mime-mapping");
        for (Element mapEle : mapping) {
            String ext = mapEle.elementText("extension");
            String type = mapEle.elementText("mime-type");
            mimeMapping.put(ext,type);
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

    public static void main(String[] args) {
        System.out.println(mimeMapping.size());
        System.out.println(getMimeType("mp3"));
    }
}
