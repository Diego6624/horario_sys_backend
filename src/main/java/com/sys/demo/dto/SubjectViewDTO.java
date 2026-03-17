package com.sys.demo.dto;

import lombok.Data;

@Data
public class SubjectViewDTO {
    private Long id;
    private Long courseId;
    private String course;
    private Long teacherId;
    private String teacher;
    private Integer duracionSemanas;
    private String modulo;
    private String fechaInicio;
}
