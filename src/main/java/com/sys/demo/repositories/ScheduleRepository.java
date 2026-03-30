package com.sys.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.sys.demo.entities.Schedule;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    List<Schedule> findByDayOfWeek(DayOfWeek day);
    List<Schedule> findByClassroomId(Long classroomId);
    List<Schedule> findBySubjectId(Long subjectId);
    List<Schedule> findByDate(LocalDate date);
}
