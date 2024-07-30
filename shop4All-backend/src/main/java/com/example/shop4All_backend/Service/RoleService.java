package com.example.shop4All_backend.Service;

import com.example.shop4All_backend.Entity.Role;
import com.example.shop4All_backend.Repository.RoleRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService {

    @Autowired
    private RoleRepo roleRepository;

    public Role createNewRole(Role role) {
        return roleRepository.save(role);
    }

}
