package br.gov.caixa.megasena.batch.service;

import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class CombinatioGeneration implements Iterator<List<Integer>> {
    private final int n = 60;
    private final int k = 6;
    private final int[] current;
    private boolean hasNext = true;

    public CombinatioGeneration() {
        current = new int[k];
        for (int i = 0; i < k; i++) {
            current[i] = i + 1;
        }
    }

    @Override
    public boolean hasNext() {
        return hasNext;
    }

    @Override
    public List<Integer> next() {
        List<Integer> result = Arrays.stream(current).boxed().collect(Collectors.toList());
        hasNext = false;
        for (int i = k - 1; i >= 0; i--) {
            if (current[i] < n - k + i + 1) {
                current[i]++;
                for (int j = i + 1; j < k; j++) {
                    current[j] = current[j - 1] + 1;
                }
                hasNext = true;
                break;
            }
        }
        return result;
    }
}
