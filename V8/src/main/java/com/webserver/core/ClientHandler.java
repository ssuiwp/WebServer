package com.webserver.core;


import com.webserver.http.HttpRequest;
import com.webserver.http.HttpResponse;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private Socket socket;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            //1.解析请求
            HttpRequest request = new HttpRequest(socket);
            HttpResponse response = new HttpResponse(socket);
            response.setProtocol(request.getProtocol());
            /*
                发送一个标准的http响应
                响应的大致内容:
                HTTP/1.1 200 OK(CRLF)
                Content-Type: text/html(CRLF)
                Content-Length: 2546(CRLF)(CRLF)
                1011101010101010101......
             */
            //获取文件
            String path = request.getUri();
            if("/".equals(path)) {
                path = "/index.html";
            }
            File file = new File("./webapps"+path);


            if(!(file.exists()&&file.isFile())){
                file = new File("./webapps/root/404.html");
                response.setStatusCode(404);
                response.setStatusReason("Not Found");
            }else {
                response.setStatusCode(200);
                response.setStatusReason("OK");
            }
            response.setEntity(file);
            response.flush();
        } catch (IOException e) {
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
