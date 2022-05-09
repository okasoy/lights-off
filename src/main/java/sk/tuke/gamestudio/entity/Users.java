package sk.tuke.gamestudio.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@NamedQuery( name = "Users.getUserPassword",
             query = "SELECT u.password FROM Users u WHERE u.game=:game AND u.login=:player")
@NamedQuery( name = "Users.resetUsers",
             query = "DELETE FROM Users")
public class Users implements Serializable {
    @Id
    @GeneratedValue
    private int ident;
    private String game;

    private String login;

    private String password;

    public Users(){}

    public Users(String game, String login, String password) {
        this.game = game;
        this.login = login;
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getGame() {
        return game;
    }

    public void setGame(String game) {
        this.game = game;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
