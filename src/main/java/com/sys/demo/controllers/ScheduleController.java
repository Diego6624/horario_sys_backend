package com.sys.demo.controllers;

import java.time.DayOfWeek;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sys.demo.entities.Schedule;
import com.sys.demo.repositories.ScheduleRepository;

@RestController
@RequestMapping("/api/schedules")
@CrossOrigin("*")
public class ScheduleController {

    @Autowired
    private ScheduleRepository scheduleRepository;

    @GetMapping
    public List<Schedule> listar() {
        return scheduleRepository.findAll();
    }

    @PostMapping
    public Schedule crear(@RequestBody Schedule schedule) {
        return scheduleRepository.save(schedule);
    }

    @GetMapping("/day/{day}")
    public List<Schedule> listarPorDia(@PathVariable String day) {
        DayOfWeek dia = DayOfWeek.valueOf(day.toUpperCase());
        return scheduleRepository.findByDayOfWeek(dia);
    }
}
