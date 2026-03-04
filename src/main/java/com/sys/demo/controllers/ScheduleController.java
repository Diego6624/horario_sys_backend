package com.sys.demo.controllers;

import java.time.DayOfWeek;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.sys.demo.dto.ScheduleDTO;
import com.sys.demo.entities.Horary;
import com.sys.demo.entities.Schedule;
import com.sys.demo.services.HoraryService;
import com.sys.demo.services.ScheduleService;

@RestController
@RequestMapping("/api/schedules")
@CrossOrigin("*")
public class ScheduleController {

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private HoraryService horaryService;

    @GetMapping
    public ResponseEntity<List<Horary>> listar() {
        List<Horary> lista = horaryService.listarActivos();
        return ResponseEntity.ok(lista);
    }

    @PostMapping
    public ResponseEntity<Schedule> crear(@RequestBody ScheduleDTO dto) {
        Schedule saved = scheduleService.createSchedule(dto);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/day/{day}")
    public List<Schedule> listarPorDia(@PathVariable String day) {
        DayOfWeek dia = DayOfWeek.valueOf(day.toUpperCase());
        return scheduleService.getSchedulesByDay(dia);
    }

    @GetMapping("/classroom/{id}")
    public List<Schedule> listarPorClassroom(@PathVariable Long id) {
        return scheduleService.getSchedulesByClassroom(id);
    }
}
