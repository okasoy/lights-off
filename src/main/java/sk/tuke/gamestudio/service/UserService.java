package sk.tuke.gamestudio.service;

import sk.tuke.gamestudio.entity.Users;

public interface UserService {
    void addUser(Users user);
    String getPassword(String game, String login);
    void reset();
}
