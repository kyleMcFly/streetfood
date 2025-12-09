package co2123.streetfood.controller;

import co2123.streetfood.model.Vendor;
import co2123.streetfood.repository.VendorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class FilterController {

    @Autowired
    private VendorRepository vendorRepository;

    @GetMapping("/search1")
    public String search1(@RequestParam String vendor, Model model) {
        // Task 6.8: Use the repository to find vendors by name (partial match)
        List<Vendor> list = vendorRepository.findByNameContains(vendor);

        if(list.isEmpty()){
            // If no results, show all vendors (as per handout logic)
            model.addAttribute("vendors", vendorRepository.findAll());
        } else {
            model.addAttribute("vendors", list);
        }
        return "vendors";
    }

    @GetMapping("/search2")
    public String search2(@RequestParam String dish, Model model) {
        // Task 6.8: Use the repository to find vendors who have a dish with this name
        List<Vendor> list = vendorRepository.findByDishesNameContains(dish);

        if(list.isEmpty()){
            model.addAttribute("vendors", vendorRepository.findAll());
        } else {
            model.addAttribute("vendors", list);
        }
        return "vendors";
    }
}