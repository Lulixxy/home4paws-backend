package org.itsci.home4paws.repository;

import org.itsci.home4paws.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User,String> {

    @Query("SELECT MAX(u.userId) FROM User u")
    String findMaxUserId();

    User findByUsername(String username);
}
