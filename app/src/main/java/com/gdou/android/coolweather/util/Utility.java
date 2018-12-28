package com.gdou.android.coolweather.util;

import android.text.TextUtils;

import com.gdou.android.coolweather.db.City;
import com.gdou.android.coolweather.db.County;
import com.gdou.android.coolweather.db.Province;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Utility {
    /**
     * 解析和处理服务器返回的省级数据
     * TextUtils.isEmpty 判断文本是否为空， 长度是否为零， 空串不能判断
     */

    public static boolean handleProvinceResponse(String response) {
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONArray allProvinces = new JSONArray(response);
                for (int i = 0; i < allProvinces.length(); i++) {
                    JSONObject provincJso = allProvinces.getJSONObject(i);
                    Province province = new Province();
                    province.setProvinceName(provincJso.getString("name"));
                    province.setProvinceCode(provincJso.getInt("id"));
                    province.save();
                }
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 解析处理服务器返回的市级数据
     */
    public static boolean handleCityResponse(String response, int provinceId) {
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONArray allCities = new JSONArray(response);
                for (int i = 0; i < allCities.length(); i++) {
                    JSONObject cityJso = allCities.getJSONObject(i);
                    City city = new City();
                    city.setCityName(cityJso.getString("name"));
                    city.setCityCode(cityJso.getInt("id"));
                    city.setProvinceId(provinceId);
                    city.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return false;
    }

    /**
     * 解析处理服务器返回的县级数据
     */
    public static boolean handleCountyResponse(String response, int cityId) {
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONArray allCounties = new JSONArray(response);
                for (int i = 0; i < allCounties.length(); i++) {
                    JSONObject countyJso = allCounties.getJSONObject(i);
                    County county = new County();
                    county.setId(countyJso.getInt("id"));
                    county.setCountyName(countyJso.getString("name"));
                    county.setCityId(cityId);
                    // 将记录存储到数据库中
                    county.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

}
