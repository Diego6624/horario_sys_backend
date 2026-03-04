package com.sys.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sys.demo.entities.Course;

public interface CourseRepository extends JpaRepository<Course, Long> {
    
}
