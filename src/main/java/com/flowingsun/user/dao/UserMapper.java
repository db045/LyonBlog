package com.flowingsun.user.dao;

import com.flowingsun.user.entity.User;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {

    Integer insertByUserRegister(User user);

    int insertSelective(User record);

    Long selectUseridByUserphone(String userphone);

    User selectByPrimaryKey(Long id);

    User selectByUserToken(User user);

    int updateByPrimaryKey(User record);

    int updateByPrimaryKeySelective(User record);

    Integer updateUserStatusByUserphone(@Param("userstatus") Integer userstatus, @Param("userphone") String userphone);

    Integer deleteByUserphone(String userphone);

    int deleteByPrimaryKey(Long id);

}