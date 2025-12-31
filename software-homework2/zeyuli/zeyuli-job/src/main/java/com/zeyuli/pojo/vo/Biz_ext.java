package com.zeyuli.pojo.vo;


import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;

/**
 *
 *
 * @author 李泽聿
 * @since 2025-12-10 18:47
 */
@Data
public class Biz_ext {
    private JsonNode cost;
    private JsonNode opentime2;
    private JsonNode level;
    private JsonNode rating;
    private JsonNode open_time;
    private JsonNode ticket_ordering;
}
