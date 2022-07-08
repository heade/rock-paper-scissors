package com.everymatrix.demo.service;

import com.everymatrix.demo.enums.Move;

public interface ResultService {

    void storeRoundResult(String result);

    void storeMoves(Move firstPlayerMove, Move secondPlayerMove);

    void storeFinalResult(int firstPlayerScoreCount, int secondPlayerScoreCount);
}
