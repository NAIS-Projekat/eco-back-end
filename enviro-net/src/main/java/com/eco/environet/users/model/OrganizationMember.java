package com.eco.environet.users.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "memberBuilder")
@Table(name = "organization_members", schema = "users")
public class OrganizationMember extends User {

    @Column(name = "wage", nullable = false)
    private double wage = 18;

    @Column(name = "working_hours", nullable = false)
    private double workingHours = 8;

    @Column(name = "overtime_wage", nullable = false)
    private double overtimeWage = 20;

    public OrganizationMember(User user, double wage, double workingHours, double overtimeWage) {
        super(user.getId(), user.getName(), user.getSurname(), user.getEmail(), user.getUsername(), user.getPassword(),
                user.getPhoneNumber(), user.getDateOfBirth(), user.getGender(), user.getLastPasswordResetDate(),
                user.getRole(), user.isEnabled(), user.isActive());
        this.wage = wage;
        this.workingHours = workingHours;
        this.overtimeWage = overtimeWage;
    }
}
