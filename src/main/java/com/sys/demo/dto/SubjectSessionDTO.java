package com.sys.demo.dto;

import lombok.Data;

@Data
public class SubjectSessionDTO {
    private Long teacherId;
    private Long courseId;
    private Long classroomId;
    private int duracionSemanas;
    private String dayOfWeek;
    private String startTime;
    private String endTime;
}
