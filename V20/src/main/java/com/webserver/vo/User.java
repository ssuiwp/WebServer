package com.webserver.vo;

import java.io.Serializable;

/**
 * VO: value Object:值对象
 * 该类的一个实例用于表示一组信息
 * <p>
 * user 用户对象
 * 该类的每一个实例用于表示一组信息
 * <p>
 * pojo: 实体对象,映射数据库的一条数据
 * <p>
 * java BEAN:类的标准定义   咖啡豆
 */
public class User implements Serializable {
    private String username;
    private String password;
    private String nickname;
    private int age;

    public User(String username, String password, String nickname, int age) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.age = age;
    }

    public User() {

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", nickname='" + nickname + '\'' +
                ", age=" + age +
                '}';
    }
}
