package com.zeyuli.pojo.vo;


import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;

/**
 *
 *
 * @author 李泽聿
 * @since 2025-12-10 18:42
 */
@Data
public class SuggestionVo {
    private JsonNode keywords;
    private JsonNode cities;
}
