package simen.order.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import simen.order.user.model.User;

@Repository
public interface UserRepo extends JpaRepository<User, String> {
}