package com.zeyuli.pojo.bo;

import lombok.Data;

import java.util.List;

/**
 * 路径
 *
 * @author 李泽聿
 * @since 2025-11-01 10:47
 */
@Data
public class Route {
    private double duration; // 总时长(秒)
    private double distance; // 总距离(米)
    private String mode; // 交通方式
    private double estimatedCost; // 预估费用
    private List<RouteStep> steps; // 路线步骤
    private String polyline; // 路线轨迹点集
    private String startName; // 起点名称
    private String endName; // 终点名称
}
