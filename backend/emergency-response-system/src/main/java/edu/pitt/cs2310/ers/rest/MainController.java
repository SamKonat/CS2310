
package edu.pitt.cs2310.ers.rest;

import edu.pitt.cs2310.ers.domain.EmergencyInfo;
import edu.pitt.cs2310.ers.domain.EmergencyNotification;
import edu.pitt.cs2310.ers.domain.Patient;
import edu.pitt.cs2310.ers.repository.EmergencyNotificationRepository;
import edu.pitt.cs2310.ers.repository.PatientRepository;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.swing.plaf.SeparatorUI;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class MainController {

    @Autowired
    private PatientRepository patientRepo;
    
    @Autowired
    private EmergencyNotificationRepository emRepo;

    private SimpleDateFormat df;
    
    public MainController() {
        df = new SimpleDateFormat("HH:mm:ss MM/dd/yyyy");
    }
    
    // API that handles incoming emergency notifications.
    // It identifies the patient based on the token specified by the request
    @PostMapping("/emergency/patient/{token}")
    public SuccessStatus registerEmergency(
            @PathVariable("token") String token,
            @RequestParam("latitude") double latitude,
            @RequestParam("longitude") double longitude
    ) {
        System.out.println("Emergency notification recieved for " + token);
        Patient patient = patientRepo.findByToken(token);
        // Creates an entry in the database
        EmergencyNotification notif = emRepo.findActiveEmenrgencyByPatientId(patient.getPatientId());
        if (notif == null) {
            notif = new EmergencyNotification();
            notif.setPatient(patient);
            notif.setNotificationCount(0);
            notif.setEmergencyTime(new Timestamp(System.currentTimeMillis()));
        }
        notif.setLatitude(latitude);
        notif.setLongitude(longitude);
        notif.setNotificationCount(notif.getNotificationCount() + 1);
        if (notif.getStatus() == null)
            notif.setStatus(EmergencyNotification.EMERGENCY_STATUS);
        emRepo.save(notif);
        return SuccessStatus.INSTANCE;
    }
    
    // API that handles requests from front-end regarding existing emergency situations
    @GetMapping("/emergency/notifications")
    public List<EmergencyInfo> getEmergencyPatients() {

        List<EmergencyInfo> emergencyInfos = new ArrayList<>();
        List<EmergencyNotification> notifs = emRepo.findActiveEmergencies();
        for (EmergencyNotification en : notifs) {
            EmergencyInfo info = new EmergencyInfo();
            info.setPatientId(en.getPatient().getPatientId());
            info.setEmergencyId(en.getEmergencyId());
            info.setPatientName(en.getPatient().getName());
            info.setStatus(en.getStatus());
            info.setNotificationCount(en.getNotificationCount());
            info.setEmergencyTime(df.format(en.getEmergencyTime()));

            emergencyInfos.add(info);
        }

        return emergencyInfos;
    }

    @GetMapping("/emergency/{emergencyId}/details")
    public EmergencyInfo getEmergencyDetails(
            @PathVariable("emergencyId") int emergencyId
    ) {
        EmergencyNotification notif = emRepo.findById(emergencyId).get();
        Patient patient = notif.getPatient();
        EmergencyInfo info = new EmergencyInfo();
        info.setEmergencyId(emergencyId);
        info.setPatientId(patient.getPatientId());
        info.setPatientName(patient.getName());
        info.setStatus(notif.getStatus());
        info.setGender(patient.getGender());
        info.setNotificationCount(notif.getNotificationCount());
        info.setAge(patient.getAge());
        info.setToken(patient.getToken());
        info.setAddress(patient.getAddress());
        info.setLatitude(notif.getLatitude());
        info.setLongitude(notif.getLongitude());
        info.setEmergencyTime(df.format(notif.getEmergencyTime()));

        return info;
    }

    @GetMapping("/patient/{patientId}/history")
    public List<EmergencyInfo> getPatientEmergencyHistory(
            @PathVariable("patientId") int patientId
    ) {
        List<EmergencyNotification> emergencyHistory = emRepo.findEmergencyHistoryByPatientId(patientId);
        List<EmergencyInfo> result = new ArrayList<>();
        emergencyHistory.forEach(x -> {
            EmergencyInfo info = new EmergencyInfo();
            info.setEmergencyTime(df.format(x.getEmergencyTime()));
            info.setNotificationCount(x.getNotificationCount());
            info.setRemarks(x.getRemarks());
            result.add(info);
        });
        return result;
    }

    @GetMapping("/patients")
    public Iterable<Patient> getPatients() {
        return patientRepo.findAll();
    }

    @PostMapping("/patient")
    public SuccessStatus createPatient(
            @RequestBody Patient patient
    ) {
        if (patient.getName() == null || patient.getToken() == null || patient.getAddress() == null
                || patient.getAge() == null) {
            throw new ApiFailedException("Please specify all details of the patient.");
        }
        Patient existing = patientRepo.findByToken(patient.getToken());
        if (existing != null) {
            throw new ApiFailedException("Token specified for the new patient already exists.");
        }

        patientRepo.save(patient);
        return SuccessStatus.INSTANCE;
    }

    @PutMapping("/patient/{patientId}")
    public SuccessStatus editPatient(
            @PathVariable("patientId") int patientId,
            @RequestBody Patient patient
    ) {
        if (patient.getName() == null || patient.getToken() == null || patient.getAddress() == null
                || patient.getAge() == null) {
            throw new ApiFailedException("Please specify all details of the patient.");
        }
        Patient other = patientRepo.findByToken(patient.getToken());
        if (other != null && !other.getPatientId().equals(patientId)) {
            throw new ApiFailedException("New token specified for the patient is already assigned to another patient.");
        }

        Patient existing = patientRepo.findById(patientId).get();
        existing.setAddress(patient.getAddress());
        existing.setAge(patient.getAge());
        existing.setGender(patient.getGender());
        existing.setName(patient.getName());
        existing.setToken(patient.getToken());

        patientRepo.save(existing);

        return SuccessStatus.INSTANCE;
    }

    @PutMapping("/emergency/{emergencyId}/resolve")
    public SuccessStatus resolveEmergency(
            @PathVariable int emergencyId,
            @RequestBody Map<String, Object> body
    ) {
        String remarks = body.get("remarks").toString();
        EmergencyNotification notif = emRepo.findById(emergencyId).get();
        notif.setStatus(EmergencyNotification.RESOLVED_STATUS);
        notif.setRemarks(remarks);
        emRepo.save(notif);

        return SuccessStatus.INSTANCE;
    }

    @PutMapping("/emergency/{emergencyId}/receive")
    public SuccessStatus receiveEmergency(
            @PathVariable int emergencyId
    ) {
        EmergencyNotification notif = emRepo.findById(emergencyId).get();
        notif.setStatus(EmergencyNotification.RECEIVED_STATUS);
        emRepo.save(notif);

        return SuccessStatus.INSTANCE;
    }
}
