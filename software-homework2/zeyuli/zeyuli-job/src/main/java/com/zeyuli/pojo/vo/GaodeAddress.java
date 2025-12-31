package com.zeyuli.pojo.vo;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 用来接受高德地图的结果{@code "/geocode/geo?address=" }
 *
 * @author 李泽聿
 * @since 2025-12-08 08:27
 */
@Setter
@Getter
public class GaodeAddress {

    private String status;
    private String info;
    private String infocode;
    private String count;
    private List<Geocodes> geocodes;

}