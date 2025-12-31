package com.zeyuli.pojo;

import lombok.Data;

/**
 * 用户实体类
 *
 * @author : 李泽聿
 * @since : 2025-10-21 21:16
 */
@Data
public class User {
    private String id;
    private String userName;
    private String password;
}