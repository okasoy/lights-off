package sk.tuke.gamestudio.service;

import sk.tuke.gamestudio.entity.Users;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Transactional
public class UserServiceJPA implements UserService{

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void addUser(Users user) throws UserException {
        entityManager.persist(user);
    }

    @Override
    public String getPassword(String game, String login) throws UserException {
        return (String) entityManager.createNamedQuery("Users.getUserPassword").setParameter("game", game)
                .setParameter("player", login).getSingleResult();
    }

    @Override
    public void reset() {
        entityManager.createNamedQuery("Users.resetUsers").executeUpdate();
    }
}
