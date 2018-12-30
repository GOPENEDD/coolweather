package com.gdou.android.coolweather.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 创建一个总的实例类来引用刚刚创建的各个实体类
 */

public class Weather {
    public String status;
    public Basic basic;
    public AQI aqi;
    public Now now;
    public Suggestion suggestion;

    @SerializedName("daily_forecast")
    public List<Forecast> forecastList;
}
