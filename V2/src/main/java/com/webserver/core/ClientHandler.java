package com.webserver.core;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

/**
 * 处理与指定客户端的HTTP交互
 * HTTP交互采取一问一答，因此这里分为三步完成一次交互过程：
 * 1：解析请求
 * 2：处理请求
 * 3：响应客户端
 */
public class ClientHandler implements Runnable {
    private Socket socket;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            //1：解析请求
                //1.1:解析请求行
            String method;//请求（方式）方法
            String uri;//请求路径（抽象路径）
            String protocol;//协议版本
            InputStream in = socket.getInputStream();
            StringBuilder sb = new StringBuilder();
            int d;
            int pre = -1;
            while((d = in.read())!=-1){
                if(d == 10 && pre ==13)break;
                pre = d;
                sb.append((char)d);
            }
            String line = sb.toString().trim();
            System.out.println(line);
            String[] data = line.split("\\s");
            method = data[0];
            /*
                如果浏览器发送了空请求（连接服务器没有发送请求内容）
                此时拆分后的数组没有三个元素，因此获取第二项是会出现下标越界，后期会专门解决空请求问题。目前忽略此异常
             */
            uri = data[1];
            protocol = data[2];
            System.out.println("method:"+method);
            System.out.println("uri:"+uri);
            System.out.println("protocol:"+protocol);
            //1.2：解析消息头

                //1.3：解析消息正文

            //2：处理请求

            //3：发送响应

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
