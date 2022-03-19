package service;

import entity.Score;

import java.util.List;

public interface ScoreService {
    void addScore(Score score) throws ScoreException;
    List<Score> getTopScores(String game) throws ScoreException;
    int getScore(String game, String player) throws ScoreException;
    void reset() throws ScoreException;
}
