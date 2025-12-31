package com.zeyuli.pojo.vo;


import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 *
 *
 * @author 李泽聿
 * @since 2025-12-10 09:50
 */

@Data
public class PoisInDetailsVo {
    private List<String> parent;
    private List<String> distance;
    private String pcode;
    private List<String> importance;
    private Biz_extInPoiDetailsVo biz_ext;
    private String recommend;
    private String type;
    private List<PhotosInPoiDetailsVo> photos;
    private String discount_num;
    private List<String> building;
    private String gridcode;
    private String typecode;
    private String shopinfo;
    private List<String> poiweight;
    private String updateflag;
    private String citycode;
    private String adname;
    private List<String> children;
    private String alias;
    private List<String> tel;
    private String id;
    private List<String> tag;
    private List<String> event;
    private String entr_location;
    private String indoor_map;
    private List<String> email;
    private Date timestamp;
    private List<String> website;
    private String address;
    private List<String> space_num;
    private String adcode;
    private String pname;
    private List<String> biz_type;
    private String cityname;
    private List<String> postcode;
    private String match;
    private String business_area;
    private Indoor_data indoor_data;
    private List<String> childtype;
    private String atag;
    private List<String> exit_location;
    private String name;
    private String location;
    private List<String> shopid;
    private List<String> favorite_num;
    private String navi_poiid;
    private String groupbuy_num;
    private List<String> featured_reviews;
}