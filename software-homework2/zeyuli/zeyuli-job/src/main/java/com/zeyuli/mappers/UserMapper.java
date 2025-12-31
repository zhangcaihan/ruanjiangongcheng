package com.zeyuli.mappers;


import com.zeyuli.pojo.User;
import org.apache.ibatis.annotations.Mapper;

/**
 *
 *
 * @author 李泽聿
 * @since 2025-10-21 17:00
 */
@Mapper
public interface UserMapper {
    User selectUserInfo(String id);

    User login(String userName, String password);

    int register(String id, String username, String password);

    int updateUserInfo(String id,String result);
}
