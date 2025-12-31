package com.zeyuli.pojo.vo;


import lombok.Data;

import java.util.List;

/**
 *
 *
 * @author 李泽聿
 * @since 2025-12-10 19:39
 */
@Data
public class GetRouteData {
    private String destination;
    private String origin;
    private List<Paths> paths;
}
