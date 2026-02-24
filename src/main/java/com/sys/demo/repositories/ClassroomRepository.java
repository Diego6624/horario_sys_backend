package com.sys.demo.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sys.demo.entities.Classroom;

public interface ClassroomRepository extends JpaRepository<Classroom, Long> {
    Optional<Classroom> findByNombre(String nombre);
}
