package com.zeyuli.service.impl;


import com.zeyuli.enm.StatusCodeEnum;
import com.zeyuli.mappers.UserMapper;
import com.zeyuli.pojo.User;
import com.zeyuli.pojo.vo.UserVo;
import com.zeyuli.service.UserService;
import com.zeyuli.strategy.login.LoginFactory;
import com.zeyuli.util.JwtUtil;
import com.zeyuli.util.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import reactor.core.publisher.Flux;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 *
 *
 * @author 李泽聿
 * @since 2025-10-21 16:58
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private LoginFactory loginFactory;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 登录
     *
     * @param vo 用户信息{@link UserVo}
     * @return : java.util.Map<java.lang.String,java.lang.Object> 成功返回：<pre>
     *     {@code
     *      {
     *       "data": {
     *         "token": "token"
     *         },
     *       "message": "成功",
     *       "statusCode": 200
     *    }
     *     }
     * </pre>
     * <br>
     * 失败返回：<pre>
     *     {@code
     *      {
     *         "data": null,
     *         "message": "登录失败",
     *         "statusCode": 420
     *      }
     *     }
     * </pre>
     * @author : 李泽聿
     * @since : 2025-10-21 21:48
     */
    @Override
    public Map<String, Object> login(UserVo vo) {
//        User res = userMapper.login(username, password);
//        if (res != null) {
//            System.out.println(res);
//            String token = jwtUtil.createToken(res.getId(), res.getUserName(), res.getPassword());
//            HashMap<String, Object> data = new HashMap<>();
//            data.put("token", token);
//            return Response.success(data);
//        }
//        return Response.failed(StatusCodeEnum.LOGIN_FAILED);
        return loginFactory.login(String.valueOf(vo.getLoginType())).login(vo);
    }

    /**
     * 注册
     *
     * @param username 用户名
     * @param password 密码
     * @return : java.util.Map<java.lang.String,java.lang.Object>成功返回：<pre>
     *     {@code
     *      {
     *         "message": "成功",
     *         "statusCode": 200
     *      }
     *     }
     * </pre>
     * <br>
     * @author : 李泽聿
     * @since : 2025-10-22 08:04
     */
    @Override
    public Map<String, Object> register(String username, String password) {
        String id = UUID.randomUUID().toString().replaceAll("-", "");
        int res = userMapper.register(id, username, password);
        if (res > 0) {
            return Response.success();
        }
        return Response.failed(StatusCodeEnum.REGISTER_FAILED);
    }

    /**
     * 从redis中获取用户行程信息
     *
     * @param token 用户token
     * @return : java.util.Map<java.lang.String,java.lang.Object>
     * @author : 李泽聿
     * @since : 2025-12-12 00:11
     */
    @Override
    public Object getTravelInfo(String token) {
        String id = jwtUtil.getUserInfo(token)[0].substring(0, 16);
        String key = "user:formated:".concat(id);
        // key=user:formated:f969248d621bcded
        Object obj = redisTemplate.opsForValue().get(key);
        if (obj != null) {
            log.info("从redis中获取用户行程信息成功");
            return obj;
        }
        return Response.failed(StatusCodeEnum.LOGIN_FAILED);
    }

    /**
     * 将用户行程缓存至redis中
     *
     * @author : 李泽聿
     * @since : 2025-12-12 15:07
     * @param token 用户token
     * @param obj 用户行程信息
     * @return : java.lang.Object
     */
    @Override
    public Map<String, Object> cacheTravelInfo(String token, Object obj) {
        String[] info = jwtUtil.getUserInfo(token);
        User res = userMapper.selectUserInfo(info[0]);
        String hash = DigestUtils.md5DigestAsHex(res.getPassword().getBytes()).substring(0, 6);

        if (jwtUtil.isExpiration(token)
                || res.getId() == null
                || !res.getUserName().equals(info[1])
                || !hash.equals(info[2])) {
            return Response.failed(StatusCodeEnum.LOGIN_FAILED);
        }

        String id = jwtUtil.getUserInfo(token)[0].substring(0, 16);
        String key = "user:formated:".concat(id);
        redisTemplate.opsForValue().set(key, obj,24, TimeUnit.HOURS);
        return Response.success();
    }
}
