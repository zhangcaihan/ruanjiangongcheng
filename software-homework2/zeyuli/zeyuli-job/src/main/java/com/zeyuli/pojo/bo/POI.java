package com.zeyuli.pojo.bo;


import lombok.Data;

/**
 * 用来返回某个地点周边的设施信息
 *
 * @author 李泽聿
 * @since 2025-11-01 10:48
 */
@Data
public class POI {
    private String name;
    private double lat;
    private double lng;
}
