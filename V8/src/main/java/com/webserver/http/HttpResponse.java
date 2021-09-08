package com.webserver.http;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * 响应对象
 * 该类的每一个实例表示一个http的响应内容
 * 每个响应包括三部分
 * 状态行，响应头，响应正文
 */
public class HttpResponse {

    //状态行相关信息
    private String protocol = "HTTP/1.1";//协议版本
    private int statusCode = 200;//状态代码
    private String statusReason = "OK";//状态描述

    //响应头相关信息
    private Map<String, String> type = new HashMap<>();
    private String typeName;
    //响应正文相关信息
    private File entity;//正文对应的一个实体文件

    //连接相关属性
    private Socket socket;
    private OutputStream out;

    public HttpResponse(Socket socket) throws IOException {
        this.socket = socket;
        this.out = socket.getOutputStream();
    }

    /**
     * 将当前响应对象内容按照标准Http响应格式发送给客户端。
     */
    public void flush() throws IOException {
        //3.1发送状态行
        sendStatusLine();
        //3.2发送响应头
        sendHeaders();
        //3.3发送响应正文
        sendContent();
    }

    /**
     * 发送状态行
     */
    private void sendStatusLine() throws IOException {
        String line = protocol + " " + statusCode + " " + statusReason;
        println(line);
    }

    /**
     * 发送响应头
     */
    private void sendHeaders() throws IOException {
        String line = "Content-Type: " + "text/html"/*getType(typeName)*/;
        println(line);
        line = "Content-Length: " + entity.length();
        println(line);
        //单独发送CRLF表示响应头发送结束
//        out.write(13);
//        out.write(10);
        println("");
    }

    /**
     * 发送响应正文
     */
    private void sendContent() throws IOException {
        try (FileInputStream fis = new FileInputStream(entity)) {
            byte[] buf = new byte[1024 * 8];
            int len;
            while ((len = fis.read(buf)) != -1) {
                out.write(buf, 0, len);
            }
        }

    }

    /**
     * 发送String
     * @param line 发送给浏览器的字符串
     */
    private void println(String line) throws IOException {
        out.write(line.getBytes("ISO8859-1"));
        out.write(13);
        out.write(10);
    }

    public String getType(String name) {
        return type.get(name);
    }
    public void setTypeName(){
        this.typeName = typeName;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public void setStatusReason(String statusReason) {
        this.statusReason = statusReason;
    }

    public void setEntity(File entity) {
        this.entity = entity;
    }
}