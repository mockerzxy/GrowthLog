package com.example.xueyuanzhang.growthlog.model;

import java.util.Date;

/**
 * Created by xueyuanzhang on 16/7/6.
 */

public class QUser {
    private Integer userID;
    private String userName;
    private String password;
    private String nickName;
    private String avatar;
    private String sex;
    private Date birth;
    private String mail;
    private Integer message;

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Date getBirth() {
        return birth;
    }

    public void setBirth(Date birth) {
        this.birth = birth;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public Integer getMessage() {
        return message;
    }

    public void setMessage(Integer message) {
        this.message = message;
    }

//    public void initByIUser(IUser iuser) {
//        this.userID = iuser.getUserID();
//        this.userName = iuser.getUserName();
//        this.password = iuser.getPassword();
//        this.nickName = iuser.getNickName();
//        this.avatar = iuser.getAvatar();
//        this.sex = iuser.getSex();
//        this.birth = iuser.getBirth();
//        this.mail = iuser.getMail();
//    }
}

