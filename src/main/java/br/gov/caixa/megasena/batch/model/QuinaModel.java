package br.gov.caixa.megasena.batch.model;

import br.gov.caixa.megasena.batch.model.utils.JogosDezenas;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QuinaModel implements JogosDezenas {

    private Long concurso;
    private LocalDate data;
    private Integer bola1;
    private Integer bola2;
    private Integer bola3;
    private Integer bola4;
    private Integer bola5;

    @Override
    public List<Integer> getDezenas() {
        return Arrays.asList(bola1, bola2, bola3, bola4, bola5);
    }

    public static QuinaModel mapper(ResultSet rs, int rowNum) throws SQLException {
        return QuinaModel.builder()
                .concurso(rs.getLong("concurso"))
                .data(rs.getDate("data").toLocalDate())
                .bola1(rs.getInt("bola1"))
                .bola2(rs.getInt("bola2"))
                .bola3(rs.getInt("bola3"))
                .bola4(rs.getInt("bola4"))
                .bola5(rs.getInt("bola5"))
                .build();
    }

}
