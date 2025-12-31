package com.zeyuli.controller;


import com.zeyuli.enm.StatusCodeEnum;
import com.zeyuli.pojo.vo.RegisterVo;
import com.zeyuli.pojo.vo.UserVo;
import com.zeyuli.service.impl.UserServiceImpl;
import com.zeyuli.util.Response;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 用户控制层
 *
 * @author 李泽聿
 * @since 2025-10-21 16:43
 */
@RestController
@RequestMapping("/user")
// todo 仅测试用
@CrossOrigin
@Slf4j
public class UserController {
    @Autowired
    private UserServiceImpl userService;

    @ApiOperation(value = "用户登录", notes = "用户登录接口")
    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody UserVo vo) {
        return userService.login(vo);
    }

    @ApiOperation(value = "用户注册", notes = "用户注册接口")
    @PostMapping("/register")
    public Map<String, Object> register(@RequestBody RegisterVo vo) {
        return userService.register(vo.getUserName(), vo.getPassword());
    }

    @ApiOperation(value = "获取用户行程信息", notes = "获取用户格式化后的行程信息")
    @GetMapping("/getTravelInfo")
    public Object getTravelInfo(@RequestParam("token") String token) {
        if (token == null) {
            log.error("token is null");
            return Response.failed(StatusCodeEnum.LOGIN_FAILED);
        }
        return userService.getTravelInfo(token);
    }

    @ApiOperation(value = "缓存用户行程信息", notes = "缓存用户格式化后的行程信息")
    @PostMapping("/cacheTravelInfo")
    public Map<String, Object> cacheTravelInfo(@RequestParam("token") String token, @RequestBody Object obj) {
        return userService.cacheTravelInfo(token, obj);
    }

}
