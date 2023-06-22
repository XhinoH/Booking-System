package backend.repository;

import backend.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RestaurantRepository extends JpaRepository<Restaurant, Integer> {
    Optional<Restaurant> findByManagerId(Integer id);
    Optional<Restaurant> findByIdAndIsDeleted(Integer id, Boolean isDeleted);

    List<Restaurant> findAllByIsDeleted(Boolean isDeleted);
}
