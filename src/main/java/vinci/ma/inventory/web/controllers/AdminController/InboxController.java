package vinci.ma.inventory.web.controllers.AdminController;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class InboxController {


    @GetMapping("/home/admin/inbox")
    public String showHomeInbox() {


        return "inbox";
    }

    @GetMapping("/home/admin/inbox/mail")
    public String showSingleMail() {


        return "singlemail";
    }

    @GetMapping("/home/admin/inbox/sender")
    public String showComposeMail() {


        return "sender";
    }


}
