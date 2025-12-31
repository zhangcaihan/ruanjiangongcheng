package com.zeyuli.pojo.vo;


import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;

/**
 *
 *
 * @author 李泽聿
 * @since 2025-12-10 18:48
 */
@Data
public class Photos {
    private JsonNode provider;
    private JsonNode title;
    private JsonNode url;
}
