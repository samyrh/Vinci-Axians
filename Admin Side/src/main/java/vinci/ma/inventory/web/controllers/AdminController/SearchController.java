package vinci.ma.inventory.web.controllers.AdminController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import vinci.ma.inventory.services.managers.SearchManager;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/home")
public class SearchController {


    @Autowired
    private SearchManager searchManager;

    @GetMapping("/search")
    public String search(@RequestParam("query") String query, Model model) {
        Map<String, List<?>> results = searchManager.search(query);
        model.addAttribute("results", results);
        return "searchResult";
    }
}
