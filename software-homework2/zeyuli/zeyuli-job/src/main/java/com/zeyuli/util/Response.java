package com.zeyuli.util;


import com.zeyuli.enm.StatusCodeEnum;

import java.util.HashMap;
import java.util.Map;

/**
 *
 *
 * @author 李泽聿
 * @since 2025-10-21 16:54
 */

public class Response {

    public static Map<String, Object> success() {
        Map<String, Object> res = new HashMap<>();
        res.put("statusCode", StatusCodeEnum.SUCCESS.getStatusCode());
        res.put("message", StatusCodeEnum.SUCCESS.getMessage());
        return res;
    }

    public static Map<String, Object> success(Object data) {
        Map<String, Object> res = success();
        res.put("data", data);
        return res;
    }

    public static Map<String, Object> failed(StatusCodeEnum statusCodeEnum) {
        Map<String, Object> res = new HashMap<>();
        res.put("statusCode", statusCodeEnum.getStatusCode());
        res.put("message", statusCodeEnum.getMessage());
        return res;
    }

    public static Map<String, Object> failed(StatusCodeEnum statusCodeEnum, Object data) {
        Map<String, Object> res = failed(statusCodeEnum);
        res.put("data", data);
        return res;
    }
}
