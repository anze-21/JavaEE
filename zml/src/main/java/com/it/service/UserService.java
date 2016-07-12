package com.it.service;
import com.google.common.collect.Maps;
import com.it.mapper.RoleMapper;
import com.it.mapper.UserLogMapper;
import com.it.mapper.UserMapper;
import javax.inject.Inject;
import javax.inject.Named;
@Named
public class UserService {

    @Inject
    private UserMapper userMapper;
    @Inject
    private UserLogMapper userLogMapper;
    @Inject
    private RoleMapper roleMapper;
}
