package com.zeyuli.strategy.login;


import com.zeyuli.strategy.login.impl.LoginByPassword;
import com.zeyuli.strategy.login.impl.LoginByQRCode;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 登录工厂
 *
 * @author 李泽聿
 * @since 2025-11-01 10:57
 */

@Component
public class LoginFactory {
    private Map<String, LoginStrategyInterface> loginType;
    private final LoginByPassword passwordLoginStrategy;
    private final LoginByQRCode qrCodeLoginStrategy;

    @Autowired
    public LoginFactory(LoginByPassword passwordLoginStrategy, LoginByQRCode orCodeLoginStrategy) {
        this.passwordLoginStrategy = passwordLoginStrategy;
        this.qrCodeLoginStrategy = orCodeLoginStrategy;
    }

    @PostConstruct
    public void init() {
        loginType = new HashMap<>();
        loginType.put("ACCOUNT_SECRET_LOGIN", passwordLoginStrategy);
        loginType.put("QRCODE_LOGIN", qrCodeLoginStrategy);
    }

    public LoginByPassword login(String type) {
        return (LoginByPassword) loginType.get(type);
    }

    public LoginByQRCode login(String type,String uid) {
        return (LoginByQRCode) loginType.get(type);
    }
}