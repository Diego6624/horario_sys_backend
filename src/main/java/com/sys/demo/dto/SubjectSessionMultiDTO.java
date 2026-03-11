package com.sys.demo.dto;

import java.util.List;

import lombok.Data;

@Data
public class SubjectSessionMultiDTO {
    private Long teacherId;
    private Long courseId;
    private Integer duracionSemanas;
    private List<ScheduleDTO> schedules;
}
