package com.sys.demo.dto;

import lombok.Data;

@Data
public class ScheduleViewDTO {
    private Long id;
    private String dayOfWeek;
    private String startTime;
    private String endTime;
    private String sesion;
    private String classroom;
    private Long classroomId;
    private String course;
    private String teacher;
    private String turno;
    private String estado;
    private String date;
}
