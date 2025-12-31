package com.zeyuli.pojo.vo;


/**
 * 根据天气获取城市信息
 *
 * @author 李泽聿
 * @since 2025-12-09 20:07
 */


import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class WeatherInfoVo {
    private String status;
    private String count;
    private String info;
    private String infocode;
    private List<LivesVo> lives;
}
