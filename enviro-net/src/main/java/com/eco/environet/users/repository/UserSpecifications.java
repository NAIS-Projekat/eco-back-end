package com.eco.environet.users.repository;

import com.eco.environet.users.model.Role;
import com.eco.environet.users.model.User;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class UserSpecifications {

    private UserSpecifications() {}

    public static Specification<User> nameLike(String name) {
        return (root, query, builder) ->
                builder.like(builder.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    public static Specification<User> surnameLike(String surname) {
        return (root, query, builder) ->
                builder.like(builder.lower(root.get("surname")), "%" + surname.toLowerCase() + "%");
    }

    public static Specification<User> emailLike(String email) {
        return (root, query, builder) ->
                builder.like(builder.lower(root.get("email")), "%" + email.toLowerCase() + "%");
    }

    public static Specification<User> rolesIn(List<Role> roles) {
        return (root, query, criteriaBuilder) -> root.get("role").in(roles);
    }
}
