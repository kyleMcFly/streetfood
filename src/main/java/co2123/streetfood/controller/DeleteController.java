package co2123.streetfood.controller;

import co2123.streetfood.model.*;
import co2123.streetfood.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class DeleteController {

    @Autowired
    private VendorRepository vendorRepository;
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private PhotoRepository photoRepository;
    @Autowired
    private AwardRepository awardRepository;

    @RequestMapping("/deleteVendor")
    public String deleteVendor(@RequestParam("id") Integer id) {
        vendorRepository.deleteById(id);
        return "redirect:/admin";
    }

    @RequestMapping("/deleteDish")
    public String deleteDish(@RequestParam Integer vendorid, @RequestParam Integer dishid) {
        // Find the vendor first
        Vendor foundVendor = vendorRepository.findById(vendorid).orElse(null);

        if (foundVendor != null) {
            // Loop to find the specific dish
            Dish dishToDelete = null;
            for(Dish d : foundVendor.getDishes()) {
                if(d.getId() == dishid) {
                    dishToDelete = d;
                    break;
                }
            }

            // If we found it, remove it from the list
            if(dishToDelete != null) {
                foundVendor.getDishes().remove(dishToDelete);
                // Saving the vendor updates the list in the database
                vendorRepository.save(foundVendor);
            }
        }
        return "redirect:/vendor?id=" + vendorid;
    }

    @RequestMapping("/deleteReview")
    public String deleteReview(@RequestParam Integer vendorId, @RequestParam Integer reviewId) {
        // Simply delete by ID using the repository
        reviewRepository.deleteById(reviewId);
        return "redirect:/vendor?id=" + vendorId;
    }

    @RequestMapping("/deletePhoto")
    public String deletePhoto(@RequestParam Integer photoId) {
        // Find the photo so we know which vendor page to go back to
        Photo foundPhoto = photoRepository.findById(photoId).orElse(null);

        if (foundPhoto != null) {
            int vendorId = foundPhoto.getVendor().getId();
            photoRepository.delete(foundPhoto);
            return "redirect:/vendor?id=" + vendorId;
        }

        return "redirect:/admin";
    }

    @RequestMapping("/deleteAward")
    public String deleteAward(@RequestParam Integer awardId) {
        // Find the award so we know which vendor page to go back to
        Award foundAward = awardRepository.findById(awardId).orElse(null);

        if (foundAward != null) {
            int vendorId = foundAward.getVendor().getId();
            awardRepository.delete(foundAward);
            return "redirect:/vendor?id=" + vendorId;
        }

        return "redirect:/admin";
    }
}