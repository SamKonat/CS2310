/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.pitt.cs2310.ers.repository;

import edu.pitt.cs2310.ers.domain.EmergencyNotification;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface EmergencyNotificationRepository extends 
        CrudRepository<EmergencyNotification, Integer> {

    @Query("SELECT e FROM EmergencyNotification e WHERE e.status != 'resolved' "
        + "ORDER BY e.status, e.emergencyTime")
    List<EmergencyNotification> findActiveEmergencies();

    @Query("SELECT e FROM EmergencyNotification e WHERE e.status != 'resolved' " +
            "AND e.patient.patientId = ?1")
    EmergencyNotification findActiveEmenrgencyByPatientId(int patientId);

    @Query("SELECT e FROM EmergencyNotification e "
        + "WHERE e.status = 'resolved' AND e.patient.patientId = ?1 "
        + "ORDER BY e.emergencyTime DESC")
    List<EmergencyNotification> findEmergencyHistoryByPatientId(int patientId);
}
