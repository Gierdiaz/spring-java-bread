package com.example.springboot.controllers;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.springboot.DTO.PatientDTO;
import com.example.springboot.models.Address;
import com.example.springboot.models.Patient;
import com.example.springboot.repositories.PatientRepository;

import jakarta.validation.Valid;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
public class PatientController {
    @Autowired
    PatientRepository patientRepository;

    @GetMapping("api/v1/patients")
    public ResponseEntity<Page<Patient>> getPatients(
        @RequestParam(value = "page", defaultValue = "0") int page,
        @RequestParam(value = "size", defaultValue = "10") int size,
        @RequestParam(value = "sortBy", defaultValue = "name") String sortBy
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        Page<Patient> patients = patientRepository.findAll(pageable);

        List<Patient> patientsList = patientRepository.findAll();
        if (!patientsList.isEmpty()) {
            for (Patient patient : patientsList) {
                patient.add(linkTo(methodOn(PatientController.class).getPatients(page, size, sortBy)).withSelfRel());
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(patients);
    }

    @GetMapping("api/v1/patients/{id}")
    public ResponseEntity<Object> getPatient(@PathVariable(value = "id") UUID id) {
        Optional<Patient> patientOptional = patientRepository.findById(id);
        if (patientOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Patient not found.");
        }
        Patient patient = patientOptional.get();
        patient.add(linkTo(methodOn(PatientController.class).getPatient(id)).withSelfRel());
        return ResponseEntity.status(HttpStatus.OK).body(patient);
    }

    @GetMapping("api/v1/patients/cpf/{cpf}")
    public ResponseEntity<Object> getPatientByCpf(@PathVariable(value = "cpf") String cpf) {
        Optional<Patient> patientOptional = patientRepository.findByCpf(cpf);
        if (patientOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Patient not found.");
        }
        Patient patient = patientOptional.get();
        patient.add(linkTo(methodOn(PatientController.class).getPatientByCpf(cpf)).withSelfRel());
        return ResponseEntity.status(HttpStatus.OK).body(patient);
    }

    @PostMapping("api/v1/patients")
    public ResponseEntity<Patient> createPatient(@RequestBody @Valid PatientDTO patientDTO) {
        Patient patient = patientDTO.toPatient();
        return ResponseEntity.status(HttpStatus.CREATED).body(patientRepository.save(patient));
    }

    @PutMapping("api/v1/patients/{id}")
    public ResponseEntity<Object> updatePatient(@PathVariable(value = "id") UUID id, @RequestBody @Valid PatientDTO patientDTO) {
        Optional<Patient> existingPatient = patientRepository.findById(id);
        if (existingPatient.isPresent()) {
            Patient patient = existingPatient.get();
            BeanUtils.copyProperties(patientDTO, patient, "address", "patientId");
            Address address = new Address(
                    patientDTO.address().street(),
                    patientDTO.address().number(),
                    patientDTO.address().complement(),
                    patientDTO.address().neighborhood(),
                    patientDTO.address().city(),
                    patientDTO.address().state(),
                    patientDTO.address().postalCode()
            );
            patient.setAddress(address);
            return ResponseEntity.status(HttpStatus.OK).body(patientRepository.save(patient)); 
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Patient not found.");
    }
    

    @DeleteMapping("api/v1/patients/{id}")
    public ResponseEntity<Object> deletePatient(@PathVariable(value = "id") UUID id) {
        Optional<Patient> existingPatient = patientRepository.findById(id);
        if (!existingPatient.isEmpty()) {
            patientRepository.delete(existingPatient.get());
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Patient not found.");
    }
}
