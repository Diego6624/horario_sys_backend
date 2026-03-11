package com.sys.demo.jobs;

import com.sys.demo.entities.Subject;
import com.sys.demo.repositories.SubjectRepository;
import com.sys.demo.repositories.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class SubjectCleanupJob {

    @Autowired private SubjectRepository subjectRepo;
    @Autowired private ScheduleRepository scheduleRepo;

    // Ejecutar todos los días a las 2 AM
    @Scheduled(cron = "0 0 2 * * ?")
    public void eliminarAsignaturasVencidas() {
        LocalDate hoy = LocalDate.now();

        for (Subject subject : subjectRepo.findAll()) {
            // Suponiendo que guardas fechaInicio en Subject
            LocalDate fechaFin = subject.getFechaInicio().plusWeeks(subject.getDuracionSemanas());

            if (hoy.isAfter(fechaFin)) {
                // Borrar primero los schedules asociados
                scheduleRepo.deleteAll(subject.getSchedules());
                // Luego borrar el subject
                subjectRepo.delete(subject);
                System.out.println("Asignatura eliminada: " + subject.getCourse().getNombre());
            }
        }
    }
}
