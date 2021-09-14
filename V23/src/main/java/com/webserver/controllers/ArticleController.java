package com.webserver.controllers;

import com.webserver.http.HttpRequest;
import com.webserver.http.HttpResponse;
import com.webserver.vo.Article;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ArticleController {
    private HttpRequest request;
    private HttpResponse response;
    private static String articles = "articles/";

    static {
        File file = new File(articles);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    public ArticleController(HttpRequest request, HttpResponse response) {
        this.request = request;
        this.response = response;
    }

    public void writeArticle() {
        String title = request.getParameter("title");
        String author = request.getParameter("author");
        String content = request.getParameter("content");
        File file = new File(articles + title + ".obj");

        if (title == null || content == null || author == null || file.exists()) {
            response.setEntity(new File("webapps/myweb/article_fail.html"));
            return;
        }

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(new Article(title, author, content));
            response.setEntity(new File("webapps/myweb/article_success.html"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 查看文章列表
     */
    public void showAllArticle() {
        File file = new File(articles);
        File[] articleFiles = file.listFiles(f -> f.getName().endsWith(".obj"));
        List<Article> articles = new ArrayList<>();
        if (articleFiles != null) {
            for (File articleFile : articleFiles) {
                try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(articleFile))) {
                    Object o = ois.readObject();
                    if (o instanceof Article) {
                        articles.add((Article) o);
                    }
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        try (PrintWriter pw = response.getWriter()) {
            pw.println("<!DOCTYPE html>\n" +
                    "<html lang=\"en\">\n" +
                    "<head>\n" +
                    "    <meta charset=\"UTF-8\">\n" +
                    "    <title>用户列表</title>\n" +
                    "</head>\n" +
                    "<body>\n" +
                    "    <center>\n" +
                    "        <h1>文章列表</h1>\n" +
                    "        <table border=\"1\">\n" +
                    "            <tr>\n" +
                    "                <td>标题</td>\n" +
                    "                <td>作者</td>\n" +
                    "            </tr>\n");
            for (Article article : articles) {
                pw.println("            <tr>\n" +
                        "                <td><a href=\"lookArticle?title=" + article.getTitle() + "\">" + article.getTitle() + "</a></td>\n" +
                        "                <td><a href=\"lookAuthor?author=" + article.getAuthor() + "\">" + article.getAuthor() + "</a></td>\n" +
                        "            </tr>");
            }
            pw.println("        </table>\n" +
                    "    </center>\n" +
                    "</body>\n" +
                    "</html>");
        }
        response.setContentType("html");

    }

    /**
     * 查看文章
     */
    public void lookArticle() {
        String title = request.getParameter("title");
        File file = new File(articles + title + ".obj");
        if (!file.exists()) {
            response.setEntity(new File("webapps/myweb/article_none.html"));
            return;
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            Object o = ois.readObject();
            if (o instanceof Article) {
                Article article = (Article) o;
                try (PrintWriter pw = response.getWriter();) {
                    pw.println("<!DOCTYPE html>\n" +
                            "<html lang=\"en\">\n" +
                            "<head>\n" +
                            "    <meta charset=\"UTF-8\">\n" +
                            "    <title>" + article.getTitle() + "</title>\n" +
                            "</head>\n" +
                            "<body>\n" +
                            "    <center>\n" +
                            "        <table border=\"1\">\n" +
                            "            <tr>\n" +
                            "                <td>标题</td>\n" +
                            "                <td>作者</td>\n" +
                            "            </tr>\n" +
                            "            <tr>\n" +
                            "                <td>" + article.getTitle() + "</td>\n" +
                            "                <td>" + article.getAuthor() + "</td>\n" +
                            "            </tr>\n" +
                            "\n" +
                            "            <tr>\n" +
                            "                <td colspan=\"2\">" + article.getContent() + "</td>\n" +
                            "            </tr>\n" +
                            "\n" +
                            "        </table>\n" +
                            "    </center>\n" +
                            "</body>\n" +
                            "</html>");
                }

            }
            response.setContentType("html");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 查看作者拥有的帖子
     */
    public void lookAuthor() {
        String author = request.getParameter("author");
        File file = new File(articles);
        File[] articleFiles = file.listFiles(f -> f.getName().endsWith(".obj"));
        List<Article> articles = new ArrayList<>();
        if (articleFiles != null) {
            for (File articleFile : articleFiles) {
                try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(articleFile))) {
                    Object o = ois.readObject();
                    if (o instanceof Article) {
                        Article article = (Article) o;
                        if (article.getAuthor().equals(author)) {
                            articles.add(article);
                        }
                    }
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
            try (PrintWriter pw = response.getWriter()) {
                pw.println("<!DOCTYPE html>\n" +
                        "<html lang=\"en\">\n" +
                        "<head>\n" +
                        "    <meta charset=\"UTF-8\">\n" +
                        "    <title>" + author + "</title>\n" +
                        "</head>\n" +
                        "<body>\n" +
                        "    <center>\n" +
                        "        <table border=\"1\">\n" +
                        "            <tr>\n" +
                        "                <td>标题</td>\n" +
                        "            </tr>\n");
                for (Article article : articles) {
                    pw.println(
                            "            <tr>\n" +
                                    "                <td><a href=\"lookArticle?title=" + article.getTitle() + "\">" + article.getTitle() + "</a></td>\n" +
                                    "            </tr>\n");
                }
                pw.println("\n" +
                        "        </table>\n" +
                        "    </center>\n" +
                        "</body>\n" +
                        "</html>");
            }
            response.setContentType("html");
        }
    }
}
