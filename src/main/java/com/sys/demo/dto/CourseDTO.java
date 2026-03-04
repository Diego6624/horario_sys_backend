package com.sys.demo.dto;

import lombok.Data;

@Data
public class CourseDTO {
    private String nombre;
    private Integer duracionSemanas;
    private Long teacherId;
}
