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
    //请求行相关信息：
    //请求方式：
    private String method;
    //抽象路径：
    private String uri;
    //协议版本
    private String protocol;

    //消息头相关信息
    //使用map保存消息头相关信息：key消息头名字，，value：消息头对应的值
    private Map<String, String> headers = new HashMap<>();
//    消息正文相关信息

    //    连接相关属性
    //对应客户端的socket
    private Socket socket;
    private InputStream in;

    /**
     * 初始化HttpRequest，该过程就是解析请求的过程，实例化完毕后，该对象就表示当前客户端发送过来的请求内容
     */
    public HttpRequest(Socket socket) throws IOException {
        this.socket = socket;
        try {
            in = socket.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //1.1解析请求行
        parseRequestLine();
        //1.2解析消息头
        parseHeaders();
        //1.3解析消息正文
        parseContent();
    }
//    private void parseRequestLine(){    }
//    private void parseHeaders(){}
//    private void parseContent(){}

    /**
     * 解析请求行
     */
    private void parseRequestLine() throws IOException {
        //1.1解析请求行
        System.out.println("开始解析请求行");

        String line = readLine();
        System.out.println("请求行" + line);
        String[] data = line.split("\\s");//\s是正则表达式中所有的空白字符
        method = data[0];
        uri = data[1];
        protocol = data[2];

        System.out.println("method:" + method);
        System.out.println("uri:" + uri);
        System.out.println("protocol:" + protocol);

        System.out.println("解析请求行完毕");
    }

    /**
     * 解析消息头
     */
    private void parseHeaders() throws IOException {
        System.out.println("开始解析消息头");
        String line;
        String[] data;
        while (true) {
            line = readLine();
            if ("".equals(line)) break;
            System.out.println("消息头：" + line);
            data = line.split(":\\s");
            headers.put(data[0], data[1]);
            System.out.println("所有的消息头：" + headers);
            System.out.println("解析消息头完毕");
        }
    }

    /**
     * 解析消息正文
     */
    private void parseContent() {
        System.out.println("开始解析消息正文");


        System.out.println("消息正文解析完毕");
    }

    /**
     * 网络流读取一行
     *
     * @return 读取到的一行String
     */
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
    public String getMethod() {
        return method;
    }
    public String getProtocol() {
        return protocol;
    }

    /**
     * 分享headers信息只通过key来查询对应的values，不直接提供整个map，防止map被篡改
     * @param name 消息头名称
     * @return 消息头对应的value
     */
    public String getHeaders(String name){
        return headers.get(name);
    }
}
