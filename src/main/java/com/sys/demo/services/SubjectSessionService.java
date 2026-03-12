package com.sys.demo.services;

import com.sys.demo.dto.SubjectSessionDTO;
import com.sys.demo.dto.SubjectSessionMultiDTO;
import com.sys.demo.dto.ScheduleDTO;
import com.sys.demo.entities.*;
import com.sys.demo.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;

@Service
public class SubjectSessionService {

    @Autowired
    private SubjectRepository subjectRepo;

    @Autowired
    private TeacherRepository teacherRepo;

    @Autowired
    private CourseRepository courseRepo;

    @Autowired
    private ClassroomRepository classroomRepo;

    @Autowired
    private ScheduleRepository scheduleRepo;

    // 🔹 Flujo 1: crear subject con un solo horario repetido por semanas
    public Subject createSubjectWithSchedules(SubjectSessionDTO dto) {
        Teacher teacher = teacherRepo.findById(dto.getTeacherId())
                .orElseThrow(() -> new RuntimeException("Teacher not found"));
        Course course = courseRepo.findById(dto.getCourseId())
                .orElseThrow(() -> new RuntimeException("Course not found"));
        Classroom classroom = classroomRepo.findById(dto.getClassroomId())
                .orElseThrow(() -> new RuntimeException("Classroom not found"));

        Subject subject = new Subject();
        subject.setTeacher(teacher);
        subject.setCourse(course);
        subject.setDuracionSemanas(dto.getDuracionSemanas());
        subject.setFechaInicio(LocalDate.now());
        subjectRepo.save(subject);

        DayOfWeek dia = DayOfWeek.valueOf(dto.getDayOfWeek().toUpperCase());
        LocalTime inicio = LocalTime.parse(dto.getStartTime());
        LocalTime fin = LocalTime.parse(dto.getEndTime());

        for (int i = 0; i < dto.getDuracionSemanas(); i++) {
            Schedule schedule = new Schedule();
            schedule.setSubject(subject);
            schedule.setClassroom(classroom);
            schedule.setDayOfWeek(dia);
            schedule.setStartTime(inicio);
            schedule.setEndTime(fin);
            schedule.setSesion("Semana " + (i + 1));
            scheduleRepo.save(schedule);
        }

        return subject;
    }

    // Flujo 2: crear subject con múltiples horarios distintos repetidos por semanas
    public Subject createSubjectWithMultipleSchedules(SubjectSessionMultiDTO dto) {
        Teacher teacher = teacherRepo.findById(dto.getTeacherId())
                .orElseThrow(() -> new RuntimeException("Teacher not found"));
        Course course = courseRepo.findById(dto.getCourseId())
                .orElseThrow(() -> new RuntimeException("Course not found"));

        Subject subject = new Subject();
        subject.setTeacher(teacher);
        subject.setCourse(course);
        subject.setDuracionSemanas(dto.getDuracionSemanas());
        subject.setModulo(dto.getModulo()); // 🔹 nuevo campo
        subject.setFechaInicio(LocalDate.now());
        subjectRepo.save(subject);

        int sesionCounter = 1; // contador global de sesiones

        for (int i = 0; i < dto.getDuracionSemanas(); i++) {
            for (ScheduleDTO schDto : dto.getSchedules()) {
                Classroom classroom = classroomRepo.findById(schDto.getClassroomId())
                        .orElseThrow(() -> new RuntimeException("Classroom not found"));

                Schedule schedule = new Schedule();
                schedule.setSubject(subject);
                schedule.setClassroom(classroom);
                schedule.setDayOfWeek(DayOfWeek.valueOf(schDto.getDayOfWeek().toUpperCase()));
                schedule.setStartTime(LocalTime.parse(schDto.getStartTime()));
                schedule.setEndTime(LocalTime.parse(schDto.getEndTime()));

                // 🔁 asignar automáticamente módulo + sesión
                schedule.setSesion(subject.getModulo() + " - S" + String.format("%02d", sesionCounter));
                sesionCounter++;

                scheduleRepo.save(schedule);
            }
        }
        return subject;
    }
}