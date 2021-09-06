package com.webserver.core;



import com.webserver.http.HttpRequest;
import com.webserver.http.HttpResponse;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

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
            InputStream in = socket.getInputStream();
            int d;
            while((d = in.read())!=-1){

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
