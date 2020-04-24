package org.iitbact.cc.repository;

import org.iitbact.cc.entities.AdminUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<AdminUser, Integer> {

    AdminUser findByEmailId(String emailId);

    AdminUser findByUserId(String userId);

}
