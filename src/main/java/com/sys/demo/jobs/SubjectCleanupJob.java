package com.sys.demo.jobs;

import com.sys.demo.entities.Subject;
import com.sys.demo.repositories.SubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class SubjectCleanupJob {

    @Autowired private SubjectRepository subjectRepo;

    // Ejecutar todos los días a las 2 AM
    @Scheduled(cron = "0 0 2 * * ?")
    public void marcarAsignaturasCompletadas() {
        LocalDate hoy = LocalDate.now();

        for (Subject subject : subjectRepo.findAll()) {
            if (subject.getFechaInicio() != null && subject.getDuracionSemanas() != null) {
                LocalDate fechaFin = subject.getFechaInicio().plusWeeks(subject.getDuracionSemanas());

                if (hoy.isAfter(fechaFin)) {
                    subject.setEstado("COMPLETADO");
                    subjectRepo.save(subject);
                    System.out.println("Asignatura marcada como COMPLETADA: " + subject.getCourse().getNombre());
                }
            }
        }
    }
}

