package com.zeyuli.service;


import com.zeyuli.pojo.vo.UserVo;

import java.util.Map;

/**
 * UserService接口
 *
 * @author 李泽聿
 * @since 2025-10-21 16:58
 */

public interface UserService {
    Map<String, Object> login(UserVo vo);

    Map<String, Object> register(String username, String password);

    Object getTravelInfo(String token);

    Map<String, Object> cacheTravelInfo(String token, Object obj);
}
