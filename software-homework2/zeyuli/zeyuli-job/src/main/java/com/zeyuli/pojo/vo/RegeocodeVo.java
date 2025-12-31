package com.zeyuli.pojo.vo;


import lombok.Data;

import java.util.List;

/**
 *
 *
 * @author 李泽聿
 * @since 2025-12-09 21:07
 */
@Data
public class RegeocodeVo {
    private String formatted_address;
    private AddressComponent addressComponent;
    private List<Pois> pois;
    private List<Roads> roads;
    private List<Roadinters> roadinters;
    private List<Aois> aois;
}
