package com.zeyuli.pojo.vo;


import lombok.Data;

import java.util.List;

/**
 * 获取POI具体情况Vo
 *
 * @author 李泽聿
 * @since 2025-12-10 08:46
 */
@Data
public class POIDetailsVo {
    private String count;
    private String infocode;
    private List<PoisInDetailsVo> pois;
    private String status;
    private String info;
}






@Data
class Indoor_data {
    private String cmsid;
    private List<String> truefloor;
    private String cpid;
    private List<String> floor;
}

