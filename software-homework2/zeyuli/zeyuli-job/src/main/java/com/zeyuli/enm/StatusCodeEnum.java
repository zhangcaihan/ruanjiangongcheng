package com.zeyuli.enm;


import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 状态码枚举
 *
 * @author 李泽聿
 * @since 2025-10-21 16:55
 */
@Getter
@AllArgsConstructor
public enum StatusCodeEnum {
    SUCCESS(200, "成功"),
    LOGIN_FAILED(420, "登录失败"),
    REGISTER_FAILED(421, "注册失败");

    private final Integer statusCode;
    private final String message;
}
