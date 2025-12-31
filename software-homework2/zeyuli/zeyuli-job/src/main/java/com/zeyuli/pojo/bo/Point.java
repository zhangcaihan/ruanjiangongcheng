package com.zeyuli.pojo.bo;


import lombok.Data;

/**
 * 地图位置信息
 *
 * @author 李泽聿
 * @since 2025-11-01 10:43
 */
@Data
public class Point {
    // 纬度
    private double lat;
    // 经度
    private double lng;

    public Point(double lat, double lon) {
        this.lat = lat;
        this.lng = lon;
    }

    public String formatLatAndLng() {
        return String.format("%.6f,%.6f", lat, lng);
    }
}
