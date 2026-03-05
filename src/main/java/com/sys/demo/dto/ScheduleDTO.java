package com.sys.demo.dto;

import lombok.Data;

@Data
public class ScheduleDTO {
    private String dayOfWeek;
    private String startTime;
    private String endTime;
    private String sesion;
    private Long classroomId;
    private Long subjectId;
}
