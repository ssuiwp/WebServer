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
            HttpRequest request = new HttpRequest(socket);
//            HttpResponse response = new HttpResponse(socket);
            /*
                发送一个标准的http响应
                响应的大致内容:
                HTTP/1.1 200 OK(CRLF)
                Content-Type: text/html(CRLF)
                Content-Length: 2546(CRLF)(CRLF)
                1011101010101010101......
             */
            //获取文件
            File file = new File("./webapps/myweb/index.html");
            //发送响应头：
            String line = "HTTP/1.1 200 OK";
            OutputStream out = socket.getOutputStream();
            out.write(line.getBytes("ISO8859-1"));
            out.write(13);
            out.write(10);
            //发送响应头
            out.write("Content-Type: text/html".getBytes("ISO8859-1"));
            out.write(13);
            out.write(10);
            out.write(("Content-Length: "+file.length()).getBytes("ISO8859-1"));
            out.write(13);
            out.write(10);
            out.write(13);
            out.write(10);
            //发送响应正文
            FileInputStream fis = new FileInputStream(file);
            byte[] buf = new byte[1024*8];
            int len;

            while((len = fis.read(buf))!=-1){
                out.write(buf,0,len);
            }
            out.close();

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
