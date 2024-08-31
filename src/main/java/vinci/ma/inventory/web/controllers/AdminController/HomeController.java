package vinci.ma.inventory.web.controllers.AdminController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {



    @GetMapping("/home/admin")
    public String showHomeAdmin() {


        return "home";
    }
}
