package com.sys.demo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.sys.demo.entities.Subject;

public interface SubjectRepository extends JpaRepository<Subject, Long> {
    List<Subject> findByTeacherId(Long teacherId);
}
