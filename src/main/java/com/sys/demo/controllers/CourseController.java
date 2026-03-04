package com.sys.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.sys.demo.dto.CourseDTO;
import com.sys.demo.entities.Course;
import com.sys.demo.entities.Teacher;
import com.sys.demo.repositories.CourseRepository;
import com.sys.demo.repositories.TeacherRepository;
import com.sys.demo.services.CourseService;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private CourseRepository courseRepository;

    // Listar todos los cursos
    @GetMapping
    public List<Course> getAllCourses() {
        return courseService.getAllCourses();
    }

    // Crear un nuevo curso
    @PostMapping
    public ResponseEntity<Course> createCourse(@RequestBody CourseDTO dto) {
        Teacher teacher = teacherRepository.findById(dto.getTeacherId())
                .orElseThrow(() -> new RuntimeException("Teacher not found"));

        Course course = new Course();
        course.setNombre(dto.getNombre());
        course.setDuracionSemanas(dto.getDuracionSemanas());
        course.setTeacher(teacher);

        return ResponseEntity.ok(courseRepository.save(course));
    }

    // Obtener curso por ID
    @GetMapping("/{id}")
    public ResponseEntity<Course> getCourse(@PathVariable Long id) {
        return ResponseEntity.ok(courseService.getCourseById(id));
    }

    // Actualizar curso existente
    @PutMapping("/{id}")
    public ResponseEntity<Course> updateCourse(@PathVariable Long id, @RequestBody CourseDTO dto) {
        return ResponseEntity.ok(courseService.updateCourse(id, dto));
    }

    // Eliminar curso
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
        return ResponseEntity.noContent().build();
    }
}
