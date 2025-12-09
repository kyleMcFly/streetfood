package co2123.streetfood;

import co2123.streetfood.model.*;
import co2123.streetfood.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class StreetfoodApplication implements CommandLineRunner {

    // Inject the Repositories so we can save to the database
    @Autowired
    private VendorRepository vendorRepository;
    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private AwardRepository awardRepository;
    @Autowired
    private PhotoRepository photoRepository;
    @Autowired
    private ReviewRepository reviewRepository;

    public static void main(String[] args) {
        SpringApplication.run(StreetfoodApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        // -------------------------------------------------------------
        // 1. Create and Save Tags
        // -------------------------------------------------------------
        Tag spicy = new Tag(); spicy.setName("Spicy Challenge");
        Tag hiddenGem = new Tag(); hiddenGem.setName("Hidden Gem");
        Tag localLegend = new Tag(); localLegend.setName("Local Legend");
        Tag vegetarian = new Tag(); vegetarian.setName("Vegetarian");
        Tag sweet = new Tag(); sweet.setName("Sweet");
        Tag classic = new Tag(); classic.setName("Classic");

        // Save tags to DB (IDs are generated automatically)
        tagRepository.saveAll(Arrays.asList(spicy, hiddenGem, localLegend, vegetarian, sweet, classic));

        // -------------------------------------------------------------
        // VENDOR 1: Tasty Vendor
        // -------------------------------------------------------------
        VendorProfile profile1 = new VendorProfile();
        profile1.setBio("Serving the best street food since 2010.");
        profile1.setSocialMediaHandle("@tastyvendorfakewebsite");
        profile1.setWebsite("http://tastyvendor.fakewebsite.com");

        Vendor vendor1 = new Vendor();
        vendor1.setName("Tasty Vendor");
        vendor1.setLocation("University Road");
        vendor1.setCuisineType("Fusion");
        vendor1.setProfile(profile1); // Link profile

        Dish noodles = new Dish();
        noodles.setName("Fire Noodles");
        noodles.setDescription("Extremely spicy noodles for the brave.");
        noodles.setSpiceLevel(5);
        noodles.setPrice(8.99);
        noodles.setTags(new ArrayList<>(List.of(spicy))); // Link tag
        noodles.setVendor(vendor1); // Link vendor

        Dish dumplings = new Dish();
        dumplings.setName("Secret Dumplings");
        dumplings.setDescription("Delicate dumplings with a secret filling.");
        dumplings.setSpiceLevel(2);
        dumplings.setPrice(6.55);
        dumplings.setTags(new ArrayList<>(List.of(hiddenGem))); // Link tag
        dumplings.setVendor(vendor1); // Link vendor

        // Add dishes to vendor list
        vendor1.setDishes(new ArrayList<>(Arrays.asList(noodles, dumplings)));

        // Create Photos
        Photo photo1 = new Photo(); photo1.setUrl("noodles.jpg"); photo1.setDescription("A bowl of fire noodles."); photo1.setVendor(vendor1);
        Photo photo2 = new Photo(); photo2.setUrl("dumplings.jpg"); photo2.setDescription("Steaming hot dumplings."); photo2.setVendor(vendor1);
        vendor1.setPhotos(new ArrayList<>(Arrays.asList(photo1, photo2)));

        // SAVE VENDOR 1 (This cascades and saves the Profile, Dishes, and Photos automatically!)
        vendorRepository.save(vendor1);

        // Create Reviews for Vendor 1's dishes
        Review r1 = new Review(); r1.setReviewerName("Sofia"); r1.setRating(5); r1.setComment("So spicy!"); r1.setReviewDate(LocalDateTime.now()); r1.setDish(noodles);
        Review r2 = new Review(); r2.setReviewerName("Jamie"); r2.setRating(4); r2.setComment("Loved them!"); r2.setReviewDate(LocalDateTime.now()); r2.setDish(dumplings);
        reviewRepository.saveAll(Arrays.asList(r1, r2));

        // Create Awards for Vendor 1
        Award a1 = new Award(); a1.setTitle("Best Street Food 2024"); a1.setYear(2024); a1.setVendor(vendor1);
        Award a2 = new Award(); a2.setTitle("Customer Favorite"); a2.setYear(2023); a2.setVendor(vendor1);
        awardRepository.saveAll(Arrays.asList(a1, a2));

        // -------------------------------------------------------------
        // VENDOR 2: Nice Food
        // -------------------------------------------------------------
        VendorProfile profile2 = new VendorProfile();
        profile2.setBio("Family-run, celebrating local and global tastes.");
        profile2.setSocialMediaHandle("@nicefoodfakewebsite");
        profile2.setWebsite("http://nicefood.fakewebsite.com");

        Vendor vendor2 = new Vendor();
        vendor2.setName("Nice Food");
        vendor2.setLocation("Leicester Market");
        vendor2.setCuisineType("Fusion");
        vendor2.setProfile(profile2);

        Dish samosa = new Dish();
        samosa.setName("Spicy Samosa Chaat");
        samosa.setDescription("Crisp samosas topped with chickpeas.");
        samosa.setSpiceLevel(3);
        samosa.setPrice(4.99);
        samosa.setTags(new ArrayList<>(Arrays.asList(spicy, vegetarian)));
        samosa.setVendor(vendor2);

        Dish porkPie = new Dish();
        porkPie.setName("Melton Mowbray Pork Pie Bites");
        porkPie.setDescription("Mini versions of the classic pork pie.");
        porkPie.setSpiceLevel(1);
        porkPie.setPrice(2.99);
        porkPie.setTags(new ArrayList<>(List.of(localLegend)));
        porkPie.setVendor(vendor2);

        Dish toastie = new Dish();
        toastie.setName("Red Leicester Cheese Toastie");
        toastie.setDescription("Thick slices of Red Leicester cheese.");
        toastie.setSpiceLevel(1);
        toastie.setPrice(3.75);
        toastie.setTags(new ArrayList<>(List.of(vegetarian)));
        toastie.setVendor(vendor2);

        vendor2.setDishes(new ArrayList<>(Arrays.asList(samosa, porkPie, toastie)));

        Photo photo3 = new Photo(); photo3.setUrl("samosa.JPG"); photo3.setDescription("Samosa chaat."); photo3.setVendor(vendor2);
        Photo photo4 = new Photo(); photo4.setUrl("pies.jpg"); photo4.setDescription("Mini pies."); photo4.setVendor(vendor2);
        Photo photo5 = new Photo(); photo5.setUrl("toastie.jpg"); photo5.setDescription("Cheese toastie"); photo5.setVendor(vendor2);
        vendor2.setPhotos(new ArrayList<>(Arrays.asList(photo3, photo4, photo5)));

        // SAVE VENDOR 2
        vendorRepository.save(vendor2);

        Review r3 = new Review(); r3.setReviewerName("Tom"); r3.setRating(5); r3.setComment("Delicious!"); r3.setReviewDate(LocalDateTime.now()); r3.setDish(samosa);
        Review r4 = new Review(); r4.setReviewerName("Ayesha"); r4.setRating(4); r4.setComment("Perfect snack."); r4.setReviewDate(LocalDateTime.now()); r4.setDish(porkPie);
        Review r5 = new Review(); r5.setReviewerName("Pierre"); r5.setRating(5); r5.setComment("Loved it!"); r5.setReviewDate(LocalDateTime.now()); r5.setDish(toastie);
        reviewRepository.saveAll(Arrays.asList(r3, r4, r5));

        Award a3 = new Award(); a3.setTitle("Leicester Market Favourite"); a3.setYear(2025); a3.setVendor(vendor2);
        awardRepository.save(a3);

        // -------------------------------------------------------------
        // VENDOR 3: Bons Desserts
        // -------------------------------------------------------------
        VendorProfile profile3 = new VendorProfile();
        profile3.setBio("Delicate French desserts.");
        profile3.setSocialMediaHandle("@bonsdessertsfakewebsite");
        profile3.setWebsite("http://bonsdesserts.fakewebsite.com");

        Vendor vendor3 = new Vendor();
        vendor3.setName("Bons Desserts");
        vendor3.setLocation("New Walk");
        vendor3.setCuisineType("French Desserts");
        vendor3.setProfile(profile3);

        Dish eclair = new Dish();
        eclair.setName("Éclair au Chocolat");
        eclair.setDescription("Choux pastry filled with rich chocolate.");
        eclair.setSpiceLevel(0);
        eclair.setPrice(3.15);
        eclair.setTags(new ArrayList<>(Arrays.asList(sweet, classic)));
        eclair.setVendor(vendor3);

        Dish tarte = new Dish();
        tarte.setName("Tarte au Citron");
        tarte.setDescription("Tangy lemon tart.");
        tarte.setSpiceLevel(0);
        tarte.setPrice(3.85);
        tarte.setTags(new ArrayList<>(List.of(sweet)));
        tarte.setVendor(vendor3);

        Dish madeleine = new Dish();
        madeleine.setName("Madeleine");
        madeleine.setDescription("Soft sponge cakes.");
        madeleine.setSpiceLevel(0);
        madeleine.setPrice(1.45);
        madeleine.setTags(new ArrayList<>(List.of(classic)));
        madeleine.setVendor(vendor3);

        vendor3.setDishes(new ArrayList<>(Arrays.asList(eclair, tarte, madeleine)));

        Photo photo6 = new Photo(); photo6.setUrl("eclair.jpg"); photo6.setDescription("Chocolate éclair."); photo6.setVendor(vendor3);
        Photo photo7 = new Photo(); photo7.setUrl("tarte.jpg"); photo7.setDescription("Lemon tart."); photo7.setVendor(vendor3);
        vendor3.setPhotos(new ArrayList<>(Arrays.asList(photo6, photo7)));

        // SAVE VENDOR 3
        vendorRepository.save(vendor3);

        Review r6 = new Review(); r6.setReviewerName("Lucie"); r6.setRating(5); r6.setComment("Delicious!"); r6.setReviewDate(LocalDateTime.now()); r6.setDish(eclair);
        Review r7 = new Review(); r7.setReviewerName("Priya"); r7.setRating(4); r7.setComment("So zesty."); r7.setReviewDate(LocalDateTime.now()); r7.setDish(tarte);
        Review r8 = new Review(); r8.setReviewerName("Harriet"); r8.setRating(3); r8.setComment("Okay."); r8.setReviewDate(LocalDateTime.now()); r8.setDish(tarte);
        reviewRepository.saveAll(Arrays.asList(r6, r7, r8));

        Award a4 = new Award(); a4.setTitle("Best Dessert Stall"); a4.setYear(2025); a4.setVendor(vendor3);
        awardRepository.save(a4);
    }
}