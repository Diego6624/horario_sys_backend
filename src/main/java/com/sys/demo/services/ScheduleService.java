package com.sys.demo.services;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sys.demo.dto.ScheduleDTO;
import com.sys.demo.dto.ScheduleViewDTO;
import com.sys.demo.entities.Classroom;
import com.sys.demo.entities.Schedule;
import com.sys.demo.entities.Subject;
import com.sys.demo.repositories.ClassroomRepository;
import com.sys.demo.repositories.ScheduleRepository;
import com.sys.demo.repositories.SubjectRepository;

@Service
public class ScheduleService {

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private ClassroomRepository classroomRepository;

    @Autowired
    private WebSocketService webSocketService;

    // 📋 LISTAR TODOS
    public List<Schedule> getAllSchedules() {
        return scheduleRepository.findAll();
    }

    // ✏️ CREAR
    public Schedule createSchedule(ScheduleDTO dto) {
        Subject subject = subjectRepository.findById(dto.getSubjectId())
                .orElseThrow(() -> new RuntimeException("Subject not found"));

        Classroom classroom = classroomRepository.findById(dto.getClassroomId())
                .orElseThrow(() -> new RuntimeException("Classroom not found"));

        Schedule schedule = new Schedule();
        schedule.setDayOfWeek(DayOfWeek.valueOf(dto.getDayOfWeek().toUpperCase()));
        schedule.setStartTime(LocalTime.parse(dto.getStartTime()));
        schedule.setEndTime(LocalTime.parse(dto.getEndTime()));
        schedule.setSesion(dto.getSesion());
        schedule.setSubject(subject);
        schedule.setClassroom(classroom);

        Schedule saved = scheduleRepository.save(schedule);

        // 🔔 Notificar al frontend
        notificarEstadoActual();

        return saved;
    }

    // 🔍 FILTROS
    public List<Schedule> getSchedulesByDay(DayOfWeek day) {
        return scheduleRepository.findByDayOfWeek(day);
    }

    public List<Schedule> getSchedulesByClassroom(Long classroomId) {
        return scheduleRepository.findByClassroomId(classroomId);
    }

    public List<Schedule> getSchedulesBySubject(Long subjectId) {
        return scheduleRepository.findBySubjectId(subjectId);
    }

    // 📊 Estado actual
    public String calcularEstado(Schedule s) {
        LocalTime ahora = LocalTime.now();
        if (ahora.isAfter(s.getStartTime()) && ahora.isBefore(s.getEndTime())) {
            return "En clase";
        }
        return "Libre";
    }

    // 🔹 Mapper limpio
    // 🔹 Mapper limpio
    public ScheduleViewDTO toViewDTO(Schedule s) {
        ScheduleViewDTO dto = new ScheduleViewDTO();
        dto.setId(s.getId());
        dto.setDayOfWeek(s.getDayOfWeek().toString());
        dto.setStartTime(s.getStartTime().toString());
        dto.setEndTime(s.getEndTime().toString());
        dto.setSesion(s.getSesion());
        dto.setClassroom(s.getClassroom().getNombre());
        dto.setCourse(s.getSubject().getCourse().getNombre());
        dto.setTeacher(s.getSubject().getTeacher().getNombre());

        // 🔹 Calcular turno según hora de inicio
        LocalTime inicio = s.getStartTime();
        String turno;
        if (inicio.isBefore(LocalTime.of(12, 0))) {
            turno = "Mañana";
        } else if (inicio.isBefore(LocalTime.of(18, 0))) {
            turno = "Tarde";
        } else {
            turno = "Noche";
        }
        dto.setTurno(turno);

        // 🔹 Calcular estado actual
        dto.setEstado(calcularEstado(s));

        return dto;
    }

    // 🔔 Notificar estado actual vía WebSocket
    public void notificarEstadoActual() {
        List<ScheduleViewDTO> data = getCurrentSchedules();
        webSocketService.enviarEstadoActual(data);
    }

    public List<ScheduleViewDTO> getCurrentSchedules() {
        LocalTime ahora = LocalTime.now();
        DayOfWeek hoy = LocalDate.now().getDayOfWeek();

        List<Classroom> aulas = classroomRepository.findAll();
        List<Schedule> schedulesHoy = scheduleRepository.findByDayOfWeek(hoy);

        return aulas.stream().map(aula -> {
            // Buscar si hay un horario activo en esta aula
            Schedule s = schedulesHoy.stream()
                    .filter(sc -> sc.getClassroom().getId().equals(aula.getId()))
                    // 🔹 Solo si la clase está en curso ahora mismo
                    .filter(sc -> ahora.isAfter(sc.getStartTime()) && ahora.isBefore(sc.getEndTime()))
                    .findFirst()
                    .orElse(null);

            ScheduleViewDTO dto = new ScheduleViewDTO();
            dto.setClassroom(aula.getNombre());

            if (s != null) {
                dto.setId(s.getId());
                dto.setDayOfWeek(s.getDayOfWeek().toString());
                dto.setStartTime(s.getStartTime().toString());
                dto.setEndTime(s.getEndTime().toString());
                dto.setSesion(s.getSesion());
                dto.setCourse(s.getSubject().getCourse().getNombre());
                dto.setTeacher(s.getSubject().getTeacher().getNombre());
                dto.setTurno(calcularTurno(s.getStartTime()));
                dto.setEstado("En clase");
            } else {
                // Aula libre sin clase activa
                dto.setEstado("Libre");
                dto.setCourse("");
                dto.setTeacher("");
                dto.setSesion("");
                dto.setStartTime("");
                dto.setEndTime("");
                dto.setTurno(calcularTurno(ahora));
            }

            return dto;
        }).toList();
    }

    // 🔹 Método auxiliar para turno
    private String calcularTurno(LocalTime inicio) {
        if (inicio.isBefore(LocalTime.of(12, 0))) {
            return "Mañana";
        } else if (inicio.isBefore(LocalTime.of(18, 0))) {
            return "Tarde";
        } else {
            return "Noche";
        }
    }

}
