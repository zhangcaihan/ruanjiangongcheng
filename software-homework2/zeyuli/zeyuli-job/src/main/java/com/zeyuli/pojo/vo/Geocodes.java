/**
 * Copyright 2025 bejson.com
 */
package com.zeyuli.pojo.vo;

import com.fasterxml.jackson.databind.JsonNode;
import com.zeyuli.pojo.bo.Point;
import com.zeyuli.util.JsonUtil;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 高德地图API返回的地址信息
 *
 * @author : 李泽聿
 * @since : 2025-12-08 08:30
 */
@Setter
@Getter
public class Geocodes {

    private JsonNode formatted_address;
    private JsonNode country;
    private JsonNode province;
    private JsonNode citycode;
    private JsonNode city;
    private JsonNode district;
    private JsonNode township;
    private JsonNode neighborhood;
    private JsonNode building;
    private JsonNode adcode;
    private JsonNode street;
    private JsonNode number;
    private JsonNode location;
    private JsonNode level;

    /**
     * 解析经纬度字符串为Point对象<br>
     * 经纬度字符串，格式为"纬度,经度"
     *
     * @author : 李泽聿
     * @since : 2025-12-08 08:43
     * @return : Point
     */
    public Point analyzeLatAndLon() {
        String[] latLon = JsonUtil.text(location).split(",");
        double lat = Double.parseDouble(latLon[0]);
        double lon = Double.parseDouble(latLon[1]);
        return new Point(lat, lon);
    }

}