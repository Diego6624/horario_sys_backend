package com.sys.demo.services;

import java.time.DayOfWeek;
import java.util.List;

import com.sys.demo.entities.Horary;

public interface HoraryService {
    List<Horary> listar();

    List<Horary> listarActivos();

    Horary buscarPorId(Long id);

    Horary actualizar(Long id, Horary datos);

    Horary toggle(Long id);

    Horary cambiarEstado(Long horaryId, Long statusId);

    List<Horary> listarPorDia(DayOfWeek dia);

    List<Horary> listarPorClassroom(Long classroomId); // 🔹 nuevo

    String getCurrentShift();
}
