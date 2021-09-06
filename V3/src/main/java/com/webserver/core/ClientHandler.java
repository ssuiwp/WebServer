package com.webserver.core;


import com.webserver.http.HttpRequest;
import com.webserver.http.HttpResponse;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class ClientHandler implements Runnable {
    private Socket socket;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    private String readLine() throws IOException {
        /*
            调用同一个socket对象的getInputStream或getOutputStream
            方法若干次，返回的始终都是一个流。
         */
        InputStream in = socket.getInputStream();
        int d, pred = -1;
        StringBuilder builder = new StringBuilder();
        while ((d = in.read()) != -1) {
            if (d == 10 && pred == 13) break;
            builder.append((char) d);
            pred = d;
        }
        return builder.toString().trim();
    }

    @Override
    public void run() {
        try {
            String line = readLine();
            System.out.println("请求行" + line);
            String[] data = line.split("\\s");//\s是正则表达式中所有的空白字符
            String method;
            String uri;
            String protocol;
            method = data[0];

            uri = data[1];
            protocol = data[2];

            System.out.println("method:" + method);
            System.out.println("uri:" + uri);
            System.out.println("protocol:" + protocol);

            // 解析消息头
            //key存放名字，value存放消息头对应的值
            Map<String,String> headers = new HashMap<>();
            while(!(line = readLine()).isEmpty()){
                System.out.println("消息头: " + line);
                data = line.split(":\\s");
                headers.put(data[0],data[1]);
            }
            System.out.println(headers);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
