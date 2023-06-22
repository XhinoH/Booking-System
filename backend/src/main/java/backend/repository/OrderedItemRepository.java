package backend.repository;

import backend.model.OrderedItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderedItemRepository extends JpaRepository<OrderedItem, Integer> {

}
