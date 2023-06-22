package backend.repository;

import backend.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Integer> {
    List<Item> findAllByMenu_IdAndIsDeleted(Integer id, Boolean isDeleted);
    Optional<Item> findByIdAndIsDeleted(Integer integer, Boolean isDeleted);
    Optional<Item> findByIdAndMenu_Restaurant_IdAndIsDeleted(Integer itemId, Integer restaurantId, Boolean isDeleted);


}
