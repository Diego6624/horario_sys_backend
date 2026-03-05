package com.sys.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.sys.demo.entities.Subject;

public interface SubjectRepository extends JpaRepository<Subject, Long> {

}
