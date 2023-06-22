package backend.repository;

import backend.model.UserDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserDetailRepository extends JpaRepository<UserDetail, Integer> {

    public Optional<UserDetail> findByUserId(Integer id);
}
