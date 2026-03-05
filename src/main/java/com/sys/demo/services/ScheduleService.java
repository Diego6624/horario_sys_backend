package com.sys.demo.services;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sys.demo.dto.ScheduleDTO;
import com.sys.demo.dto.SubjectViewDTO;
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

    public List<Schedule> getAllSchedules() {
        return scheduleRepository.findAll();
    }

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

    public List<Schedule> getSchedulesByDay(DayOfWeek day) {
        return scheduleRepository.findByDayOfWeek(day);
    }

    public List<Schedule> getSchedulesByClassroom(Long classroomId) {
        return scheduleRepository.findByClassroomId(classroomId);
    }

    public List<Schedule> getSchedulesBySubject(Long subjectId) {
        return scheduleRepository.findBySubjectId(subjectId);
    }

    public String calcularEstado(Schedule s) {
        LocalTime ahora = LocalTime.now();
        if (ahora.isAfter(s.getStartTime()) && ahora.isBefore(s.getEndTime())) {
            return "En clase";
        }
        return "Libre";
    }

    // 🔹 Método para enviar estado actual al frontend
    public void notificarEstadoActual() {
        List<Schedule> schedules = scheduleRepository.findAll();

        List<SubjectViewDTO> data = schedules.stream().map(s -> {
            SubjectViewDTO dto = new SubjectViewDTO();
            dto.setClassroom(s.getClassroom().getNombre());
            dto.setTeacher(s.getSubject().getTeacher().getNombre());
            dto.setCourse(s.getSubject().getCourse().getNombre());
            dto.setHorario(s.getStartTime() + " - " + s.getEndTime());
            dto.setSesion(s.getSesion());
            dto.setEstado(calcularEstado(s));
            return dto;
        }).toList();

        webSocketService.enviarEstadoActual(data);
    }
}
