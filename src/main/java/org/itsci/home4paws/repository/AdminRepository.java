package org.itsci.home4paws.repository;

import org.itsci.home4paws.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin,String> {
    Admin findByUsername(String username);
}
