package tests;

import sk.tuke.gamestudio.entity.Rating;
import org.junit.jupiter.api.Test;
import sk.tuke.gamestudio.service.RatingService;
import sk.tuke.gamestudio.service.RatingServiceJDBC;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RatingServiceTest {
    private final RatingService ratingService = new RatingServiceJDBC();

    @Test
    public void testReset() {
        ratingService.reset();

        assertEquals(0, ratingService.getAverageRating("lights off"));
    }

    @Test
    public void testSetRating() {
        ratingService.reset();
        Date date = new Date();

        ratingService.setRating(new Rating("lights off", "Nick", 3, date));

        int rating = ratingService.getRating("lights off", "Nick");
        assertEquals(3, rating);
    }

    @Test
    public void testGetRating() {
        ratingService.reset();
        Date date = new Date();
        ratingService.setRating(new Rating("lights off", "Jake", 3, date));
        ratingService.setRating(new Rating("lights off", "Kate", 5, date));
        ratingService.setRating(new Rating("lights off", "Nick", 4, date));

        int rating = ratingService.getRating("lights off", "Kate");

        assertEquals(5, rating);
    }

    @Test
    public void testGetAverageRating() {
        ratingService.reset();
        Date date = new Date();
        ratingService.setRating(new Rating("lights off", "Jake", 5, date));
        ratingService.setRating(new Rating("lights off", "Kate", 5, date));
        ratingService.setRating(new Rating("lights off", "Nick", 4, date));

        int rating = ratingService.getAverageRating("lights off");

        assertEquals(4, rating);
    }
}
