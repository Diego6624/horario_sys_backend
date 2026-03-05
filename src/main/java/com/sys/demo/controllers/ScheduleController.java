package com.sys.demo.controllers;

import java.time.DayOfWeek;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.sys.demo.dto.ScheduleDTO;
import com.sys.demo.dto.SubjectViewDTO;
import com.sys.demo.entities.Schedule;
import com.sys.demo.services.ScheduleService;

@RestController
@RequestMapping("/api/schedules")
@CrossOrigin("*")
public class ScheduleController {

    @Autowired
    private ScheduleService scheduleService;

    // 📺 LISTAR TODOS LOS HORARIOS (Pantalla TV / Admin)
    @GetMapping
    public ResponseEntity<List<Schedule>> listar() {
        List<Schedule> lista = scheduleService.getAllSchedules();
        return ResponseEntity.ok(lista);
    }

    // ✏️ CREAR HORARIO
    @PostMapping
    public ResponseEntity<Schedule> crear(@RequestBody ScheduleDTO dto) {
        Schedule saved = scheduleService.createSchedule(dto);
        return ResponseEntity.ok(saved);
    }

    // 🔍 LISTAR POR DÍA DE LA SEMANA
    @GetMapping("/day/{day}")
    public List<Schedule> listarPorDia(@PathVariable String day) {
        DayOfWeek dia = DayOfWeek.valueOf(day.toUpperCase());
        return scheduleService.getSchedulesByDay(dia);
    }

    // 🔍 LISTAR POR AULA
    @GetMapping("/classroom/{id}")
    public List<Schedule> listarPorClassroom(@PathVariable Long id) {
        return scheduleService.getSchedulesByClassroom(id);
    }

    // 👁️ LISTAR ESTADO ACTUAL (Libre/Ocupado)
    @GetMapping("/current")
    public ResponseEntity<List<SubjectViewDTO>> listarEstadoActual() {
        List<Schedule> schedules = scheduleService.getAllSchedules();

        List<SubjectViewDTO> respuesta = schedules.stream().map(s -> {
            SubjectViewDTO dto = new SubjectViewDTO();
            dto.setClassroom(s.getClassroom().getNombre());
            dto.setTeacher(s.getSubject().getTeacher().getNombre());
            dto.setCourse(s.getSubject().getCourse().getNombre());
            dto.setHorario(s.getStartTime() + " - " + s.getEndTime());
            dto.setSesion(s.getSesion());
            dto.setEstado(scheduleService.calcularEstado(s)); // Libre / Ocupado
            return dto;
        }).toList();

        return ResponseEntity.ok(respuesta);
    }
}
