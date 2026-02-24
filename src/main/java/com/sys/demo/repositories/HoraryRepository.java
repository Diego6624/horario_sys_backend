package com.sys.demo.repositories;

import java.time.DayOfWeek;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sys.demo.entities.Horary;

public interface HoraryRepository extends JpaRepository<Horary, Long> {
    List<Horary> findByEnabledTrue();
    List<Horary> findByClassroomId(Long classroomId);
    List<Horary> findByScheduleDayOfWeek(DayOfWeek dayOfWeek);
}
