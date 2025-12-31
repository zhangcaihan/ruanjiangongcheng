package com.zeyuli.pojo.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.Setter;
import org.stringtemplate.v4.ST;

/**
 * 高德地图POI实体类
 *
 * @author 李泽聿
 * @since 2025-12-08 09:10
 */
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Pois {
    // 所有可能为各种类型的字段都使用 JsonNode
    private JsonNode parent;
    private String address;      // 可能是字符串，也可能是其他结构
    private JsonNode distance;     // 高德API有时返回字符串，有时返回数组
    private JsonNode space_num;
    private JsonNode pname;
    private JsonNode importance;
    private JsonNode biz_ext;
    private JsonNode biz_type;
    private JsonNode cityname;
    private String type;
    private JsonNode photos;
    private JsonNode building;
    private JsonNode typecode;
    private JsonNode shopinfo;
    private JsonNode poiweight;
    private JsonNode childtype;
    private JsonNode adname;
    private String name;
    private String location;
    private JsonNode tel;
    private JsonNode shopid;
    private JsonNode id;
    private JsonNode favorite_num;
    private JsonNode featured_reviews;
    private JsonNode parking_type;
    private JsonNode entr_location;
    private JsonNode exit_location;
    private JsonNode navi_poiid;
    private JsonNode gridcode;
    private JsonNode alias;
    private JsonNode floor;
    private JsonNode postcode;
    private JsonNode website;
    private JsonNode email;
    private JsonNode timestamp;
    private JsonNode match;
    private JsonNode recommend;
    private JsonNode indoor_map;
    private JsonNode indoor_data;
    private JsonNode indoor_state;
    private JsonNode groupbuy_num;
    private JsonNode discount_num;
    private JsonNode biz_info;
    private JsonNode event;
    private JsonNode children;
    private JsonNode cost;
    private JsonNode rating;
    private JsonNode tag;

    public String getDistanceAsString() {
        return distance != null ? distance.asText() : null;
    }


    public String getCityNameAsString() {
        return cityname != null ? cityname.asText() : null;
    }

    // 如果 distance 是数组，尝试获取第一个元素
    public String getDistanceValue() {
        if (distance == null) return null;

        if (distance.isArray() && distance.size() > 0) {
            return distance.get(0).asText();
        } else {
            return distance.asText();
        }
    }
}