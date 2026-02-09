package com.sys.demo.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "horary")
public class Horary {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numLab", unique = true, nullable = false)
    private String numLab;

    @Column(name = "nameDocente")
    private String nameDocente;

    @Column(name = "nameCurso")
    private String nameCurso;

    @Column(name = "horario")
    private String horario;

    @Column(name = "numSesion")
    private String numSesion;
}
