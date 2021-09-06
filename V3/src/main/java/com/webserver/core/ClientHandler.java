package com.webserver.core;



import com.webserver.http.HttpRequest;
import com.webserver.http.HttpResponse;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class ClientHandler implements Runnable {
    private Socket socket;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            //工作有三部分
            //解析请求
            HttpRequest request = new HttpRequest(socket);
            HttpResponse response = new HttpResponse(socket);
            //2处理请求
            //2.1通过request获取请求路径
            String path = request.getUri();
            System.out.println("该抽象路径为："+path);
            //2.2通过该路径去查询文件
            File file = new File("./webapps"+path);
            //2.3判断文件是否存在
            if(file.exists()){
                System.out.println("该资源已找到");
                response.setEntity(file);
                Map<String,String> map = new HashMap<>();
                map.put("html","text/html");
                map.put("css","text/css");
                map.put("js","application/javascript");
                map.put("png","image/png");
                map.put("gif","image/gif");
                map.put("jpg","image/jpeg");
                //获取请求的文件名
                String fileName = file.getName();
                String ext = fileName.substring(fileName.lastIndexOf(".")+1);
                String type = map.get(ext);

                response.putHeaders("Content-Type",type);
                response.putHeaders("Content-Type",file.length()+"");
            }else{
                System.out.println("该资源不存在！");
                File notFoundFile = new File("./webapps/root/404.html");
                response.setStatusCode(404);
                response.setStatusReason("Not Found");
                response.setEntity(notFoundFile);
                response.putHeaders("Content-Type","text/html");
                response.putHeaders("Content-Type",notFoundFile.length()+"");
            }

            //发送响应
            response.flush();
        } catch (Exception e) {
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
