package com.eco.environet.users.repository;

import com.eco.environet.users.model.RegisteredUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegisteredUserRepository extends JpaRepository<RegisteredUser, Long> {

}
