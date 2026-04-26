package com.sys.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.sys.demo.dto.SubjectViewDTO;
import com.sys.demo.entities.Teacher;
import com.sys.demo.services.SubjectService;
import com.sys.demo.services.TeacherService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/teachers")
public class TeacherController {

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private SubjectService subjectService;

    @GetMapping
    public List<Teacher> getAllTeachers() {
        return teacherService.getAllTeachers();
    }

    @PostMapping("/{id}/photo")
    public ResponseEntity<Teacher> uploadPhoto(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file) {
        Teacher teacher = teacherService.getTeacherById(id);

        try {
            // Guardar archivo en carpeta local
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path path = Paths.get("uploads/teachers/" + fileName);
            Files.copy(file.getInputStream(), path);

            // Guardar ruta en BD
            teacher.setPhotoUrl("/uploads/teachers/" + fileName);
            teacherService.saveTeacher(teacher);

            return ResponseEntity.ok(teacher);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Teacher> getTeacher(@PathVariable Long id) {
        return ResponseEntity.ok(teacherService.getTeacherById(id));
    }

    @GetMapping("/{id}/subjects")
    public ResponseEntity<List<SubjectViewDTO>> getSubjectsByTeacher(@PathVariable Long id) {
        List<SubjectViewDTO> respuesta = subjectService.getSubjectsByTeacher(id).stream()
                .map(subjectService::toViewDTO)
                .toList();
        return ResponseEntity.ok(respuesta);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Teacher> updateTeacher(@PathVariable Long id, @RequestBody Teacher teacherDetails) {
        return ResponseEntity.ok(teacherService.updateTeacher(id, teacherDetails));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTeacher(@PathVariable Long id) {
        teacherService.deleteTeacher(id);
        return ResponseEntity.noContent().build();
    }
}
