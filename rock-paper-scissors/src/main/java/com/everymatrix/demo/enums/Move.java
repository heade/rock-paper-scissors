package com.everymatrix.demo.enums;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public enum Move {
    Rock,
    Paper,
    Scissors;

    private static final List<Move> VALUES = Collections.unmodifiableList(Arrays.asList(values()));
    private static final int SIZE = VALUES.size();

    public static Move randomMove() {
        return VALUES.get(ThreadLocalRandom.current().nextInt(SIZE));
    }
}
