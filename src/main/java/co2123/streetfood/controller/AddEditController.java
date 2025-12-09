package co2123.streetfood.controller;

import co2123.streetfood.model.*;
import co2123.streetfood.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
public class AddEditController {

    // Injecting the repositories we created in Task 3
    @Autowired
    private VendorRepository vendorRepository;
    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private PhotoRepository photoRepository;
    @Autowired
    private AwardRepository awardRepository;

    // NOTE: The 'findVendor' method was removed here because Task 6.2 says to use repository.findById() instead.

    @RequestMapping("editVendor")
    public String editVendorForm(@RequestParam Integer id, Model model) {
        // OLD: Vendor foundVendor = findVendor(id);
        // NEW: Use repository to find by ID from the database
        Vendor foundVendor = vendorRepository.findById(id).orElse(null);

        if(foundVendor == null) {
            return "redirect:/admin";
        }
        model.addAttribute("vendor", foundVendor);
        return "forms/editVendor";
    }

    @RequestMapping("editedVendor")
    public String submittedEditForm(@RequestParam Integer id, @ModelAttribute Vendor vendor, Model model) {
        Vendor foundVendor = vendorRepository.findById(id).orElse(null);
        if(foundVendor == null) return "redirect:/admin";

        // Update the fields
        foundVendor.setName(vendor.getName());
        foundVendor.setLocation(vendor.getLocation());
        foundVendor.setCuisineType(vendor.getCuisineType());

        // NEW: Save the changes to the database
        vendorRepository.save(foundVendor);

        model.addAttribute("vendor", foundVendor);
        return "redirect:/vendor?id=" + id;
    }

    @RequestMapping("editVendorProfile")
    public String editVendorProfileForm(@RequestParam Integer id, Model model) {
        Vendor foundVendor = vendorRepository.findById(id).orElse(null);
        if(foundVendor == null) return "redirect:/admin";

        model.addAttribute("profile", foundVendor.getProfile());
        model.addAttribute("vendor", id);
        return "forms/editVendorProfile";
    }

    @RequestMapping("editedVendorProfile")
    public String submittedProfileEditForm(@RequestParam Integer id, @ModelAttribute VendorProfile profile, Model model) {
        Vendor foundVendor = vendorRepository.findById(id).orElse(null);
        if(foundVendor == null) return "redirect:/admin";

        if(foundVendor.getProfile() == null){
            // Task 5: We DO NOT call profile.setId(...) anymore. Database does it.
            foundVendor.setProfile(profile);
        } else {
            // Update existing profile
            foundVendor.getProfile().setBio(profile.getBio());
            foundVendor.getProfile().setSocialMediaHandle(profile.getSocialMediaHandle());
            foundVendor.getProfile().setWebsite(profile.getWebsite());
        }

        // Save the vendor (CascadeType.ALL ensures the profile is saved too)
        vendorRepository.save(foundVendor);

        model.addAttribute("vendor", foundVendor);
        return "redirect:/vendor?id=" + id;
    }

    @RequestMapping("newDish")
    public String newDishForm(@RequestParam Integer id, Model model) {
        Vendor foundVendor = vendorRepository.findById(id).orElse(null);
        if(foundVendor == null) return "redirect:/admin";

        model.addAttribute("vendor", foundVendor);
        model.addAttribute("dish", new Dish());
        // Load all available tags from the database for the checkbox list
        model.addAttribute("tags", tagRepository.findAll());
        return "forms/newDish";
    }

    @RequestMapping("addDish")
    public String addDish(@RequestParam Integer vendorid, @RequestParam List<Integer> tagIds, @ModelAttribute Dish dish, Model model) {
        Vendor foundVendor = vendorRepository.findById(vendorid).orElse(null);
        if(foundVendor == null) return "redirect:/admin";

        dish.setTags(new ArrayList<>());

        // Task 6.5 Hint: Use findAllById to get tags based on the IDs selected in the form
        Iterable<Tag> tags = tagRepository.findAllById(tagIds);
        for(Tag t : tags) {
            dish.getTags().add(t);
        }

        dish.setReviews(new ArrayList<>());
        dish.setVendor(foundVendor);

        // Task 6.5: "You don't need a dish repository". We add it to the vendor and save the vendor.
        foundVendor.getDishes().add(dish);
        vendorRepository.save(foundVendor);

        model.addAttribute("vendor", foundVendor);
        return "redirect:/vendor?id=" + vendorid;
    }

    @RequestMapping("newReview")
    public String newReview(@RequestParam Integer vendorid, @RequestParam Integer dishid, Model model) {
        Vendor foundVendor = vendorRepository.findById(vendorid).orElse(null);
        if(foundVendor == null) return "redirect:/admin";

        model.addAttribute("vendor", foundVendor);
        model.addAttribute("dishid",dishid);
        model.addAttribute("review", new Review());
        return "forms/newReview";
    }

    @RequestMapping("addReview")
    public String addReview(@RequestParam Integer vendorid, @RequestParam Integer dishid, @ModelAttribute Review review, Model model) {
        Vendor foundVendor = vendorRepository.findById(vendorid).orElse(null);
        if(foundVendor == null) return "redirect:/admin";

        // Find the correct dish to attach the review to
        Dish foundDish = null;
        for (Dish dish : foundVendor.getDishes()) {
            if (dish.getId() == dishid) {
                foundDish = dish;
                break;
            }
        }
        if (foundDish == null) return "redirect:/admin";

        review.setReviewDate(LocalDateTime.now());
        review.setDish(foundDish);

        // Task 6.5: Save the review using its repository
        reviewRepository.save(review);

        model.addAttribute("vendor", foundVendor);
        return "redirect:/vendor?id=" + vendorid;
    }

    @RequestMapping("newPhoto")
    public String newPhoto(@RequestParam Integer vendorid, Model model) {
        Vendor foundVendor = vendorRepository.findById(vendorid).orElse(null);
        if(foundVendor == null) return "redirect:/admin";

        model.addAttribute("vendor", foundVendor);
        model.addAttribute("photo", new Photo());
        return "forms/newPhoto";
    }

    @RequestMapping("addPhoto")
    public String addPhoto(@RequestParam Integer vendorid, @ModelAttribute Photo photo, Model model) {
        Vendor foundVendor = vendorRepository.findById(vendorid).orElse(null);
        if(foundVendor == null) return "redirect:/admin";

        photo.setVendor(foundVendor);
        // Task 6.4: Save using photo repository
        photoRepository.save(photo);

        model.addAttribute("vendor", foundVendor);
        return "redirect:/vendor?id=" + vendorid;
    }

    @RequestMapping("newAward")
    public String newAward(@RequestParam Integer vendorid, Model model) {
        Vendor foundVendor = vendorRepository.findById(vendorid).orElse(null);
        if(foundVendor == null) return "redirect:/admin";

        model.addAttribute("vendor", foundVendor);
        model.addAttribute("award", new Award());
        return "forms/newAward";
    }

    @RequestMapping("addAward")
    public String addAward(@RequestParam Integer vendorid, @ModelAttribute Award award, Model model) {
        Vendor foundVendor = vendorRepository.findById(vendorid).orElse(null);
        if(foundVendor == null) return "redirect:/admin";

        award.setVendor(foundVendor);
        // Task 6.4: Save using award repository
        awardRepository.save(award);

        model.addAttribute("vendor", foundVendor);
        return "redirect:/vendor?id=" + vendorid;
    }

    @RequestMapping("editDish")
    public String editDishForm(@RequestParam Integer vendorid, @RequestParam Integer dishid, Model model) {
        Vendor foundVendor = vendorRepository.findById(vendorid).orElse(null);
        if(foundVendor == null) return "redirect:/admin";

        Dish foundDish = null;
        for (Dish dish : foundVendor.getDishes()) {
            if (dish.getId() == dishid) {
                foundDish = dish;
                break;
            }
        }
        if(foundDish == null) return "redirect:/admin";

        model.addAttribute("vendor", foundVendor);
        model.addAttribute("dish", foundDish);
        model.addAttribute("tags", tagRepository.findAll());
        return "forms/editDish";
    }

    @RequestMapping("editedDish")
    public String submittedEditDishForm(@RequestParam Integer vendorid, @RequestParam Integer dishid, @RequestParam(required = false) List<Integer> tagIds, @ModelAttribute Dish dish, Model model) {
        Vendor foundVendor = vendorRepository.findById(vendorid).orElse(null);
        if(foundVendor == null) return "redirect:/admin";

        // Find the dish to update
        Dish foundDish = null;
        for (Dish d : foundVendor.getDishes()) {
            if (d.getId() == dishid) {
                foundDish = d;
                break;
            }
        }
        if(foundDish == null) return "redirect:/admin";

        // Update fields
        foundDish.setName(dish.getName());
        foundDish.setPrice(dish.getPrice());
        foundDish.setDescription(dish.getDescription());
        foundDish.setSpiceLevel(dish.getSpiceLevel());

        // Update tags
        foundDish.getTags().clear();
        if(tagIds != null) {
            Iterable<Tag> tags = tagRepository.findAllById(tagIds);
            for(Tag t : tags) {
                foundDish.getTags().add(t);
            }
        }

        // Save the changes (Cascades to dish)
        vendorRepository.save(foundVendor);

        model.addAttribute("vendor", foundVendor);
        return "redirect:/vendor?id=" + vendorid;
    }

    @RequestMapping("editReview")
    public String editReview(@RequestParam Integer vendorId, @RequestParam Integer reviewId, Model model) {
        Review foundReview = reviewRepository.findById(reviewId).orElse(null);
        Vendor foundVendor = vendorRepository.findById(vendorId).orElse(null);

        if(foundReview == null || foundVendor == null) return "redirect:/admin";

        model.addAttribute("vendor", foundVendor);
        model.addAttribute("review", foundReview);
        return "forms/editReview";
    }

    @RequestMapping("editedReview")
    public String editedReview(@RequestParam Integer vendorId, @RequestParam Integer reviewId, @ModelAttribute Review review, Model model) {
        Review foundReview = reviewRepository.findById(reviewId).orElse(null);
        if(foundReview == null) return "redirect:/admin";

        foundReview.setReviewerName(review.getReviewerName());
        foundReview.setComment(review.getComment());
        foundReview.setRating(review.getRating());

        reviewRepository.save(foundReview);

        return "redirect:/vendor?id=" + vendorId;
    }

    @RequestMapping("editPhoto")
    public String editPhoto(@RequestParam Integer photoId, Model model) {
        Photo foundPhoto = photoRepository.findById(photoId).orElse(null);
        if(foundPhoto == null) return "redirect:/admin";

        model.addAttribute("photo", foundPhoto);
        return "forms/editPhoto";
    }

    @RequestMapping("editedPhoto")
    public String editedPhoto(@RequestParam Integer photoId, @ModelAttribute Photo photo, Model model) {
        Photo foundPhoto = photoRepository.findById(photoId).orElse(null);
        if(foundPhoto == null) return "redirect:/admin";

        foundPhoto.setDescription(photo.getDescription());
        foundPhoto.setUrl(photo.getUrl());

        photoRepository.save(foundPhoto);

        return "redirect:/vendor?id=" + foundPhoto.getVendor().getId();
    }

    @RequestMapping("editAward")
    public String editAward(@RequestParam Integer awardId, Model model) {
        Award foundAward = awardRepository.findById(awardId).orElse(null);
        if(foundAward == null) return "redirect:/admin";

        model.addAttribute("award", foundAward);
        return "forms/editAward";
    }

    @RequestMapping("editedAward")
    public String editedAward(@RequestParam Integer awardId, @ModelAttribute Award award, Model model) {
        Award foundAward = awardRepository.findById(awardId).orElse(null);
        if(foundAward == null) return "redirect:/admin";

        foundAward.setTitle(award.getTitle());
        foundAward.setYear(award.getYear());

        awardRepository.save(foundAward);

        return "redirect:/vendor?id=" + foundAward.getVendor().getId();
    }
}