package com.sys.demo.services;

import java.util.List;

import com.sys.demo.entities.Horary;

public interface HoraryService {

    List<Horary> listar();

    Horary buscarPorLab(String numLab);

    Horary actualizar(String numLab, Horary datos);

    List<Horary> listarActivos();

    Horary toggle(Long id);
}