package com.sys.demo.dto;

import lombok.Data;

@Data
public class SubjectDTO {
    private Long courseId;
    private Long teacherId;
    private Integer duracionSemanas;
}