package com.bansiwala.bhojnalay.controller;

import com.bansiwala.bhojnalay.dto.StudentRequest;
import com.bansiwala.bhojnalay.dto.StudentResponse;
import com.bansiwala.bhojnalay.service.QrCodeService;
import com.bansiwala.bhojnalay.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService studentService;
    private final QrCodeService qrCodeService;

    public StudentController(StudentService studentService, QrCodeService qrCodeService) {
        this.studentService = studentService;
        this.qrCodeService = qrCodeService;
    }

    @PostMapping
    public ResponseEntity<StudentResponse> register(@Valid @RequestBody StudentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(studentService.registerStudent(request));
    }

    @GetMapping
    public ResponseEntity<List<StudentResponse>> getAll(@RequestParam(defaultValue = "false") boolean activeOnly) {
        List<StudentResponse> students = activeOnly
                ? studentService.getAllActiveStudents()
                : studentService.getAllStudents();
        return ResponseEntity.ok(students);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(studentService.getStudentById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<StudentResponse> update(@PathVariable Long id, @Valid @RequestBody StudentRequest request) {
        return ResponseEntity.ok(studentService.updateStudent(id, request));
    }

    @GetMapping("/{id}/qr")
    public ResponseEntity<byte[]> getQrCode(@PathVariable Long id) {
        StudentResponse student = studentService.getStudentById(id);
        byte[] qrImage = qrCodeService.generateQrCode(student.getQrCodeUuid().toString());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);
        headers.setContentDispositionFormData("filename", "qr-" + student.getName() + ".png");
        return new ResponseEntity<>(qrImage, headers, HttpStatus.OK);
    }

    @PatchMapping("/{id}/fingerprint")
    public ResponseEntity<StudentResponse> registerFingerprint(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {
        String template = body.get("fingerprintTemplate");
        if (template == null || template.isBlank()) {
            throw new IllegalArgumentException("fingerprintTemplate is required");
        }
        return ResponseEntity.ok(studentService.registerFingerprint(id, template));
    }

    @PatchMapping("/{id}/payment")
    public ResponseEntity<StudentResponse> recordPayment(
            @PathVariable Long id,
            @RequestBody Map<String, Object> body) {
        double amount = Double.parseDouble(body.get("amount").toString());
        return ResponseEntity.ok(studentService.recordPayment(id, amount));
    }

    @PatchMapping("/{id}/clear-due")
    public ResponseEntity<StudentResponse> clearPaymentDue(@PathVariable Long id) {
        return ResponseEntity.ok(studentService.clearPaymentDue(id));
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivate(@PathVariable Long id) {
        studentService.deactivateStudent(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<Void> activate(@PathVariable Long id) {
        studentService.activateStudent(id);
        return ResponseEntity.noContent().build();
    }
}
