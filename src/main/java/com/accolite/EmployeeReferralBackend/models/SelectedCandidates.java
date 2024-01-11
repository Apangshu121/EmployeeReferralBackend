package com.accolite.EmployeeReferralBackend.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@AllArgsConstructor
@Setter
@Getter
@Entity
@Table(name="Selected_Candidates")
public class SelectedCandidates{
    @Id
    int id;
    Date dateOfJoining;
    String designation;
    long bonus;
    int referredId;
    boolean present;
    String businessUnit;
}
