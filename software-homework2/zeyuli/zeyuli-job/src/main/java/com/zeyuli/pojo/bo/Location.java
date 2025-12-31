package com.zeyuli.pojo.bo;

import lombok.Data;

/**
 * 位置信息
 *
 * @author 李泽聿
 * @since 2025-11-01 10:45
 */
@Data
public class Location {
    // 名称
    private String name;
    // 纬度
    private double lat;
    // 经度
    private double lng;
    // 地址
    private String address;
    // 类型
    private String type;
    // 城市编码
    private String cityCode;
}
