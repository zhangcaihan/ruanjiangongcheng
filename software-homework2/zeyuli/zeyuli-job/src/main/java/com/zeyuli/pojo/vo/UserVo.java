package com.zeyuli.pojo.vo;


import com.zeyuli.enm.LoginTypeEnum;
import lombok.Data;

/**
 *
 *
 * @author 李泽聿
 * @since 2025-10-21 21:00
 */
@Data
public class UserVo {
    private String userName;
    private String password;
    private LoginTypeEnum loginType;
}
