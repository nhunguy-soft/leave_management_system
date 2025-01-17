package com.example.leave_management_system.repository;

import com.example.leave_management_system.model.Employee;
import jakarta.transaction.Transactional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
@Transactional
public interface EmployeeRepository extends CrudRepository<Employee,Integer> {
}
