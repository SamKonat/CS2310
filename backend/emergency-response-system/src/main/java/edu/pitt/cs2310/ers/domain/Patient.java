
package edu.pitt.cs2310.ers.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "patient")
public class Patient {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "patient_id")
    private Integer patientId;
    
    @Column(name = "patient_name")
    private String name;
    
    @Column(name = "patient_token")
    private String token;

    @Column(name = "patient_address")
    private String address;

    @Column(name = "patient_gender")
    private String gender;

    @Column(name = "patient_age")
    private Integer age;
}
