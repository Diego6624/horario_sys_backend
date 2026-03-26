package com.sys.demo.services;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sys.demo.dto.SubjectSessionMultiDTO;
import com.sys.demo.dto.SubjectViewDTO;
import com.sys.demo.entities.Course;
import com.sys.demo.entities.Subject;
import com.sys.demo.entities.Teacher;
import com.sys.demo.repositories.CourseRepository;
import com.sys.demo.repositories.SubjectRepository;
import com.sys.demo.repositories.TeacherRepository;

@Service
public class SubjectService {

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    public List<Subject> getAllSubjects() {
        return subjectRepository.findAll();
    }

    public List<Subject> getSubjectsByTeacher(Long teacherId) {
        return subjectRepository.findByTeacherId(teacherId);
    }

    public SubjectViewDTO toViewDTO(Subject s) {
        SubjectViewDTO dto = new SubjectViewDTO();
        dto.setId(s.getId());
        dto.setCourseId(s.getCourse().getId());
        dto.setCourse(s.getCourse().getNombre());
        dto.setTeacherId(s.getTeacher().getId());
        dto.setTeacher(s.getTeacher().getNombre());
        dto.setDuracionSemanas(s.getDuracionSemanas());
        dto.setModulo(s.getModulo());
        dto.setFechaInicio(s.getFechaInicio() != null ? s.getFechaInicio().toString() : null);
        return dto;
    }

    // 🚀 Crear usando SubjectSessionMultiDTO (sí tiene modulo)
    public Subject createSubject(SubjectSessionMultiDTO dto) {
        Course course = courseRepository.findById(dto.getCourseId())
                .orElseThrow(() -> new RuntimeException("Course not found"));
        Teacher teacher = teacherRepository.findById(dto.getTeacherId())
                .orElseThrow(() -> new RuntimeException("Teacher not found"));

        Subject subject = new Subject();
        subject.setCourse(course);
        subject.setTeacher(teacher);
        subject.setDuracionSemanas(dto.getDuracionSemanas());
        subject.setModulo(dto.getModulo());
        subject.setEstado("ACTIVO");
        subject.setFechaInicio(dto.getFechaInicio() != null
                ? LocalDate.parse(dto.getFechaInicio())
                : LocalDate.now());

        return subjectRepository.save(subject);
    }

    public Subject getSubjectById(Long id) {
        return subjectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Subject not found"));
    }

    public void deleteSubject(Long id) {
        Subject subject = getSubjectById(id);
        subjectRepository.delete(subject);
    }

    // 🔄 Actualizar usando SubjectSessionMultiDTO (sí tiene modulo)
    public Subject updateSubject(Long id, SubjectSessionMultiDTO dto) {
        Subject subject = subjectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Subject not found"));

        Course course = courseRepository.findById(dto.getCourseId())
                .orElseThrow(() -> new RuntimeException("Course not found"));
        Teacher teacher = teacherRepository.findById(dto.getTeacherId())
                .orElseThrow(() -> new RuntimeException("Teacher not found"));

        subject.setCourse(course);
        subject.setTeacher(teacher);
        subject.setDuracionSemanas(dto.getDuracionSemanas());
        subject.setModulo(dto.getModulo());
        subject.setFechaInicio(dto.getFechaInicio() != null
                ? LocalDate.parse(dto.getFechaInicio())
                : LocalDate.now());

        return subjectRepository.save(subject);
    }
}
