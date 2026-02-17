package com.sys.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.sys.demo.entities.Status;

public interface StatusRepository extends JpaRepository<Status, Long> {
    
}