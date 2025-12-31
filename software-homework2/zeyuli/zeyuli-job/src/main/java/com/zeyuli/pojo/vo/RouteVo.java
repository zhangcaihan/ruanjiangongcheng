package com.zeyuli.pojo.vo;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

/**
 *
 *
 * @author 李泽聿
 * @since 2025-12-10 22:29
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RouteVo {
    private String origin;
    private String destination;
    private String taxi_cost;
    private List<Paths> paths;
}
