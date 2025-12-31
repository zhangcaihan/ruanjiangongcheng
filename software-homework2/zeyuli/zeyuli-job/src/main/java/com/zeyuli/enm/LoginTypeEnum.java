package com.zeyuli.enm;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

/**
 * 登录方式枚举
 *
 * @author 李泽聿
 * @since 2025-11-01 11:21
 */
@Getter
@AllArgsConstructor
public enum LoginTypeEnum {
    ACCOUNT_SECRET_LOGIN("ACCOUNT_SECRET_LOGIN"),
    QRCODE_LOGIN("QRCODE_LOGIN");

    private final String loginType;
}
