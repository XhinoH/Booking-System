package backend.repository;

import backend.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Integer> {

    List<Order> findAllByMenu_Restaurant_IdAndIsDeleted(Integer restaurantId, Boolean isDeleted);
    List<Order> findAllByMenu_Restaurant_IdAndOrderDetail_StatusAndIsDeleted(Integer restaurantId, String orderStatus, Boolean isDeleted);
    List<Order> findAllByUser_UsernameAndIsDeletedOrderByOrderDateDesc(String clientUsername, Boolean isDeleted);
    Optional<Order> findByIdAndMenu_Restaurant_IdAndIsDeleted(Integer orderId, Integer restaurantId, Boolean isDeleted);
    Optional<Order> findByIdAndUser_UsernameAndIsDeleted(Integer orderId, String clientUsername, Boolean isDeleted);
    Optional<Order> findByIdAndIsDeleted(Integer orderId, Boolean isDeleted);
}
