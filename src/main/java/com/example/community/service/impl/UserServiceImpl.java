package com.example.community.service.impl;

import com.example.community.mapper.PermissionMapper;
import com.example.community.mapper.RoleMapper;
import com.example.community.mapper.RolePermissionMapper;
import com.example.community.mapper.UserMapper;
import com.example.community.model.*;
import com.example.community.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author chy
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PermissionMapper permissionMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private RolePermissionMapper rolePermissionMapper;

    @Override
    public User queryUserByUsername(String username) {
        UserExample userExample = new UserExample();
        userExample.createCriteria().andUsernameEqualTo(username);
        List<User> users = userMapper.selectByExample(userExample);
        return users.get(0);
    }

    @Override
    public List<Permission> queryPermissionByUsername(String username) {
        // get user
        UserExample userExample = new UserExample();
        userExample.createCriteria().andUsernameEqualTo(username);
        List<User> users = userMapper.selectByExample(userExample);
        User user = users.get(0);

        // get permission_id through role_id from user
        RolePermissionExample rolePermissionExample = new RolePermissionExample();
        rolePermissionExample.createCriteria().andRoleIdEqualTo(user.getRoleId());
        List<RolePermission> rolePermissions = rolePermissionMapper.selectByExample(rolePermissionExample);

        List<Permission> permissions = new ArrayList<>();
        for (RolePermission rolePermission : rolePermissions) {
            permissions.add(permissionMapper.selectByPrimaryKey(rolePermission.getPermissionId()));
        }

        return permissions;
    }

    @Override
    public Role queryRoleByUsername(String username) {
        UserExample userExample = new UserExample();
        userExample.createCriteria().andUsernameEqualTo(username);
        List<User> users = userMapper.selectByExample(userExample);
        User user = users.get(0);
        return roleMapper.selectByPrimaryKey(user.getId());
    }

    @Override
    public int addUser(User user) {
        User retUser = userMapper.selectByPrimaryKey(user.getId());

        if (retUser == null) {
            return userMapper.insert(user);
        }
        System.out.println("Register failed! User exists!");
        return 0;
    }
}
