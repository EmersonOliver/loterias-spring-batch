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
@Entity
@Table(name = "tb_quina")
@AllArgsConstructor
@NoArgsConstructor
public class Quina implements Serializable {

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
}
