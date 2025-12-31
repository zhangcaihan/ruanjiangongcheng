package com.zeyuli.pojo.bo;


import lombok.Data;

/**
 * 路线步骤
 *
 * @author 李泽聿
 * @since 2025-12-08
 */
@Data
public class RouteStep {
    /**
     * 导航指示
     */
    private String instruction;

    /**
     * 该段距离（米）
     */
    private double distance;

    /**
     * 该段时长（秒）
     */
    private double duration;

    /**
     * 该段交通方式
     */
    private String mode;

    /**
     * 该段费用（元，可选）
     */
    private double cost;
}
