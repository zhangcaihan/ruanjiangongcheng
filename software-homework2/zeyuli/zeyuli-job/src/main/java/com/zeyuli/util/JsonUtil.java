package com.zeyuli.util;


import com.fasterxml.jackson.databind.JsonNode;

/**
 * json相关工具类
 *
 * @author 李泽聿
 * @since 2025-12-08 11:05
 */

public class JsonUtil {
    public static String text(JsonNode node) {
        if (node == null) return "";
        if (node.isTextual()) return node.asText();
        if (node.isArray() && !node.isEmpty()) return node.get(0).asText();
        return "";
    }
}
