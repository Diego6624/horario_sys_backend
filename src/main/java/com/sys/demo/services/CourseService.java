package com.sys.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sys.demo.dto.CourseDTO;
import com.sys.demo.entities.Course;
import com.sys.demo.entities.Teacher;
import com.sys.demo.repositories.CourseRepository;
import com.sys.demo.repositories.TeacherRepository;

import java.util.List;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    // Listar todos los cursos
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    // Crear curso nuevo
    public Course createCourse(CourseDTO dto) {
        Teacher teacher = teacherRepository.findById(dto.getTeacherId())
                .orElseThrow(() -> new RuntimeException("Teacher not found"));

        Course course = new Course();
        course.setNombre(dto.getNombre());
        course.setDuracionSemanas(dto.getDuracionSemanas());
        course.setTeacher(teacher);

        return courseRepository.save(course);
    }

    // Obtener curso por ID
    public Course getCourseById(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found"));
    }

    // Actualizar curso
    public Course updateCourse(Long id, CourseDTO dto) {
        Course course = getCourseById(id);
        Teacher teacher = teacherRepository.findById(dto.getTeacherId())
                .orElseThrow(() -> new RuntimeException("Teacher not found"));

        course.setNombre(dto.getNombre());
        course.setDuracionSemanas(dto.getDuracionSemanas());
        course.setTeacher(teacher);

        return courseRepository.save(course);
    }

    // Eliminar curso
    public void deleteCourse(Long id) {
        Course course = getCourseById(id);
        courseRepository.delete(course);
    }
}
