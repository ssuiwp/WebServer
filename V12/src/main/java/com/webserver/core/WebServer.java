package com.webserver.core;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * webserver主类
 * WebServer是一个模拟Tomcat的web容器
 * Web容器的主要工作为：
 * 1.管理部署在其中的所有网络应用（Webapp）
 * 每一个网络应用都会包含如：网页，静态资源素材，处理业务的逻辑代码等
 * 可以理解为一个网路应用就是我们俗称的一个网站
 * <p>
 * 2.负责与客户端（浏览器）完成TCP网络连接，并基于Http协议进行交互，使得用户
 * 可以通过浏览器浏览当前容器中的网络应用内容
 * <p>
 * 通过本次项目我们的主要目标：
 * 1：运用所学的API完成功能
 * 2：了解Http协议
 * 3：了解Tomcat底层工作原理
 */
public class WebServer {
    private ServerSocket serverSocket;

    public WebServer() {
        try {
            System.out.println("正在启动服务端");
            serverSocket = new ServerSocket(8088);
            System.out.println("服务端启动完毕");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        try {
            while (true) {
                System.out.println("等待客户端连接");
                Socket socket = serverSocket.accept();
                System.out.println("一个客户端已连接！");
                //启动一个线程处理该客户端交互
                new Thread(new ClientHandler(socket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        WebServer webServer = new WebServer();
        webServer.start();
    }
}
