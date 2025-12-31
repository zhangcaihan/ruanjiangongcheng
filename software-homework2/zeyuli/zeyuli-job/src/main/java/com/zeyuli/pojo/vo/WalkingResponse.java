package com.zeyuli.pojo.vo;


import lombok.Data;

import java.util.List;

/**
 * 接受步行返回的数据
 *
 * @author 李泽聿
 * @since 2025-12-08 10:09
 */

@Data
public class WalkingResponse {
    private String status;
    private String info;
    private RouteX route;

    @Data
    public static class RouteX {
        private List<Path> paths;
    }

    @Data
    public static class Path {
        private String distance;
        private String duration;
        private String steps;        // 高德步行返回的是“已拼接好的字符串”
        private String polyline;     // 轨迹
        private OriginDest origin;   // 起点
        private OriginDest destination;
    }

    @Data
    public static class OriginDest {
        private String name;         // 起点/终点名称
    }
}