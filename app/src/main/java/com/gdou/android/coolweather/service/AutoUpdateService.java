package com.gdou.android.coolweather.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;

import com.gdou.android.coolweather.gson.Weather;
import com.gdou.android.coolweather.util.HttpUtil;
import com.gdou.android.coolweather.util.Utility;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 实现后台自动更新天气
 */

public class AutoUpdateService extends Service {

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // 定时完成更新天气和背景图片
        updateWeather();
        updateBindPic();
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        // 8小时毫秒数
        int anHour = 8 * 60 * 60 * 1000;
        // 设置定时任务被触发的时间
        // SystemClock.elapsedRealtime()得到系统开机至今所经历的时间的毫秒数
        long triggerAtTime = SystemClock.elapsedRealtime() + anHour;
        // 获取一个能够执行服务或广播的PendingIntent. 这样当定时任务被执行的时候， 服务的onStartCommand()
        // 方法或广播接收器的onReceive()方法就可以得到执行
        // 这里设置了再系统开机后的8小时后AutoUpdateService的onStartComand()方法就会被执行
        Intent i = new Intent(this, AutoUpdateService.class);
        PendingIntent pi = PendingIntent.getService(this, 0, i, 0);
        // 取消掉所有与pi相关的相同的alarm
        manager.cancel(pi);

        // 设置(新的)定时任务
        //      第一个参数AlarmManager.ELAPSED_REALTIME_WAKEUP表示让定时任务的触发时间从系统开机时间算起
        //          会唤醒cpu
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
        return super.onStartCommand(intent, flags, startId);
    }

    private void updateBindPic() {
        String requestBingPic = "http://guolin.tech/api/bing_pic";
        HttpUtil.sendOkHttpRequest(requestBingPic, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String bingPic = response.body().string();
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(AutoUpdateService.this).edit();
                editor.putString("bing_pic", bingPic);
                editor.apply();
            }
        });
    }

    private void updateWeather() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        final String weatherContent = prefs.getString("weather", null);
        if (weatherContent != null) {
            // 有缓存直接解析天气的数据
            Weather weather = Utility.handleWeatherResponse(weatherContent);
            String weatherId = weather.basic.weatherId;
            String weatherUrl = "http://guolin.tech/api/weather?cityid=" + weatherId + "&key=9347e1f2ea2648e3825bf7addea2cce5";
            HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String weatherString = response.body().string();
                    Weather weather2 = Utility.handleWeatherResponse(weatherString);
                    if (weather2 != null && "ok".equals(weather2.status)) {
                        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(AutoUpdateService.this).edit();
                        editor.putString("weather", weatherString);
                        editor.apply();
                    }
                }
            });
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}



























