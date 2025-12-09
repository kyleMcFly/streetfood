package co2123.streetfood.repository;

import co2123.streetfood.model.Vendor;
import co2123.streetfood.model.VendorProfile;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface VendorRepository extends CrudRepository<Vendor,Integer> {
    List<Vendor> findByName(String name);
    List<Vendor> findByNameContains(String name);
    List<Vendor> findByDishesNameContains(String name);
}
