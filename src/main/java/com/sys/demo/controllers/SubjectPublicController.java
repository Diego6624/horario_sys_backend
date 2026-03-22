package com.sys.demo.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sys.demo.dto.SubjectViewDTO;
import com.sys.demo.services.SubjectService;

@RestController
@RequestMapping("/api/public/subjects")
@CrossOrigin("*")
public class SubjectPublicController {

    @Autowired
    private SubjectService subjectService;

    @GetMapping
    public ResponseEntity<List<SubjectViewDTO>> getAllSubjectsPublic() {
        List<SubjectViewDTO> respuesta = subjectService.getAllSubjects().stream()
                .map(subjectService::toViewDTO)
                .toList();
        return ResponseEntity.ok(respuesta);
    }
}
