package com.example.community.service;

import com.example.community.model.Permission;
import com.example.community.model.Role;
import com.example.community.model.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {

    User queryUserByUsername(String username);

    List<Permission> queryPermissionByUsername(String username);

    Role queryRoleByUsername(String username);

    int addUser(User user);

}
