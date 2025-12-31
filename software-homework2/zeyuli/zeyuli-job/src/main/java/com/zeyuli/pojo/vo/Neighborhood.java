package com.zeyuli.pojo.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 高德地图API返回的小区信息
 *
 * @author : 李泽聿
 * @since : 2025-12-08 08:32
 */
@Setter
@Getter
public class Neighborhood {
    private List<String> name;
    private List<String> type;

}