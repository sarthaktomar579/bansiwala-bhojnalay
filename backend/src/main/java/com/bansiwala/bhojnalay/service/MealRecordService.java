package com.bansiwala.bhojnalay.service;

import com.bansiwala.bhojnalay.dto.CheckInRequest;
import com.bansiwala.bhojnalay.dto.CheckInResponse;
import com.bansiwala.bhojnalay.dto.DailyReportResponse;
import com.bansiwala.bhojnalay.dto.StudentMealHistory;
import com.bansiwala.bhojnalay.entity.MealRecord;
import com.bansiwala.bhojnalay.entity.Student;
import com.bansiwala.bhojnalay.enums.CheckInMethod;
import com.bansiwala.bhojnalay.enums.MealType;
import com.bansiwala.bhojnalay.exception.DuplicateCheckInException;
import com.bansiwala.bhojnalay.exception.StudentNotFoundException;
import com.bansiwala.bhojnalay.repository.MealRecordRepository;
import com.bansiwala.bhojnalay.repository.StudentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.UUID;

@Service
public class MealRecordService {

    private final MealRecordRepository mealRecordRepository;
    private final StudentRepository studentRepository;
    private final MealTimeService mealTimeService;

    public MealRecordService(MealRecordRepository mealRecordRepository,
                             StudentRepository studentRepository,
                             MealTimeService mealTimeService) {
        this.mealRecordRepository = mealRecordRepository;
        this.studentRepository = studentRepository;
        this.mealTimeService = mealTimeService;
    }

    @Transactional
    public CheckInResponse checkInByQr(CheckInRequest request) {
        Student student = studentRepository.findByQrCodeUuid(UUID.fromString(request.getQrCodeUuid()))
                .orElseThrow(() -> new StudentNotFoundException("Invalid QR code — no student found"));

        MealType mealType = (request.getMealType() != null) ? request.getMealType() : mealTimeService.detectCurrentMeal();
        int thalis = (request.getThaliCount() != null && request.getThaliCount() > 0) ? request.getThaliCount() : 1;
        return processCheckIn(student, mealType, CheckInMethod.QR_SCAN, thalis);
    }

    @Transactional
    public CheckInResponse checkInByFingerprint(CheckInRequest request) {
        Student student = studentRepository.findByFingerprintTemplate(request.getFingerprintTemplate())
                .orElseThrow(() -> new StudentNotFoundException("Fingerprint not recognized — no matching student"));

        MealType mealType = (request.getMealType() != null) ? request.getMealType() : mealTimeService.detectCurrentMeal();
        return processCheckIn(student, mealType, CheckInMethod.FINGERPRINT, 1);
    }

    @Transactional
    public CheckInResponse checkInManual(Long studentId, MealType mealType, Integer thaliCount) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new StudentNotFoundException("Student not found with ID: " + studentId));

        if (mealType == null) {
            mealType = mealTimeService.detectCurrentMeal();
        }
        int thalis = (thaliCount != null && thaliCount > 0) ? thaliCount : 1;
        return processCheckIn(student, mealType, CheckInMethod.MANUAL, thalis);
    }

    @Transactional
    public CheckInResponse checkInByScan(Long studentId, int thaliCount) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new StudentNotFoundException("Student not found with ID: " + studentId));

        MealType mealType = mealTimeService.detectCurrentMeal();
        int thalis = (thaliCount > 0) ? thaliCount : 1;
        return processCheckIn(student, mealType, CheckInMethod.QR_SCAN, thalis);
    }

    private CheckInResponse processCheckIn(Student student, MealType mealType, CheckInMethod method, int thaliCount) {
        LocalDate today = LocalDate.now();

        var existing = mealRecordRepository.findByStudentIdAndMealDateAndMealType(student.getId(), today, mealType);

        if (existing.isPresent()) {
            MealRecord record = existing.get();
            int oldCount = record.getThaliCount();
            int newCount = oldCount + thaliCount;
            record.setThaliCount(newCount);
            record = mealRecordRepository.save(record);

            return new CheckInResponse(
                    record.getId(), student.getId(), student.getName(),
                    today, mealType, record.getCheckInTime(), method, newCount,
                    student.getName() + " — added " + thaliCount + " more thali(s) for " + mealType
                            + ". Total now: " + newCount + " thalis"
            );
        }

        MealRecord record = new MealRecord();
        record.setStudent(student);
        record.setMealDate(today);
        record.setMealType(mealType);
        record.setCheckInTime(LocalDateTime.now());
        record.setCheckInMethod(method);
        record.setThaliCount(thaliCount);

        record = mealRecordRepository.save(record);

        String thaliMsg = thaliCount > 1 ? " (" + thaliCount + " thalis)" : "";
        return new CheckInResponse(
                record.getId(), student.getId(), student.getName(),
                today, mealType, record.getCheckInTime(), method, thaliCount,
                student.getName() + " checked in for " + mealType + thaliMsg + " successfully!"
        );
    }

    public DailyReportResponse getDailyReport(LocalDate date) {
        long breakfast = mealRecordRepository.countByDateAndMealType(date, MealType.BREAKFAST);
        long lunch = mealRecordRepository.countByDateAndMealType(date, MealType.LUNCH);
        long dinner = mealRecordRepository.countByDateAndMealType(date, MealType.DINNER);

        long bThalis = mealRecordRepository.sumThalisByDateAndMealType(date, MealType.BREAKFAST);
        long lThalis = mealRecordRepository.sumThalisByDateAndMealType(date, MealType.LUNCH);
        long dThalis = mealRecordRepository.sumThalisByDateAndMealType(date, MealType.DINNER);

        return new DailyReportResponse(date, breakfast, lunch, dinner, breakfast + lunch + dinner,
                bThalis, lThalis, dThalis, bThalis + lThalis + dThalis);
    }

    public List<CheckInResponse> getTodayRecords(MealType mealType) {
        LocalDate today = LocalDate.now();
        List<MealRecord> records;

        if (mealType != null) {
            records = mealRecordRepository.findByMealDateAndMealTypeWithStudent(today, mealType);
        } else {
            records = mealRecordRepository.findByMealDateWithStudent(today);
        }

        return records.stream().map(this::toCheckInResponse).toList();
    }

    public StudentMealHistory getStudentMonthlyHistory(Long studentId, int year, int month) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new StudentNotFoundException("Student not found with ID: " + studentId));

        YearMonth ym = YearMonth.of(year, month);
        LocalDate start = ym.atDay(1);
        LocalDate end = ym.atEndOfMonth();

        List<MealRecord> records = mealRecordRepository
                .findByStudentIdAndMealDateBetweenOrderByMealDateAscMealTypeAsc(studentId, start, end);

        List<StudentMealHistory.MealEntry> meals = records.stream()
                .map(r -> new StudentMealHistory.MealEntry(
                        r.getMealDate(), r.getMealType(), r.getCheckInTime(), r.getCheckInMethod(), r.getThaliCount()))
                .toList();

        return new StudentMealHistory(
                student.getId(), student.getName(),
                year, month, meals,
                records.stream().filter(r -> r.getMealType() == MealType.BREAKFAST).count(),
                records.stream().filter(r -> r.getMealType() == MealType.LUNCH).count(),
                records.stream().filter(r -> r.getMealType() == MealType.DINNER).count()
        );
    }

    private CheckInResponse toCheckInResponse(MealRecord record) {
        return new CheckInResponse(
                record.getId(), record.getStudent().getId(), record.getStudent().getName(),
                record.getMealDate(), record.getMealType(), record.getCheckInTime(),
                record.getCheckInMethod(), record.getThaliCount(), null
        );
    }
}
