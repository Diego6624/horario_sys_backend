package com.sys.demo.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.sys.demo.dto.SubjectSessionMultiDTO;
import com.sys.demo.dto.SubjectViewDTO;
import com.sys.demo.entities.Subject;
import com.sys.demo.services.SubjectService;

@RestController
@RequestMapping("/api/subjects")
@CrossOrigin("*")
public class SubjectController {

    @Autowired
    private SubjectService subjectService;

    @GetMapping
    public ResponseEntity<List<SubjectViewDTO>> getAllSubjects() {
        List<SubjectViewDTO> respuesta = subjectService.getAllSubjects().stream()
                .map(subjectService::toViewDTO)
                .toList();
        return ResponseEntity.ok(respuesta);
    }

    // 🚀 Crear Subject con múltiples sesiones y módulo
    @PostMapping
    public ResponseEntity<SubjectViewDTO> createSubject(@RequestBody SubjectSessionMultiDTO dto) {
        Subject saved = subjectService.createSubject(dto);
        return ResponseEntity.ok(subjectService.toViewDTO(saved));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubjectViewDTO> getSubject(@PathVariable Long id) {
        Subject subject = subjectService.getSubjectById(id);
        return ResponseEntity.ok(subjectService.toViewDTO(subject));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SubjectViewDTO> updateSubject(
            @PathVariable Long id,
            @RequestBody SubjectSessionMultiDTO dto) {
        Subject updated = subjectService.updateSubject(id, dto);
        return ResponseEntity.ok(subjectService.toViewDTO(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubject(@PathVariable Long id) {
        subjectService.deleteSubject(id);
        return ResponseEntity.noContent().build();
    }
}