package com.sys.demo.controllers;

import java.time.DayOfWeek;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.sys.demo.dto.HoraryViewDTO;
import com.sys.demo.entities.Horary;
import com.sys.demo.services.HoraryService;

@RestController
@RequestMapping("/api/horaries")
@CrossOrigin(origins = "*") // Para React / TV
public class HoraryController {

    @Autowired
    private HoraryService horaryService;

    // ===============================
    // 📺 LISTAR ACTIVOS (Pantalla TV)
    // ===============================
    @GetMapping
    public ResponseEntity<List<Horary>> listar() {
        List<Horary> lista = horaryService.listarActivos();
        return ResponseEntity.ok(lista);
    }

    // ===============================
    // 🛠️ ADMIN → TODOS
    // ===============================
    @GetMapping("/all")
    public ResponseEntity<List<Horary>> listarTodos() {
        List<Horary> lista = horaryService.listar();
        return ResponseEntity.ok(lista);
    }

    // ==================================
    // 🔍 BUSCAR POR CLASSROOM (Editar form)
    // ==================================
    @GetMapping("/classroom/{id}")
    public ResponseEntity<List<Horary>> buscarPorClassroom(@PathVariable Long id) {
        List<Horary> horarios = horaryService.listarPorClassroom(id);
        return ResponseEntity.ok(horarios);
    }

    // ==================================
    // 🔍 LISTAR POR DÍA DE LA SEMANA
    // ==================================
    @GetMapping("/day/{day}")
    public ResponseEntity<List<Horary>> listarPorDia(@PathVariable String day) {
        DayOfWeek dia = DayOfWeek.valueOf(day.toUpperCase());
        List<Horary> horarios = horaryService.listarPorDia(dia);
        return ResponseEntity.ok(horarios);
    }

    // ==================================
    // ✏️ ACTUALIZAR HORARIO
    // ==================================
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(
            @PathVariable Long id,
            @RequestBody Horary datos) {
        try {
            Horary actualizado = horaryService.actualizar(id, datos);
            return ResponseEntity.ok(actualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // ==================================
    // 👁️ TOGGLE HABILITAR / OCULTAR
    // ==================================
    @PutMapping("/toggle/{id}")
    public ResponseEntity<?> toggle(@PathVariable Long id) {
        try {
            Horary actualizado = horaryService.toggle(id);
            return ResponseEntity.ok(actualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // ==================================
    // 🔄 CAMBIAR ESTADO
    // ==================================
    @PutMapping("/{id}/status")
    public ResponseEntity<?> cambiarEstado(
            @PathVariable Long id,
            @RequestBody Map<String, Long> body) {
        Long statusId = body.get("statusId");
        horaryService.cambiarEstado(id, statusId);
        return ResponseEntity.ok().build();
    }

    // ==================================
    // ⏰ TURNO ACTUAL
    // ==================================
    @GetMapping("/current-shift")
    public String getCurrentShift() {
        return horaryService.getCurrentShift();
    }

    @PostMapping
    public ResponseEntity<Horary> crear(@RequestBody Horary datos) {
        Horary nuevo = horaryService.crear(datos);
        return ResponseEntity.ok(nuevo);
    }

    @GetMapping("/current")
    public ResponseEntity<List<HoraryViewDTO>> listarEstadoActual() {
        List<Horary> horarios = horaryService.listarActivos();

        List<HoraryViewDTO> respuesta = horarios.stream().map(h -> {
            HoraryViewDTO dto = new HoraryViewDTO();
            dto.setClassroom(h.getClassroom().getNombre());
            dto.setTeacher(h.getSchedule().getCourse().getTeacher().getNombre());
            dto.setCourse(h.getSchedule().getCourse().getNombre());
            dto.setHorario(h.getSchedule().getStartTime() + " - " + h.getSchedule().getEndTime());
            dto.setSesion(h.getSchedule().getSesion());
            dto.setEstado(h.getStatus().getName()); // Libre / Ocupado
            return dto;
        }).toList();

        return ResponseEntity.ok(respuesta);
    }

}
