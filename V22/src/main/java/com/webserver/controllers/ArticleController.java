package com.webserver.controllers;

import com.webserver.http.HttpRequest;
import com.webserver.http.HttpResponse;
import com.webserver.vo.Article;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class ArticleController {
    private HttpRequest request;
    private HttpResponse response;
    private static String articles = "articles/";
    static{
        File file = new File(articles);
        if(!file.exists()){
            file.mkdirs();
        }
    }
    public ArticleController(HttpRequest request, HttpResponse response) {
        this.request = request;
        this.response = response;
    }
    public void writeArticle(){
        String title = request.getParameter("title");
        String author = request.getParameter("author");
        String content = request.getParameter("content");
        File file = new File(articles+title+".obj");

        if(title==null||content==null||author==null||file.exists()){
            response.setEntity(new File("webapps/myweb/article_fail.html"));
            return;
        }


        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))){
            oos.writeObject(new Article(title,author,content));
            response.setEntity(new File("webapps/myweb/article_success.html"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
