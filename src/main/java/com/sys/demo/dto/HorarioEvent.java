package com.sys.demo.dto;

import lombok.Data;

@Data
public class HorarioEvent {
    private String tipo;   
    private Long horarioId;

    public HorarioEvent() {}

    public HorarioEvent(String tipo, Long horarioId) {
        this.tipo = tipo;
        this.horarioId = horarioId;
    }
}
