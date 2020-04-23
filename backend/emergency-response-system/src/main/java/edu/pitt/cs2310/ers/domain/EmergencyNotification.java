
package edu.pitt.cs2310.ers.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "emergency_notification")
public class EmergencyNotification {

    public static final String EMERGENCY_STATUS = "emergency";
    public static final String RECEIVED_STATUS = "received";
    public static final String RESOLVED_STATUS = "resolved";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "emergency_id")
    private int emergencyId;

    @ManyToOne
    @JoinColumn(name = "patient_id", referencedColumnName = "patient_id")
    private Patient patient;

    @Column(name = "latitude")
    private double latitude;

    @Column(name = "longitude")
    private double longitude;

    @Column(name = "status")
    private String status;

    @Column(name = "emergency_time")
    private Timestamp emergencyTime;

    @Column(name = "notification_count")
    private int notificationCount;

    @Column(name = "remarks")
    private String remarks;
}
