package com.candy.servicetest.util;

import java.util.Date;

/**
 * Created by candy on 2015/3/30.
 */
public class Point {
    /*
    * 时间
    * 坐标
    * */
    private Date date;
    private int latitude,longtitude;

    public Point() {
    }

    public Point(Date date, int latitude, int longtitude) {
        this.date = date;
        this.latitude = latitude;
        this.longtitude = longtitude;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getLatitude() {
        return latitude;
    }

    public void setLatitude(int latitude) {
        this.latitude = latitude;
    }

    public int getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(int longtitude) {
        this.longtitude = longtitude;
    }
}
