package com.webserver.http;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * 该类对象用于表示发送给客户端的HTTP
 * 响应内容
 * 每个响应包括三部分
 * 状态行，响应头，响应正文
 */
public class HttpResponse {
//    状态行相关信息
    private int statusCode = 200;
    private String statusReason = "ok";

    //响应头相关信息
    private Map<String,String> headers = new HashMap<>();
//    响应正文相关信息

//    正文的实体文件
    private File entity;


    //和连接相关的信息
    private Socket socket;
    private OutputStream out;

    public HttpResponse(Socket socket) {
        this.socket = socket;
        try {
            this.out = socket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 将当前对象以一个标准的http响应格式发送给客户端
     */
    public void flush(){
        System.out.println("HttpResponse：开始发送响应...");
        //发送状态行
        sendStatusLine();
        //发送响应头
        sendHeaders();
        //发送响应正文
        sendContent();
        System.out.println("HttpResponse：发送响应完毕！");
    }
    /**
     * 发送状态行
     */
    private void sendStatusLine(){
        try{
            System.out.println("HttpResponse:开始发送状态行");
            String line = "HTTP/1.1 "+statusCode+" "+statusReason;
            System.out.println("状态行："+line);
            out.write(line.getBytes("ISO8859-1"));
            out.write(13);
            out.write(10);
            System.out.println("HttpResponse：发送状态行完毕！");
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    /**
     * 发送响应头
     */
    private void sendHeaders(){
        try {
            System.out.println("HttpResponse：开始发送响应头");
            for (Map.Entry<String, String> e : headers.entrySet()) {
                String name = e.getKey();
                String value = e.getValue();
                String line = name+": "+value;
                System.out.println("响应头："+line);
                out.write(line.getBytes("ISO8859-1"));
                out.write(13);
                out.write(10);
            }
            out.write(13);
            out.write(10);
            System.out.println("HttpResponse：发送响应头完毕！");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 发送响应正文
     */
    private void sendContent(){
        try (FileInputStream fis = new FileInputStream(entity)){
            System.out.println("HttpResponse：开始发送响应正文");
            byte[] bytes = new byte[1024*10];
            int len = -1;
            while((len = fis.read(bytes))!=-1){
                out.write(bytes,0,len);
            }
            System.out.println("发送响应正文完毕！");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusReason() {
        return statusReason;
    }

    public void setStatusReason(String statusReason) {
        this.statusReason = statusReason;
    }

    public void putHeaders(String name,String value) {
        this.headers.put(name,value);
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public File getEntity() {
        return entity;
    }

    public void setEntity(File entity) {
        this.entity = entity;
    }
}
