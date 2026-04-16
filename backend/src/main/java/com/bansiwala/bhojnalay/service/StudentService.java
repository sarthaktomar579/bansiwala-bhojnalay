package com.bansiwala.bhojnalay.service;

import com.bansiwala.bhojnalay.dto.StudentRequest;
import com.bansiwala.bhojnalay.dto.StudentResponse;
import com.bansiwala.bhojnalay.entity.Student;
import com.bansiwala.bhojnalay.exception.StudentNotFoundException;
import com.bansiwala.bhojnalay.repository.StudentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Transactional
    public StudentResponse registerStudent(StudentRequest request) {
        if (studentRepository.existsByMobile(request.getMobile())) {
            throw new IllegalArgumentException("Student with mobile " + request.getMobile() + " already exists");
        }

        Student student = new Student();
        student.setName(request.getName());
        student.setMobile(request.getMobile());
        student.setEmail(request.getEmail());
        student.setFingerprintTemplate(request.getFingerprintTemplate());

        student = studentRepository.save(student);
        return StudentResponse.from(student);
    }

    public StudentResponse getStudentById(Long id) {
        Student student = findStudentOrThrow(id);
        return StudentResponse.from(student);
    }

    public StudentResponse getStudentByQr(String qrUuid) {
        Student student = studentRepository.findByQrCodeUuid(UUID.fromString(qrUuid))
                .orElseThrow(() -> new StudentNotFoundException("No student found for this QR code"));
        return StudentResponse.from(student);
    }

    public List<StudentResponse> getAllActiveStudents() {
        return studentRepository.findByIsActiveTrue()
                .stream()
                .map(StudentResponse::from)
                .toList();
    }

    public List<StudentResponse> getAllStudents() {
        return studentRepository.findAll()
                .stream()
                .map(StudentResponse::from)
                .toList();
    }

    @Transactional
    public StudentResponse updateStudent(Long id, StudentRequest request) {
        Student student = findStudentOrThrow(id);
        student.setName(request.getName());
        student.setMobile(request.getMobile());
        student.setEmail(request.getEmail());
        if (request.getFingerprintTemplate() != null) {
            student.setFingerprintTemplate(request.getFingerprintTemplate());
        }
        student = studentRepository.save(student);
        return StudentResponse.from(student);
    }

    @Transactional
    public StudentResponse registerFingerprint(Long id, String fingerprintTemplate) {
        Student student = findStudentOrThrow(id);
        student.setFingerprintTemplate(fingerprintTemplate);
        student = studentRepository.save(student);
        return StudentResponse.from(student);
    }

    @Transactional
    public StudentResponse recordPayment(Long id, double amount) {
        Student student = findStudentOrThrow(id);
        student.setAmountPaid(student.getAmountPaid() + amount);
        student = studentRepository.save(student);
        return StudentResponse.from(student);
    }

    @Transactional
    public void deactivateStudent(Long id) {
        Student student = findStudentOrThrow(id);
        student.setIsActive(false);
        studentRepository.save(student);
    }

    @Transactional
    public void activateStudent(Long id) {
        Student student = findStudentOrThrow(id);
        student.setIsActive(true);
        studentRepository.save(student);
    }

    private Student findStudentOrThrow(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException("Student not found with ID: " + id));
    }
}
