package com.it.mapper;

import com.it.pojo.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface UserMapper {

    User findByUsername(@Param("username") String username);

    void updateUser(User user);

    List<User> findByParam(Map<String, Object> params);

    Long count();

    Long countByParam(Map<String, Object> params);

    void save(User user);

    User findById(Integer id);
    List<User> findAll();
}
