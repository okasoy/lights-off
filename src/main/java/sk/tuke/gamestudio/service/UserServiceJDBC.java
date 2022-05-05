package sk.tuke.gamestudio.service;

import sk.tuke.gamestudio.entity.Users;

import java.sql.*;

public class UserServiceJDBC implements UserService{
    public static final String URL = "jdbc:postgresql://localhost/gamestudio";
    public static final String USER = "postgres";
    public static final String PASSWORD = "postgres";
    public static final String SELECT = "SELECT game, login, password FROM users WHERE game = ? AND login = ?";
    public static final String DELETE = "DELETE FROM users";
    public static final String INSERT = "INSERT INTO users (game, player, password) VALUES (?, ?, ?)";

    @Override
    public void addUser(Users user) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(INSERT)
        ) {
            statement.setString(1, user.getGame());
            statement.setString(2, user.getLogin());
            statement.setString(3, user.getPassword());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new UserException("Problem inserting player", e);
        }
    }

    @Override
    public String getPassword(String game, String login) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(SELECT)
        ) {
            statement.setString(1, game);
            statement.setString(2, login);
            try (ResultSet rs = statement.executeQuery()) {
                rs.next();
                return rs.getString(3);
            }
        } catch (SQLException e) {
            throw new UserException("Problem selecting password", e);
        }
    }

    @Override
    public void reset() {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement statement = connection.createStatement()
        ) {
            statement.executeUpdate(DELETE);
        } catch (SQLException e) {
            throw new UserException("Problem deleting user", e);
        }
    }
}
