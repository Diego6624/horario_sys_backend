package com.sys.demo.services;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sys.demo.entities.Classroom;
import com.sys.demo.entities.Horary;
import com.sys.demo.entities.Schedule;
import com.sys.demo.entities.Status;
import com.sys.demo.repositories.ClassroomRepository;
import com.sys.demo.repositories.HoraryRepository;
import com.sys.demo.repositories.ScheduleRepository;
import com.sys.demo.repositories.StatusRepository;

@Service
public class HoraryServiceImpl implements HoraryService {

    @Autowired
    private HoraryRepository horaryRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private ClassroomRepository classroomRepository;

    @Autowired
    private StatusRepository statusRepository;

    @Autowired
    private WebSocketService webSocketService;

    @Override
    public List<Horary> listar() {
        return horaryRepository.findAll();
    }

    @Override
    public List<Horary> listarActivos() {
        return horaryRepository.findByEnabledTrue();
    }

    @Override
    public Horary buscarPorId(Long id) {
        return horaryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Horario no encontrado"));
    }

    @Override
    public Horary actualizar(Long id, Horary datos) {
        Horary existente = horaryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Horario no encontrado"));

        // Actualizar relaciones
        if (datos.getSchedule() != null) {
            Schedule schedule = scheduleRepository.findById(datos.getSchedule().getId())
                    .orElseThrow(() -> new RuntimeException("Schedule no encontrado"));
            existente.setSchedule(schedule);
        }

        if (datos.getClassroom() != null) {
            Classroom classroom = classroomRepository.findById(datos.getClassroom().getId())
                    .orElseThrow(() -> new RuntimeException("Classroom no encontrado"));
            existente.setClassroom(classroom);
        }

        if (datos.getStatus() != null) {
            Status status = statusRepository.findById(datos.getStatus().getId())
                    .orElseThrow(() -> new RuntimeException("Estado no encontrado"));
            existente.setStatus(status);
        }

        Horary actualizado = horaryRepository.save(existente);

        // 🔔 Notificar cambio
        webSocketService.enviarActualizacionHorario("UPDATE", actualizado.getId());

        return actualizado;
    }

    @Override
    public Horary toggle(Long id) {
        Horary h = horaryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Horario no encontrado"));

        h.setEnabled(!h.getEnabled());
        Horary actualizado = horaryRepository.save(h);

        webSocketService.enviarActualizacionHorario("TOGGLE", actualizado.getId());
        return actualizado;
    }

    @Override
    public Horary cambiarEstado(Long horaryId, Long statusId) {
        Horary h = horaryRepository.findById(horaryId)
                .orElseThrow(() -> new RuntimeException("Horario no encontrado"));

        Status status = statusRepository.findById(statusId)
                .orElseThrow(() -> new RuntimeException("Estado no encontrado"));

        h.setStatus(status);
        Horary actualizado = horaryRepository.save(h);

        webSocketService.enviarActualizacionHorario("STATUS_CHANGE", actualizado.getId());
        return actualizado;
    }

    @Override
    public List<Horary> listarPorDia(DayOfWeek dia) {
        return horaryRepository.findByScheduleDayOfWeek(dia);
    }

    @Override
    public List<Horary> listarPorClassroom(Long classroomId) {
        return horaryRepository.findByClassroomId(classroomId);
    }

    @Override
    public String getCurrentShift() {
        LocalTime now = LocalTime.now(ZoneId.of("America/Lima"));
        if (now.isBefore(LocalTime.NOON))
            return "MAÑANA";
        if (now.isBefore(LocalTime.of(18, 0)))
            return "TARDE";
        return "NOCHE";
    }

    @Override
    public Horary crear(Horary datos) {
        Classroom classroom = classroomRepository.findById(datos.getClassroom().getId())
                .orElseThrow(() -> new RuntimeException("Classroom no encontrado"));

        Schedule schedule = scheduleRepository.findById(datos.getSchedule().getId())
                .orElseThrow(() -> new RuntimeException("Schedule no encontrado"));

        Status status = statusRepository.findById(datos.getStatus().getId())
                .orElseThrow(() -> new RuntimeException("Estado no encontrado"));

        Horary nuevo = new Horary();
        nuevo.setClassroom(classroom);
        nuevo.setSchedule(schedule);
        nuevo.setStatus(status);
        nuevo.setEnabled(datos.getEnabled());

        Horary guardado = horaryRepository.save(nuevo);

        // 🔔 Notificar creación
        webSocketService.enviarActualizacionHorario("CREATE", guardado.getId());

        return guardado;
    }

}
