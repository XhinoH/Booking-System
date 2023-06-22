package backend.repository;

import backend.model.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Integer> {
    public Optional<OrderDetail> findByOrderId(Integer id);
}
