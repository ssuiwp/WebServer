package com.webserver.controllers;

import com.webserver.http.HttpRequest;
import com.webserver.http.HttpResponse;
import com.webserver.vo.User;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 处理用户相关业务逻辑
 */
public class UserController {
    private static final String userDir = "./users/";

    static {
        File file = new File(userDir);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    private HttpRequest request;
    private HttpResponse response;

    public UserController(HttpRequest request, HttpResponse response) {
        this.request = request;
        this.response = response;
    }

    /**
     * 注册
     */
    public void reg(/*HttpResponse response,HttpRequest request*/) throws UnsupportedEncodingException {
        System.out.println("开始处理用户注册!~!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!~!~~~~~~~~~~~~~~~~~~~~~~~");
        //1通过request获取用户表单提交的数据
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String nickname = request.getParameter("nickname");
        String age = request.getParameter("age");
        /*
            必要的验证:要求上述信息不能为null,并且年龄要求必须是一个数字格式
            否则直接设置响应对象注册失败的提示页面:reg_input_error.html
            该页面中显示一行字:注册失败,输入信息有误
         */
        //检测请求信息
        if (username == null || password == null || nickname == null || age == null || !age.matches("\\d+")) {
            response.setEntity(new File("webapps/myweb/reg_input_error.html"));
            return;
        }
        if (Integer.parseInt(age) > 120 || Integer.parseInt(age) < 18) {
            response.setEntity(new File("webapps/myweb/reg_input_error.html"));
            return;
        }
        //判断用户是否存在
        File file = new File(userDir);
        File[] files = file.listFiles();
        if (files != null) {
            for (File f : files) {
                String filename = f.getName().substring(0, f.getName().indexOf('.'));
                if (username.equals(filename)) {
                    response.setEntity(new File("webapps/myweb/user_exist.html"));
                    return;
                }
            }
        }
        System.out.println(username + " " + password + " " + nickname + " " + age);
        //2将用户写入文件
        try (FileOutputStream fos = new FileOutputStream(userDir + username + ".obj");
             ObjectOutputStream oos = new ObjectOutputStream(fos);) {
            User user = new User(username, password, nickname, Integer.parseInt(age));
            oos.writeObject(user);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //3设置response响应登录结果页面
        response.setEntity(new File("./webapps/myweb/reg_success.html"));
    }

    /**
     * 登录
     */
    public void login() throws UnsupportedEncodingException {
        System.out.println("开始处理用户登录业务");
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        if (username == null || password == null) {
            response.setEntity(new File("webapps/myweb/login_error.html"));
            return;
        }
        File file = new File(userDir + username + ".obj");
        File failed = new File("webapps/myweb/login_failed.html");
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                Object o = ois.readObject();
                if (o instanceof User) {
                    User user = (User) o;
                    if (password.equals(user.getPassword()) && username.equals(user.getUsername())) {
                        response.setEntity(new File("webapps/myweb/login_success.html"));
                        return;
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
            response.setEntity(failed);
        }

    }

    /**
     * 查看所有用户
     */
    public void showAllUser() {
        //读取所有用户
        File users = new File(userDir);
        File[] userFiles = users.listFiles(f -> f.getName().endsWith(".obj"));
        List<User> userList = new ArrayList<>();//保存所有的用户集合
        if (userFiles == null) {
            response.setEntity(new File(""));
            return;
        }
        for (File userFile : userFiles) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(userFile))) {
                Object o = ois.readObject();
                if (o instanceof User) {
                    userList.add((User) o);
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        System.out.println(userList);
        //用户拼接,每个用户拼接一个<tr>
        try {
            File file = new File("./userList.html");
            PrintWriter pw = new PrintWriter(file, "utf-8");
            pw.println
                    ("<!DOCTYPE html>\n" +
                    "<html lang=\"en\">\n" +
                    "<head>\n" +
                    "    <meta charset=\"UTF-8\">\n" +
                    "    <title>用户列表</title>\n" +
                    "</head>\n" +
                    "<body>\n" +
                    "    <center>\n" +
                    "        <h1>用户列表</h1>\n" +
                    "        <table border=\"1\">\n" +
                    "            <tr>\n" +
                    "                <td>用户名</td>\n" +
                    "                <td>密码</td>\n" +
                    "                <td>昵称</td>\n" +
                    "                <td>年龄</td>\n" +
                    "            </tr>");
            for (User user : userList) {
                pw.println
                        ("            <tr>\n" +
                        "               <td>" + user.getUsername() + "</td>\n" +
                        "               <td>" + user.getPassword() + "</td>\n" +
                        "               <td>" + user.getNickname() + "</td>\n" +
                        "               <td>" + user.getAge() + "</td>\n" +
                        "            </tr>");
            }
            pw.println(
                    "        </table>\n" +
                    "    </center>\n" +
                    "</body>\n" +
                    "</html>");
            pw.flush();
            response.setEntity(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //拼好的页面返回给浏览器

    }
}