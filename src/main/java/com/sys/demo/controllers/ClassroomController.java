package com.sys.demo.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sys.demo.entities.Classroom;
import com.sys.demo.repositories.ClassroomRepository;

@RestController
@RequestMapping("/api/classrooms")
@CrossOrigin("*")
public class ClassroomController {

    @Autowired
    private ClassroomRepository classroomRepository;

    @GetMapping
    public List<Classroom> listar() {
        return classroomRepository.findAll();
    }

    @PostMapping
    public Classroom crear(@RequestBody Classroom classroom) {
        return classroomRepository.save(classroom);
    }
}
