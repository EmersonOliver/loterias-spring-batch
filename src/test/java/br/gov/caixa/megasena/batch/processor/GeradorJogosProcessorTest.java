package br.gov.caixa.megasena.batch.processor;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class GeradorJogosProcessorTest {


    @Test
    void test_processor() throws Exception {
        GeradorJogosProcessor processor = new GeradorJogosProcessor("05-03-2026");
        String result = processor.process(List.of(10, 11, 12, 13, 15));
        assertNotNull(result);
        assertEquals("1,10,11,12,13,15", result);
    }


}