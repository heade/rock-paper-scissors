package com.everymatrix.demo.service;

import com.everymatrix.demo.enums.Choose;

public interface ResultService {

    void storeRoundResult(String result);

    void storeMoves(Choose firstPlayerMove, Choose secondPlayerMove);

    void storeFinalResult(int firstPlayerScoreCount, int secondPlayerScoreCount);
}
