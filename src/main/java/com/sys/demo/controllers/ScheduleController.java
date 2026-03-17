package com.sys.demo.controllers;

import java.time.DayOfWeek;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.sys.demo.dto.ScheduleDTO;
import com.sys.demo.dto.ScheduleViewDTO;
import com.sys.demo.services.ScheduleService;

@RestController
@RequestMapping("/api/schedules")
@CrossOrigin("*")
public class ScheduleController {

    @Autowired
    private ScheduleService scheduleService;

    // 📺 LISTAR TODOS LOS HORARIOS (DTO limpio)
    @GetMapping
    public ResponseEntity<List<ScheduleViewDTO>> listar() {
        List<ScheduleViewDTO> respuesta = scheduleService.getAllSchedules();
        return ResponseEntity.ok(respuesta);
    }

    // ✏️ CREAR HORARIO
    @PostMapping
    public ResponseEntity<ScheduleViewDTO> crear(@RequestBody ScheduleDTO dto) {
        var saved = scheduleService.createSchedule(dto);
        return ResponseEntity.ok(scheduleService.toViewDTO(saved));
    }

    // 🔍 FILTRAR POR DÍA
    @GetMapping("/day/{day}")
    public ResponseEntity<List<ScheduleViewDTO>> listarPorDia(@PathVariable String day) {
        DayOfWeek dia = DayOfWeek.valueOf(day.toUpperCase());
        List<ScheduleViewDTO> respuesta = scheduleService.getSchedulesByDay(dia)
                .stream()
                .map(scheduleService::toViewDTO)
                .toList();
        return ResponseEntity.ok(respuesta);
    }

    // 🔍 FILTRAR POR AULA
    @GetMapping("/classroom/{id}")
    public ResponseEntity<List<ScheduleViewDTO>> listarPorClassroom(@PathVariable Long id) {
        List<ScheduleViewDTO> respuesta = scheduleService.getSchedulesByClassroom(id)
                .stream()
                .map(scheduleService::toViewDTO)
                .toList();
        return ResponseEntity.ok(respuesta);
    }

    // 🔍 FILTRAR POR MATERIA
    @GetMapping("/subject/{id}")
    public ResponseEntity<List<ScheduleViewDTO>> listarPorSubject(@PathVariable Long id) {
        List<ScheduleViewDTO> respuesta = scheduleService.getSchedulesBySubject(id)
                .stream()
                .map(scheduleService::toViewDTO)
                .toList();
        return ResponseEntity.ok(respuesta);
    }

    // 📊 ESTADO ACTUAL
    @GetMapping("/current")
    public ResponseEntity<List<ScheduleViewDTO>> listarEstadoActual() {
        List<ScheduleViewDTO> respuesta = scheduleService.getCurrentSchedules();
        return ResponseEntity.ok(respuesta);
    }
}
