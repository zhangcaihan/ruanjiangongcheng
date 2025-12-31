package com.zeyuli.strategy.login;

import com.zeyuli.pojo.vo.UserVo;

import java.util.Map;

/**
 * 登录策略接口<br>
 * 目前只有账密登录策略<br>
 * 希望不要在加了O_O
 *
 * @author 李泽聿
 * @since 2025-09-26 10:46
 */

public interface LoginStrategyInterface {
    Map<String, Object> login(UserVo vo);
}
