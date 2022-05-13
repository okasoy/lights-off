package sk.tuke.gamestudio.service;

import sk.tuke.gamestudio.entity.SavedGame;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Transactional
public class SavedGameJPA implements SavedGameService{
    @PersistenceContext
    private EntityManager entityManager;

    public void addGame(SavedGame game){
        if(getGame(game.getUsername()) != null){
            entityManager.createNamedQuery("SavedGame.delete").setParameter("username", game.getUsername()).executeUpdate();
        }
        entityManager.persist(game);
    }

    public SavedGame getGame(String name) {
        try {
            return (SavedGame) entityManager.createNamedQuery("SavedGame.get").setParameter("username", name).getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
}
