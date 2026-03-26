package com.sys.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sys.demo.dto.ClassroomViewDTO;
import com.sys.demo.entities.Classroom;
import com.sys.demo.repositories.ClassroomRepository;

import java.util.List;

@Service
public class ClassroomService {

    @Autowired
    private ClassroomRepository classroomRepository;

    public List<ClassroomViewDTO> getAllClassrooms() {
        return classroomRepository.findAll().stream()
                .map(c -> {
                    ClassroomViewDTO dto = new ClassroomViewDTO();
                    dto.setId(c.getId());
                    dto.setNombre(c.getNombre());
                    return dto;
                })
                .toList();
    }

    public Classroom createClassroom(Classroom classroom) {
        // Aquí podrías validar que no exista otro con el mismo nombre
        classroomRepository.findByNombre(classroom.getNombre())
                .ifPresent(c -> {
                    throw new RuntimeException("Ya existe un aula con ese nombre");
                });

        return classroomRepository.save(classroom);
    }

    public Classroom getClassroomById(Long id) {
        return classroomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Classroom not found"));
    }

    public Classroom updateClassroom(Long id, Classroom classroomDetails) {
        Classroom classroom = getClassroomById(id);
        classroom.setNombre(classroomDetails.getNombre());
        return classroomRepository.save(classroom);
    }

    public void deleteClassroom(Long id) {
        Classroom classroom = getClassroomById(id);
        classroomRepository.delete(classroom);
    }
}
