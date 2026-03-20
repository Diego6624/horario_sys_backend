package com.sys.demo.services;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
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
    public List<ScheduleViewDTO> getAllSchedules() {
        return scheduleRepository.findAll()
                .stream()
                .map(this::toViewDTO)
                .toList();
    }

    // ✏️ CREAR
    public Schedule createSchedule(ScheduleDTO dto) {
        Subject subject = subjectRepository.findById(dto.getSubjectId())
                .orElseThrow(() -> new RuntimeException("Subject not found"));

        Classroom classroom = classroomRepository.findById(dto.getClassroomId())
                .orElseThrow(() -> new RuntimeException("Classroom not found"));

        Schedule schedule = new Schedule();

        if (dto.getDate() != null && !dto.getDate().isBlank()) {
            LocalDate parsedDate = LocalDate.parse(dto.getDate());
            schedule.setDate(parsedDate);
            schedule.setDayOfWeek(parsedDate.getDayOfWeek());
        } else {
            if (dto.getDayOfWeek() == null || dto.getDayOfWeek().isBlank()) {
                throw new RuntimeException("Debe especificar dayOfWeek o date");
            }
            DayOfWeek day = DayOfWeek.valueOf(dto.getDayOfWeek().toUpperCase());
            LocalDate today = LocalDate.now();
            LocalDate nextDate = today.with(java.time.temporal.TemporalAdjusters.nextOrSame(day));
            schedule.setDayOfWeek(day);
            schedule.setDate(nextDate);
        }

        schedule.setStartTime(LocalTime.parse(dto.getStartTime()));
        schedule.setEndTime(LocalTime.parse(dto.getEndTime()));
        schedule.setSesion(dto.getSesion());
        schedule.setSubject(subject);
        schedule.setClassroom(classroom);
        schedule.setEstado("Libre");

        Schedule saved = scheduleRepository.save(schedule);

        notificarEstadoActual();
        return saved;
    }

    // 🔄 ACTUALIZAR ESTADO Y NOTIFICAR
    public Schedule updateEstado(Long id, String nuevoEstado) {
        var opt = scheduleRepository.findById(id);
        if (opt.isEmpty()) return null;

        Schedule schedule = opt.get();
        schedule.setEstado(nuevoEstado);
        Schedule saved = scheduleRepository.save(schedule);

        // 🔔 Notificar a todos los clientes conectados
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

    // 🔹 Mapper limpio
    public ScheduleViewDTO toViewDTO(Schedule s) {
        ScheduleViewDTO dto = new ScheduleViewDTO();
        dto.setId(s.getId());
        dto.setDate(s.getDate() != null ? s.getDate().format(DateTimeFormatter.ISO_LOCAL_DATE) : "");
        dto.setDayOfWeek(s.getDayOfWeek() != null
                ? s.getDayOfWeek().getDisplayName(TextStyle.FULL, new Locale("es", "ES")).toLowerCase()
                : "");
        dto.setStartTime(s.getStartTime() != null ? s.getStartTime().toString() : "");
        dto.setEndTime(s.getEndTime() != null ? s.getEndTime().toString() : "");
        dto.setSesion(s.getSesion() != null ? s.getSesion() : "");
        dto.setClassroom(s.getClassroom() != null ? s.getClassroom().getNombre() : "");
        dto.setCourse(s.getSubject() != null && s.getSubject().getCourse() != null
                ? s.getSubject().getCourse().getNombre()
                : "");
        dto.setTeacher(s.getSubject() != null && s.getSubject().getTeacher() != null
                ? s.getSubject().getTeacher().getNombre()
                : "");
        dto.setTurno(s.getStartTime() != null ? calcularTurno(s.getStartTime()) : "");
        dto.setEstado(s.getEstado() != null ? s.getEstado() : "Libre");
        return dto;
    }

    // 🔔 Notificar estado actual vía WebSocket
    public void notificarEstadoActual() {
        List<ScheduleViewDTO> data = getCurrentSchedules();
        webSocketService.enviarEstadoActual(data);
    }

    @Scheduled(fixedRate = 60000)
    public void notificarCadaMinuto() {
        notificarEstadoActual();
    }

    public List<ScheduleViewDTO> getCurrentSchedules() {
        LocalTime ahora = LocalTime.now();
        DayOfWeek hoy = LocalDate.now().getDayOfWeek();

        List<Classroom> aulas = classroomRepository.findAll();
        List<Schedule> schedulesHoy = scheduleRepository.findByDayOfWeek(hoy);

        return aulas.stream().map(aula -> {
            Schedule s = schedulesHoy.stream()
                    .filter(sc -> sc.getClassroom().getId().equals(aula.getId()))
                    .filter(sc -> ahora.isAfter(sc.getStartTime()) && ahora.isBefore(sc.getEndTime()))
                    .findFirst()
                    .orElse(null);

            ScheduleViewDTO dto = new ScheduleViewDTO();
            dto.setClassroom(aula.getNombre());
            dto.setDate(LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE));
            dto.setDayOfWeek(hoy.getDisplayName(TextStyle.FULL, new Locale("es", "ES")).toLowerCase());

            if (s != null) {
                dto.setId(s.getId());
                dto.setStartTime(s.getStartTime().toString());
                dto.setEndTime(s.getEndTime().toString());
                dto.setSesion(s.getSesion());
                dto.setCourse(s.getSubject().getCourse().getNombre());
                dto.setTeacher(s.getSubject().getTeacher().getNombre());
                dto.setTurno(calcularTurno(s.getStartTime()));

                // ✅ lógica combinada
                if ("Cancelado".equalsIgnoreCase(s.getEstado())) {
                    dto.setEstado("Cancelado");
                } else if (ahora.isAfter(s.getStartTime()) && ahora.isBefore(s.getEndTime())) {
                    dto.setEstado("En clase");
                } else {
                    dto.setEstado("Libre");
                }
            } else {
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

    private String calcularTurno(LocalTime inicio) {
        if (inicio.isBefore(LocalTime.of(12, 0)))
            return "Mañana";
        else if (inicio.isBefore(LocalTime.of(18, 0)))
            return "Tarde";
        else
            return "Noche";
    }
}
