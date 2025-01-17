package com.example.leave_management_system.repository;

import com.example.leave_management_system.model.Role;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepo extends CrudRepository<Role,Integer> {
}
