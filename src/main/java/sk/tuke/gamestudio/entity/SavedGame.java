package sk.tuke.gamestudio.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import java.io.Serializable;

@Entity
@NamedQuery( name = "SavedGame.delete",
             query = "DELETE FROM SavedGame s WHERE s.username=:username")
@NamedQuery( name = "SavedGame.get",
             query = "SELECT s FROM SavedGame s WHERE s.username=:username")
public class SavedGame implements Serializable {
    @Id
    @GeneratedValue
    private int ident;
    private String username;

    private int score;

    private int level;

    public SavedGame(){}

    public SavedGame(String user, int score, int level) {
        this.username = user;
        this.score = score;
        this.level = level;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    @Override
    public String toString() {
        return "SavedGame{" +
                "username='" + username + '\'' +
                ", score=" + score +
                ", level=" + level +
                '}';
    }
}
