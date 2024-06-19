package com.eco.environet.users.repository;

import com.eco.environet.users.model.RegisteredUser;
import com.eco.environet.users.model.Role;
import com.eco.environet.users.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<RegisteredUser> findRegisteredUserById(Long id);
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    Page<User> findAll(Specification<User> specification, Pageable pageable);
    @Query("SELECT u FROM User u WHERE u.role IN ('ACCOUNTANT', 'BOARD_MEMBER', 'PROJECT_MANAGER', 'PROJECT_COORDINATOR', 'EDUCATOR')")
    List<User> findAllOrganizationMembers();
    @Query("SELECT u FROM User u WHERE u.role IN (:roles)")
    List<User> findAllUsersByRoles(@Param("roles") List<Role> roles);

}
