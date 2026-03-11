package com.sys.demo.controllers;

import com.sys.demo.dto.ScheduleViewDTO;
import com.sys.demo.dto.SubjectSessionDTO;
import com.sys.demo.dto.SubjectSessionMultiDTO;
import com.sys.demo.dto.SubjectViewDTO;
import com.sys.demo.entities.Subject;
import com.sys.demo.services.ScheduleService;
import com.sys.demo.services.SubjectService;
import com.sys.demo.services.SubjectSessionService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/subject-sessions")
@CrossOrigin("*")
public class SubjectSessionController {

    @Autowired
    private SubjectSessionService subjectSessionService;

    @Autowired
    private SubjectService subjectService;

    @Autowired
    private ScheduleService scheduleService;

    @PostMapping
    public ResponseEntity<SubjectViewDTO> crear(@RequestBody SubjectSessionDTO dto) {
        Subject subject = subjectSessionService.createSubjectWithSchedules(dto);
        return ResponseEntity.ok(subjectService.toViewDTO(subject));
    }

    // 📋 Listar los horarios generados para un subject
    @GetMapping("/{id}/schedules")
    public ResponseEntity<List<ScheduleViewDTO>> listarHorarios(@PathVariable Long id) {
        List<ScheduleViewDTO> respuesta = scheduleService.getSchedulesBySubject(id).stream()
                .map(scheduleService::toViewDTO)
                .toList();
        return ResponseEntity.ok(respuesta);
    }

    @PostMapping("/multi")
    public ResponseEntity<SubjectViewDTO> crearConMultiplesHorarios(@RequestBody SubjectSessionMultiDTO dto) {
        Subject subject = subjectSessionService.createSubjectWithMultipleSchedules(dto);
        return ResponseEntity.ok(subjectService.toViewDTO(subject));
    }
}
