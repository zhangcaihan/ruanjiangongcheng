package com.zeyuli.pojo.vo;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 获取路径规划实体类
 *
 * @author 李泽聿
 * @since 2025-12-10 19:36
 */

@Data
@JsonIgnoreProperties(ignoreUnknown = true) // 忽略JSON中的其他未知字段
public class GetRouteVo {
    private GetRouteData data;
    private int errcode;
    private String errdetail;
    private String errmsg;
    private String ext;
    @JsonProperty("route")
    private RouteVo routeVo;
}