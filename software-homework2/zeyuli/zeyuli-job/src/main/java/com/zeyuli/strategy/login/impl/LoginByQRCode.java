package com.zeyuli.strategy.login.impl;


import com.zeyuli.pojo.vo.UserVo;
import com.zeyuli.strategy.login.LoginStrategyInterface;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 二维码登录
 *
 * @author 李泽聿
 * @since 2025-11-01 11:04
 */
@Component
public class LoginByQRCode implements LoginStrategyInterface {

    // TODO:2025-11-01 11:04 需要实现二维码登录逻辑
    @Override
    public Map<String, Object> login(UserVo vo) {
        return Map.of();
    }
}
