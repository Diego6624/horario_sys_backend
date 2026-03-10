package com.sys.demo.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.sys.demo.dto.ClassroomViewDTO;
import com.sys.demo.entities.Classroom;
import com.sys.demo.services.ClassroomService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/classrooms")
@CrossOrigin("*")
public class ClassroomController {

    @Autowired
    private ClassroomService classroomService;

    @GetMapping
    public ResponseEntity<List<ClassroomViewDTO>> listar() {
        return ResponseEntity.ok(classroomService.getAllClassrooms());
    }

    @PostMapping
    public ResponseEntity<Classroom> crear(@RequestBody Classroom classroom) {
        Classroom saved = classroomService.createClassroom(classroom);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Classroom> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(classroomService.getClassroomById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Classroom> actualizar(@PathVariable Long id, @RequestBody Classroom classroomDetails) {
        return ResponseEntity.ok(classroomService.updateClassroom(id, classroomDetails));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        classroomService.deleteClassroom(id);
        return ResponseEntity.noContent().build();
    }
}
