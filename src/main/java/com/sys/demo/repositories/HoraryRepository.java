package com.sys.demo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sys.demo.entities.Horary;

public interface HoraryRepository extends JpaRepository<Horary, Long>{
    Horary findByNumLab(String numLab);
    List<Horary> findByEnabledTrue();
}
