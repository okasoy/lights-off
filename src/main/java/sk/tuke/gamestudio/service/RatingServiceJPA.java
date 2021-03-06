package sk.tuke.gamestudio.service;

import sk.tuke.gamestudio.entity.Rating;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Transactional
public class RatingServiceJPA implements RatingService{
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void setRating(Rating rating) throws RatingException {
        if(getRating(rating.getGame(), rating.getPlayer()) != 0){
            entityManager.createNamedQuery("Rating.deleteRatingOnUpdate").setParameter("game", rating.getGame()).setParameter("player", rating.getPlayer()).executeUpdate();
        }
        entityManager.persist(rating);
    }

    @Override
    public int getAverageRating(String game) throws RatingException {
       Double rating = (double) entityManager.createNamedQuery("Rating.getAverageRating").setParameter("game", game).getSingleResult();
       return rating.intValue();
    }

    @Override
    public int getRating(String game, String player) throws RatingException {
        try {
            return (int) entityManager.createNamedQuery("Rating.getRating").setParameter("game", game).setParameter("player", player).getSingleResult();
        }
        catch (Exception e) {
            return 0;
        }
    }

    @Override
    public List<Rating> getRatings(String game) throws RatingException {
        return entityManager.createNamedQuery("Rating.getRatings")
                .setParameter("game", game).getResultList();
    }

    @Override
    public void reset() throws RatingException {
        entityManager.createNamedQuery("Rating.resetRating").executeUpdate();
    }
}
