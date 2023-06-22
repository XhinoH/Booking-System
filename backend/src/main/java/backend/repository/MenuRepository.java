package backend.repository;

import backend.model.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MenuRepository extends JpaRepository<Menu, Integer> {
    public List<Menu> findAllByRestaurantIdAndIsDeleted(Integer id, Boolean isDeleted);
    public Optional<Menu> findByIdAndRestaurantIdAndIsDeleted(Integer menuId, Integer restaurantId, Boolean isDeleted);
    public Optional<Menu> findByIsActiveAndRestaurant_IdAndIsDeleted(Boolean isActive, Integer restaurantId, Boolean isDeleted);
    Optional<Menu> findByIdAndIsDeleted(Integer menuId, Boolean isDeleted);
}
