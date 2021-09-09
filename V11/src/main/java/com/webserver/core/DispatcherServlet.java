package com.webserver.core;

import com.webserver.http.HttpRequest;
import com.webserver.http.HttpResponse;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * 用于处理请求
 */
public class DispatcherServlet {
    private static Map<String, String> type = new HashMap<>();

    static {
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream("webapps/Extension.txt")))){
            String d ;
            while((d = br.readLine())!=null){
                String[] split = d.split(":");
                type.put(split[0], split[1]);
            }
//            type.put("html", "text/html");
//            type.put("css", "text/css");
//            type.put("js", "application/javascript");
//            type.put("png", "image/png");
//            type.put("gif", "image/gif");
//            type.put("jpg", "image/jpeg");
//            type.put("ico", "image/icon");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
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
        String ext = type.get(file.getName().substring(file.getName().lastIndexOf(".")+1));
//        String ext = type.get(file.getName().split("\\.")[file.getName().split("\\.").length-1]);

        response.addHeader("Content-Type",ext);
        response.addHeader("Content-Length",file.length()+"");
        response.addHeader("Server","WebServer");
        response.setEntity(file);
    }
}
