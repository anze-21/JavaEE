package com.it.mapper;

import com.it.pojo.UserLog;

import java.util.List;
import java.util.Map;

public interface UserLogMapper {
     void save(UserLog userLog);


     List<UserLog> findByParam(Map<String,Object> param);

     Long countByParam(Map<String, Object> param);
}
