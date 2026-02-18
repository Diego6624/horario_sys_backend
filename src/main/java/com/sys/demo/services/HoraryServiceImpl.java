package com.sys.demo.services;

import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sys.demo.entities.Horary;
import com.sys.demo.entities.Status;
import com.sys.demo.repositories.HoraryRepository;
import com.sys.demo.repositories.StatusRepository;

@Service
public class HoraryServiceImpl implements HoraryService {

    @Autowired
    private HoraryRepository horaryRepository;

    @Autowired
    private WebSocketService webSocketService;

    @Autowired
    private StatusRepository statusRepository;

    // LISTAR TODOS (para TV)
    @Override
    public List<Horary> listar() {
        return horaryRepository.findAll();
    }

    @Override
    public List<Horary> listarActivos() {
        return horaryRepository.findByEnabledTrue();
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

        // Estado por aula
        Status status = statusRepository
                .findById(datos.getStatus().getId())
                .orElseThrow(() -> new RuntimeException("Estado no encontrado"));

        existente.setStatus(status);

        Horary actualizado = horaryRepository.save(existente);

        // 🔔 NOTIFICAR CAMBIO EN TIEMPO REAL
        webSocketService.enviarActualizacionHorario(
                "UPDATE",
                actualizado.getId() // usa el ID de tu entidad
        );

        return actualizado;
    }

    @Override
    public Horary toggle(Long id) {

        Horary h = horaryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Horario no encontrado"));

        h.setEnabled(!h.getEnabled());

        Horary actualizado = horaryRepository.save(h);

        // 🔔 Notificar a la TV
        webSocketService.enviarActualizacionHorario(
                "TOGGLE",
                actualizado.getId());

        return actualizado;
    }

    @Override
    public Horary cambiarEstado(Long horaryId, Long statusId) {

        Horary h = horaryRepository.findById(horaryId)
                .orElseThrow(() -> new RuntimeException("Aula no encontrada"));

        Status status = statusRepository.findById(statusId)
                .orElseThrow(() -> new RuntimeException("Estado no existe"));

        h.setStatus(status);

        Horary actualizado = horaryRepository.save(h);

        // 🔥 Notificar TVs
        webSocketService.enviarActualizacionHorario(
                "STATUS_CHANGE",
                actualizado.getId());

        return actualizado; 
    }

    public String getCurrentShift() {

        LocalTime now = LocalTime.now(
            ZoneId.of("America/Lima"));

        // 00:00 - 11:59
        if (now.isBefore(LocalTime.NOON)) {
            return "MAÑANA";
        }

        // 12:00 - 17:59
        if (now.isBefore(LocalTime.of(18, 0))) {
            return "TARDE";
        }

        // 18:00 - 23:59
        return "NOCHE";
    }
}
