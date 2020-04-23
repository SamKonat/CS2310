package edu.pitt.cs2310.ers.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EmergencyInfo {

    private int emergencyId;

    private int patientId;

    private String patientName;

    private String status;

    private String emergencyTime;

    private int notificationCount;

    private String token;

    private String address;

    private String gender;

    private Integer age;

    private double latitude;

    private double longitude;

    private String remarks;
}
