package vinci.ma.inventory.services.managers;

import vinci.ma.inventory.dao.entities.Department;

import java.util.List;

public interface DepartmentManager
{

    List<Department> getAllDepartments();

    Department getDepartmentById(Long departmentId);

}
