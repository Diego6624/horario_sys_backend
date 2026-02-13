package com.sys.demo.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.sys.demo.entities.Horary;
import com.sys.demo.services.HoraryService;

@RestController
@RequestMapping("/api/horaries")
@CrossOrigin(origins = "*") // Para React / TV
public class HoraryController {

    @Autowired
    private HoraryService horaryService;

    // ===============================
    // 📺 LISTAR TODOS (Pantalla TV)
    // ===============================
    @GetMapping
    public ResponseEntity<List<Horary>> listar() {

        // SOLO aulas habilitadas para la TV
        List<Horary> lista = horaryService.listarActivos();

        return ResponseEntity.ok(lista);
    }

    // ==================================
    // 🔍 BUSCAR POR AULA (Editar form)
    // ==================================
    @GetMapping("/aula/{aula}")
    public ResponseEntity<?> buscarPorAula(@PathVariable String aula) {

        Horary horary = horaryService.buscarPorLab(aula);

        if (horary == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(horary);
    }

    // ==================================
    // ✏️ ACTUALIZAR HORARIO
    // ==================================
    @PutMapping("/aula/{aula}")
    public ResponseEntity<?> actualizar(
            @PathVariable String aula,
            @RequestBody Horary datos) {

        try {

            Horary actualizado = horaryService.actualizar(aula, datos);

            return ResponseEntity.ok(actualizado);

        } catch (RuntimeException e) {

            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
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

            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }
}
