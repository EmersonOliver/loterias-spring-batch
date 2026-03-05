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
@Table(name = "tb_megasena")
public class Megasena implements Serializable {

    @Id
    @Column(name = "concurso")
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

}
