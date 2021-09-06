package com.webserver.http;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * 该类的实例对象用于表示客户端发来的一个HTTP的请求内容
 * 每个请求内容有：
 * 请求行
 * 消息头
 * 请求正文
 */
public class HttpRequest {
    //请求航相关信息：
    //请求方式：
    private String method;
    //抽象路径：
    private String uri;
    //协议版本
    private String protocol;

    //消息头相关信息
    //使用map保存消息头相关信息：key消息头名字，，value：消息头对应的值
    private Map<String,String> headers = new HashMap<>();
//    消息正文相关信息


//    连接相关属性
    //对应客户端的socket
    private Socket socket;
    private InputStream in;

    //初始化HttpRequest，该过程就是解析请求的过程，实例化完毕后，该对象就表示当前客户端发送过来的请求内容
    public HttpRequest(Socket socket) {
        this.socket = socket;
        try {
            this.in = socket.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("开始解析请求");
        //解析请求行
        parseRequestLine();
        //解析消息头
        parseHeaders();
        //解析消息正文
        parseContent();
        System.out.println("解析请求完毕！");
    }
    private void parseRequestLine(){
        System.out.println("开始解析请求行");
        try {
            String line = readLine();
            System.out.println("请求行"+line);
            String[] data = line.split("\\s");//\s是正则表达式中所有的空白字符
            method =  data[0];
            uri = data[1];
            protocol = data[2];

            System.out.println("method:"+method);
            System.out.println("uri:"+uri);
            System.out.println("protocol:"+protocol);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("解析请求行完毕");
    }
    private void parseHeaders(){
        System.out.println("开始解析消息头");
        try {
            while (true){
                String line = readLine();
                if("".equals(line))break;
                System.out.println("消息头："+line);
                String[] data = line.split(":\\s");
                headers.put(data[0],data[1]);
            }
            System.out.println("所有的消息头："+headers);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("解析消息头完毕");
    }
    private void parseContent(){
        System.out.println("开始解析消息正文");


        System.out.println("消息正文解析完毕");
    }
    private String readLine() throws IOException {
        int d, pred = -1;
        StringBuilder builder = new StringBuilder();
        while ((d = in.read()) != -1) {
            if (d == 10 && pred == 13) break;
            builder.append((char) d);
            pred = d;
        }
        return builder.toString().trim();
    }

    public String getUri() {
        return uri;
    }
}
