package com.sys.demo.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sys.demo.entities.Horary;
import com.sys.demo.repositories.HoraryRepository;

@Service
public class HoraryServiceImpl implements HoraryService {

    @Autowired
    private HoraryRepository horaryRepository;

    @Autowired
    private WebSocketService webSocketService;

    // LISTAR TODOS (para TV)
    @Override
    public List<Horary> listar() {
        return horaryRepository.findAll();
    }

    // BUSCAR POR AULA (para editar)
    @Override
    public Horary buscarPorLab(String numLab) {
        return horaryRepository.findByNumLab(numLab);
    }

    // ACTUALIZAR
    @Override
    public Horary actualizar(String numLab, Horary datos) {

        Horary existente = horaryRepository.findByNumLab(numLab);

        if (existente == null) {
            throw new RuntimeException("Aula no encontrada");
        }

        existente.setNameDocente(datos.getNameDocente());
        existente.setNameCurso(datos.getNameCurso());
        existente.setHorario(datos.getHorario());
        existente.setNumSesion(datos.getNumSesion());

        Horary actualizado = horaryRepository.save(existente);

        // 🔔 NOTIFICAR CAMBIO EN TIEMPO REAL
        webSocketService.enviarActualizacionHorario(
                "UPDATE",
                actualizado.getId() // usa el ID de tu entidad
        );

        return actualizado;
    }
}
