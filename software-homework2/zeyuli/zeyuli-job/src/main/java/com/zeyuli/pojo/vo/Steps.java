package com.zeyuli.pojo.vo;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;

/**
 *
 *
 * @author 李泽聿
 * @since 2025-12-10 19:37
 */

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Steps {
    private JsonNode action;
    private JsonNode assistant_action;
    private int distance;
    private int duration;
    private String instruction;
    private String orientation;
    private String polyline;
    private String road;
    private int walk_type;

}