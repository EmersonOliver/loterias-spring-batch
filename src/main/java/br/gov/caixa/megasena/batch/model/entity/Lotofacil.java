package br.gov.caixa.megasena.batch.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_lotofacil")
public class Lotofacil implements Serializable {


    @Id
    @Column
    private Long concurso;

    @Column
    private LocalDate data;

    @Column
    private Integer bola1;

    @Column
    private Integer bola2;

    @Column
    private Integer bola3;

    @Column
    private Integer bola4;

    @Column
    private Integer bola5;

    @Column
    private Integer bola6;

    @Column
    private Integer bola7;

    @Column
    private Integer bola8;

    @Column
    private Integer bola9;

    @Column
    private Integer bola10;

    @Column
    private Integer bola11;

    @Column
    private Integer bola12;

    @Column
    private Integer bola13;

    @Column
    private Integer bola14;

    @Column
    private Integer bola15;

}
