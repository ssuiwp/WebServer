package com.webserver.core;

import com.webserver.controllers.ArticleController;
import com.webserver.controllers.UserController;
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

    public void service(HttpResponse response, HttpRequest request) throws UnsupportedEncodingException {
        response.setProtocol(request.getProtocol());
        response.addHeader("Server", "WebServer");
        /*
            V16的第一个改动,不再用uri来作为请求路径。因为如果提交数据,uri会包含？及参数
            此时应该选择requestUri。这是我们处理过的uri只获取请求路径部分
         */
        String path = request.getRequestUri();
//        System.out.println(111111+path);
        //判断请求路径,是否为请求业务逻辑的
        if ("/".equals(path)) {
            path = "/index.html";
        }
        //看所有的文章
        if("/myweb/showAllArticle".equals(path)){
            new ArticleController(request,response).showAllArticle();
            return;
        }
        //看作者
        if("/myweb/lookAuthor".equals(path)){
            new ArticleController(request,response).lookAuthor();
            return;
        }
        //看文章
        if("/myweb/lookArticle".equals(path)){
            new ArticleController(request,response).lookArticle();
            return;
        }
        //看所用的用户
        if("/myweb/showAllUser".equals(path)){
            new UserController(request,response).showAllUser();
            return;
        }
        //写文章
        if("/myweb/writeArticle".equals(path)){
            new ArticleController(request,response).writeArticle();
            return;
        }
        //注册
        if ("/myweb/regUser".equals(path)) {
            new UserController(request, response).reg();
            return;
        }
        //登录
        if("/myweb/loginUser".equals(path)){
            new UserController(request,response).login();
            return;
        }
        File file = new File("./webapps" + path);
        if (!(file.exists() && file.isFile())) {
            file = new File("./webapps/root/404.html");
            response.setStatusCode(404);
            response.setStatusReason("Not Found");
        } else {
            response.setStatusCode(200);
            response.setStatusReason("OK");
        }
        response.setEntity(file);
    }
}
