package com.candy.servicetest.util;

/**
 * Created by candy on 2015/3/30.
 */
public class Phone {
    /*
    * 手机号
    * 串号
    * 用户名
    * 密码
    *
    * */
   private  String IMEI_NO,username,password,phoneNum;

    public Phone() {
    }

    public Phone(String IMEI_NO, String username, String password, String phoneNum) {
        this.IMEI_NO = IMEI_NO;
        this.username = username;
        this.password = password;
        this.phoneNum = phoneNum;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getIMEI_NO() {
        return IMEI_NO;
    }

    public void setIMEI_NO(String IMEI_NO) {
        this.IMEI_NO = IMEI_NO;
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
}
