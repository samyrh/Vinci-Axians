package vinci.ma.inventory.web.controllers.AdminController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vinci.ma.inventory.dao.entities.Department;
import vinci.ma.inventory.dao.repositories.DepartmentRepo;
import vinci.ma.inventory.services.managers.NotificationManager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

@Controller
public class DepartmentController {

    @Autowired
    private DepartmentRepo departmentRepo;
    @Autowired
    private NotificationManager notificationManager;

    @GetMapping("/home/admin/users/delete/department/{id}")
    public String deleteDepartment(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        // Find the department by id
        Optional<Department> departmentOpt = departmentRepo.findById(id);

        if (departmentOpt.isPresent()) {
            Department department = departmentOpt.get();
            String departmentName = department.getName(); // Get the department name

            // Delete the department
            departmentRepo.deleteById(id);

            // Notify about the department deletion
            notificationManager.notifyAdminsAndAdminUsersAboutDepartmentDeletion(departmentName);

            redirectAttributes.addFlashAttribute("message", "Department deleted successfully!");
        } else {
            redirectAttributes.addFlashAttribute("error", "Department not found.");
        }

        return "redirect:/home/admin/users";
    }




    @PostMapping("/home/admin/users/add/department")
    public String addDepartment(@RequestParam("name") String name) {
        Department newDepartment = new Department();
        newDepartment.setName(name);
        departmentRepo.save(newDepartment);

        // Notify admins and users with role admin
        String timestamp = new SimpleDateFormat("yyyy/MM/dd HH:mm").format(new Date());
        notificationManager.notifyAdminsAndAdminUsersAboutDepartmentCreation(name, timestamp);

        return "redirect:/home/admin/users";
    }
}
