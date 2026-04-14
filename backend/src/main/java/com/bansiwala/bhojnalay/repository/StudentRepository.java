package com.bansiwala.bhojnalay.repository;

import com.bansiwala.bhojnalay.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    Optional<Student> findByQrCodeUuid(UUID qrCodeUuid);

    Optional<Student> findByMobile(String mobile);

    Optional<Student> findByFingerprintTemplate(String fingerprintTemplate);

    List<Student> findByIsActiveTrue();

    boolean existsByMobile(String mobile);
}
