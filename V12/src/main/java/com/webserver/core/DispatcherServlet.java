package com.webserver.core;

import com.webserver.http.HttpContext;
import com.webserver.http.HttpRequest;
import com.webserver.http.HttpResponse;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * 用于处理请求
 */
public class DispatcherServlet {

    public void service(HttpResponse response, HttpRequest request){
        response.setProtocol(request.getProtocol());
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
        //后缀名
//        String ext = type.get(file.getName().split("\\.")[file.getName().split("\\.").length-1]);

        response.addHeader("Content-Type", HttpContext.getMimeType(file));
        response.addHeader("Content-Length",file.length()+"");
        response.addHeader("Server","WebServer");
        response.setEntity(file);
    }
}
