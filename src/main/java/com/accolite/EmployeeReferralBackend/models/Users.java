package com.accolite.EmployeeReferralBackend.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "Users")
public class Users{
    @Id
    int id;
    String email;
    Role role=Role.EMPLOYEE;
    long totalBonus;
}
