package io.ssosso.springsecuritypractice1.service;


import io.ssosso.springsecuritypractice1.domain.entity.Role;

import java.util.List;

public interface RoleService {

    Role getRole(long id);

    List<Role> getRoles();

    void createRole(Role role);

    void deleteRole(long id);
}