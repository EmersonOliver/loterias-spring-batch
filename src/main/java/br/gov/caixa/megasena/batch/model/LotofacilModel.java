package br.gov.caixa.megasena.batch.model;

import br.gov.caixa.megasena.batch.model.utils.JogosDezenas;
import lombok.*;

import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LotofacilModel implements JogosDezenas {

    private Long concurso;
    private LocalDate data;
    private Integer bola1;
    private Integer bola2;
    private Integer bola3;
    private Integer bola4;
    private Integer bola5;
    private Integer bola6;
    private Integer bola7;
    private Integer bola8;
    private Integer bola9;
    private Integer bola10;
    private Integer bola11;
    private Integer bola12;
    private Integer bola13;
    private Integer bola14;
    private Integer bola15;

    @SneakyThrows
    public static LotofacilModel mapper(ResultSet rs,int rowNum) {
        return LotofacilModel.builder()
                .concurso(rs.getLong("concurso"))
                .data(rs.getDate("data").toLocalDate())
                .bola1(rs.getInt("bola1"))
                .bola2(rs.getInt("bola2"))
                .bola3(rs.getInt("bola3"))
                .bola4(rs.getInt("bola4"))
                .bola5(rs.getInt("bola5"))
                .bola6(rs.getInt("bola6"))
                .bola7(rs.getInt("bola7"))
                .bola8(rs.getInt("bola8"))
                .bola9(rs.getInt("bola9"))
                .bola10(rs.getInt("bola10"))
                .bola11(rs.getInt("bola11"))
                .bola12(rs.getInt("bola12"))
                .bola13(rs.getInt("bola13"))
                .bola14(rs.getInt("bola14"))
                .bola15(rs.getInt("bola15"))
                .build();
    }


    @Override
    public List<Integer> getDezenas() {
        return List.of(bola1, bola2, bola3, bola4, bola5, bola6, bola7, bola8, bola9, bola10, bola11, bola12, bola13, bola14, bola15);
    }
}
