package com.eco.environet.users.model;

import java.util.Arrays;
import java.util.List;

public enum Role {
    ADMINISTRATOR,
    REGISTERED_USER,
    ACCOUNTANT,
    BOARD_MEMBER,
    PROJECT_MANAGER,
    PROJECT_COORDINATOR,
    EDUCATOR;

    public boolean isOrganizationMember() {
        List<Role> organizationRoles = Arrays.asList(ACCOUNTANT, BOARD_MEMBER, PROJECT_MANAGER, PROJECT_COORDINATOR, EDUCATOR);
        return organizationRoles.contains(this);
    }

    public static List<Role> getAllOrganizationRoles() {
        return Arrays.asList(ACCOUNTANT, BOARD_MEMBER, PROJECT_MANAGER, PROJECT_COORDINATOR, EDUCATOR);
    }

    @Override
    public String toString() {
        switch (this) {
            case ADMINISTRATOR:
                return "Administrator";
            case REGISTERED_USER:
                return "Registered User";
            case ACCOUNTANT:
                return "Accountant";
            case BOARD_MEMBER:
                return "Board Member";
            case PROJECT_MANAGER:
                return "Project Manager";
            case PROJECT_COORDINATOR:
                return "Project Coordinator";
            case EDUCATOR:
                return "Educator";
            default:
                return super.toString();
        }
    }
}
