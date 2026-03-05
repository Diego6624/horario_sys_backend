package com.sys.demo.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sys.demo.dto.SubjectDTO;
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

    public Subject createSubject(SubjectDTO dto) {
        Course course = courseRepository.findById(dto.getCourseId())
                .orElseThrow(() -> new RuntimeException("Course not found"));
        Teacher teacher = teacherRepository.findById(dto.getTeacherId())
                .orElseThrow(() -> new RuntimeException("Teacher not found"));

        Subject subject = new Subject();
        subject.setCourse(course);
        subject.setTeacher(teacher);
        subject.setDuracionSemanas(dto.getDuracionSemanas());

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
}
