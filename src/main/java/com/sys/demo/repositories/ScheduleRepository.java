package com.sys.demo.repositories;

import java.time.DayOfWeek;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sys.demo.entities.Schedule;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    List<Schedule> findByDayOfWeek(DayOfWeek dayOfWeek);
    List<Schedule> findByClassroomId(Long classroomId);
}
