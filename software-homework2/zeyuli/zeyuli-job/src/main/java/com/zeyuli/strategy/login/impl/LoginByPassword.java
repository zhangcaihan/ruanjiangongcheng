package com.zeyuli.strategy.login.impl;


import com.zeyuli.enm.StatusCodeEnum;
import com.zeyuli.mappers.UserMapper;
import com.zeyuli.pojo.User;
import com.zeyuli.pojo.vo.UserVo;
import com.zeyuli.strategy.login.LoginStrategyInterface;
import com.zeyuli.util.JwtUtil;
import com.zeyuli.util.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 根据密码登录
 *
 * @author 李泽聿
 * @since 2025-11-01 10:58
 */
@Component
public class LoginByPassword implements LoginStrategyInterface {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JwtUtil jwtUtil;

    // TODO: 2025-11-01 10:59 具体逻辑待实现
    @Override
    public Map<String, Object> login(UserVo vo) {

        User res = userMapper.login(vo.getUserName(), vo.getPassword());
        System.out.println(res);
        if (res != null) {
            System.out.println(res);
            String token = jwtUtil.createToken(res.getId(), res.getUserName(), res.getPassword());
            HashMap<String, Object> data = new HashMap<>();
            data.put("token", token);
            return Response.success(data);
        }
        return Response.failed(StatusCodeEnum.LOGIN_FAILED);
    }
}
