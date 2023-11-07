package com.lrh.blog.chat.domain;

/**
 * @ProjectName: Blog
 * @Package: com.lrh.blog.chat.domain
 * @ClassName: User
 * @Author: 63283
 * @Description:
 * @Date: 2023/11/7 19:48
 */

public class User {

    public String userId;

    private String userName;

    private String photo;

    private String loginStatus;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getLoginStatus() {
        return loginStatus;
    }

    public void setLoginStatus(String loginStatus) {
        this.loginStatus = loginStatus;
    }
}
