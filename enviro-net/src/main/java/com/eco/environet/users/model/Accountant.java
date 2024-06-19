package com.eco.environet.users.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "accountantBuilder")
@Table(name="accountants", schema = "users")
public class Accountant extends User {

    @Column(name = "wage", nullable = false)
    private double wage;

    @Column(name = "working_hours", nullable = false)
    private double workingHours;

    @Column(name = "overtime_wage", nullable = false)
    private double overtimeWage;

    public Accountant(Long id, String name, String surname, String email, String username, String password, String phoneNumber,
                      Timestamp dateOfBirth, Gender gender, Timestamp lastPasswordResetDate, Role role, boolean enabled, boolean active,
                      double wage, double workingHours, double overtimeWage) {
        super(id, name, surname, email, username, password, phoneNumber, dateOfBirth, gender, lastPasswordResetDate, role, enabled, active);
        this.wage = wage;
        this.workingHours = workingHours;
        this.overtimeWage = overtimeWage;
    }
    public Accountant(User user, double wage, double workingHours, double overtimeWage){
        super(user.getId(), user.getName(), user.getSurname(), user.getEmail(), user.getUsername(), user.getPassword(), user.getPhoneNumber(), user.getDateOfBirth(), user.getGender(), user.getLastPasswordResetDate(), user.getRole(), user.isEnabled(), user.isActive());
        this.wage = wage;
        this.workingHours = workingHours;
        this.overtimeWage = overtimeWage;
    }
}
