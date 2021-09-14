package com.webserver.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.URLDecoder;
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
    private String method;//请求方式
    private String uri;//抽象路径
    //保存表单提交的每一组参数;
//    key:参数名(输入框的名字,网页中input内自定义的name属性)
//    value:参数值(输入框中用户输入的信息)
    private Map<String, String> parameters = new HashMap<>();
    private String requestUri;//请求uri中的请求部分？左侧内容
    private String queryString;//请求uri中的请求部分？右侧内容
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
    public HttpRequest(Socket socket) throws IOException, EmptyRequestException {
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

    /**
     * 解析请求行
     */
    private void parseRequestLine() throws IOException, EmptyRequestException {
        //1.1解析请求行
//        System.out.println("开始解析请求行");

        String line = readLine();
        if (line.isEmpty()) {
            throw new EmptyRequestException("浏览器发送了空请求");
        }
        System.out.println("请求行" + line);
        String[] data = line.split("\\s");//\s是正则表达式中所有的空白字符
        method = data[0];
        uri = data[1];
        protocol = data[2];
        parseURI();
/*
   fontawesome-webfont.woff2?v=4.7.0
 */
//        System.out.println("method:" + method);
//        System.out.println("uri:" + uri);
//        System.out.println("protocol:" + protocol);
//        System.out.println("解析请求行完毕");
    }

    /**
     * 进一步解析uri
     */
    private void parseURI() {
        String[] data = uri.split("\\?");
        requestUri = data[0];//不管有没有？都会拆出来一个
        if (data.length > 1) {//拆出来两个才可以进行后续
            queryString = data[1];
            parseParameters(queryString);
        }
        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            System.out.println(entry.getKey() + ":" + entry.getValue());
        }
    }
    private void parseParameters(String line){
        String[] data = line.split("&");
        for (String para : data) {
            String[] paras = para.split("=");
            if (paras.length > 1) {
                parameters.put(paras[0], paras[1]);
            } else {
                parameters.put(paras[0], null);
            }
        }
    }
    /**
     * 解析消息头
     */
    private void parseHeaders() throws IOException {
//        System.out.println("开始解析消息头");
        String line;
        String[] data;
        while (true) {
            line = readLine();
            if ("".equals(line)) break;
//            if (line.startsWith("Referer")) {
            System.out.println("消息头：" + line);
//            }
            data = line.split(":\\s");
            //消息头名字全部转成小写。防止浏览器发送消息头时,大小写不统一的情况。
            headers.put(data[0].toLowerCase(), data[1]);
        }
//            System.out.println("所有的消息头：" + headers);
//            System.out.println("解析消息头完毕");
    }

    /**
     * 解析消息正文
     */
    private void parseContent() throws IOException {
        System.out.println("开始解析消息正文");
        if(!"post".equalsIgnoreCase(method))return;
        //确定正文长度,根据消息头:Content-Length
        String lengthStr = headers.get("content-length");
        System.out.println("正文长度"+lengthStr);
        if(lengthStr!=null){
            int length = Integer.parseInt(lengthStr);
            //读取正文的所有字节数据
            byte[] data = new byte[length];
            in.read(data);
            //根据消息头Content-Type确定正文类型
            if("application/x-www-form-urlencoded".equalsIgnoreCase(getHeaders("content-type"))){
                String line = new String(data,"ISO8859-1");
                System.out.println(line);
                parseParameters(line);
            }
        }

        //application/x-www-form-urlencoded


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
     *
     * @param name 消息头名称
     * @return 消息头对应的value
     */
    public String getHeaders(String name) {
        return headers.get(name);
    }

    public String getRequestUri() {
        return requestUri;
    }

    public String getQueryString() {
        return queryString;
    }

    public String getParameter(String name) {
        //如果转换失败 。说明字符集写错了。
        String value = parameters.get(name);
        try {
            if (value != null) {
                return URLDecoder.decode(value, "UTF-8");
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return value;
    }
}
