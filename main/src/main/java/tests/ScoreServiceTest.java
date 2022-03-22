package tests;

import entity.Score;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import service.ScoreService;
import service.ScoreServiceJDBC;

import java.util.Date;

public class ScoreServiceTest {
    private final ScoreService scoreService = new ScoreServiceJDBC();

    @Test
    public void testReset() {
        scoreService.reset();

        assertEquals(0, scoreService.getTopScores("lights off").size());
    }

    @Test
    public void testAddScore() {
        scoreService.reset();
        Date date = new Date();

        scoreService.addScore(new Score("lights off", "Nick", 100, date));

        var scores = scoreService.getTopScores("lights off");
        assertEquals(1, scores.size());
        assertEquals("lights off", scores.get(0).getGame());
        assertEquals("Nick", scores.get(0).getPlayer());
        assertEquals(100, scores.get(0).getPoints());
        assertEquals(date, scores.get(0).getPlayedOn());
    }

    @Test
    public void testGetTopScores() {
        scoreService.reset();
        Date date = new Date();
        scoreService.addScore(new Score("lights off", "Jake", 120, date));
        scoreService.addScore(new Score("lights off", "Kate", 150, date));
        scoreService.addScore(new Score("mines", "Nick", 180, date));
        scoreService.addScore(new Score("lights off", "Mike", 100, date));

        var scores = scoreService.getTopScores("lights off");

        assertEquals(3, scores.size());

        assertEquals("lights off", scores.get(0).getGame());
        assertEquals("Kate", scores.get(0).getPlayer());
        assertEquals(150, scores.get(0).getPoints());
        assertEquals(date, scores.get(0).getPlayedOn());

        assertEquals("lights off", scores.get(1).getGame());
        assertEquals("Jake", scores.get(1).getPlayer());
        assertEquals(120, scores.get(1).getPoints());
        assertEquals(date, scores.get(1).getPlayedOn());

        assertEquals("lights off", scores.get(2).getGame());
        assertEquals("Mike", scores.get(2).getPlayer());
        assertEquals(100, scores.get(2).getPoints());
        assertEquals(date, scores.get(2).getPlayedOn());
    }

    @Test
    public void testGetScore() {
        scoreService.reset();
        Date date = new Date();

        scoreService.addScore(new Score("lights off", "Jake", 120, date));
        scoreService.addScore(new Score("lights off", "Kate", 150, date));
        scoreService.addScore(new Score("lights off", "Mike", 100, date));

        int score = scoreService.getScore("lights off", "Kate");

        assertEquals(150, score);
    }
}

