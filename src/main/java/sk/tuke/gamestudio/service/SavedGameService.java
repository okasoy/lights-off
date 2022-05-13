package sk.tuke.gamestudio.service;

import sk.tuke.gamestudio.entity.SavedGame;

public interface SavedGameService {
    public void addGame(SavedGame game);
    public SavedGame getGame(String name);
}
