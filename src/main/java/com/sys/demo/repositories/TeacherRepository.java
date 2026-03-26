package com.sys.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sys.demo.entities.Teacher;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    
}
