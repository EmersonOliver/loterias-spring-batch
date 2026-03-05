package br.gov.caixa.megasena.batch.model;

import br.gov.caixa.megasena.batch.model.utils.JogosDezenas;
import lombok.*;

import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MegasenaModel implements JogosDezenas {

    private Long concurso;
    private LocalDate data;
    private Integer bola1;
    private Integer bola2;
    private Integer bola3;
    private Integer bola4;
    private Integer bola5;
    private Integer bola6;

    @SneakyThrows
    public static MegasenaModel mapper(ResultSet rs, int rowNum) {
        return MegasenaModel.builder()
                .concurso(rs.getLong("concurso"))
                .data(rs.getDate("data").toLocalDate())
                .bola1(rs.getInt("bola1"))
                .bola2(rs.getInt("bola2"))
                .bola3(rs.getInt("bola3"))
                .bola4(rs.getInt("bola4"))
                .bola5(rs.getInt("bola5"))
                .bola6(rs.getInt("bola6"))
                .build();
    }

    @Override
    public List<Integer> getDezenas() {
        return Arrays.asList(bola1, bola2, bola3, bola4, bola5, bola6);
    }


}
