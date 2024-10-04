package vinci.ma.inventory.web.controllers.AdminController;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import vinci.ma.inventory.dao.entities.Admin;
import vinci.ma.inventory.dao.repositories.AdminRepo;

import java.util.Base64;

@Controller
public class InboxController {

    @Autowired
    private AdminRepo adminRepository;


    @GetMapping("/home/admin/inbox")
    public String showHomeInbox(Model model) {

        // Fetch the currently logged-in admin
        Authentication authenticationn = SecurityContextHolder.getContext().getAuthentication();
        String username = authenticationn.getName();
        Admin loggedInAdmin = adminRepository.findAdminByUsername(username);
        model.addAttribute("loggedInAdmin", loggedInAdmin);
        if (loggedInAdmin.getAdminPicture() != null) {
            String base1 = Base64.getEncoder().encodeToString(loggedInAdmin.getAdminPicture());
            model.addAttribute("adminPicture", "data:image/jpeg;base64," + base1);
        }
        return "inbox";
    }

    @GetMapping("/home/admin/inbox/mail")
    public String showSingleMail(Model model) {
        // Fetch the currently logged-in admin
        Authentication authenticationn = SecurityContextHolder.getContext().getAuthentication();
        String username = authenticationn.getName();
        Admin loggedInAdmin = adminRepository.findAdminByUsername(username);
        model.addAttribute("loggedInAdmin", loggedInAdmin);
        if (loggedInAdmin.getAdminPicture() != null) {
            String base1 = Base64.getEncoder().encodeToString(loggedInAdmin.getAdminPicture());
            model.addAttribute("adminPicture", "data:image/jpeg;base64," + base1);
        }

        return "singlemail";
    }

    @GetMapping("/home/admin/inbox/sender")
    public String showComposeMail(Model model) {
        // Fetch the currently logged-in admin
        Authentication authenticationn = SecurityContextHolder.getContext().getAuthentication();
        String username = authenticationn.getName();
        Admin loggedInAdmin = adminRepository.findAdminByUsername(username);
        model.addAttribute("loggedInAdmin", loggedInAdmin);
        if (loggedInAdmin.getAdminPicture() != null) {
            String base1 = Base64.getEncoder().encodeToString(loggedInAdmin.getAdminPicture());
            model.addAttribute("adminPicture", "data:image/jpeg;base64," + base1);
        }

        return "sender";
    }


}
