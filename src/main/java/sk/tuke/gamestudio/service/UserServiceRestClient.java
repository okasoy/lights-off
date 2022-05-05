package sk.tuke.gamestudio.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;
import sk.tuke.gamestudio.entity.Users;

public class UserServiceRestClient implements UserService{
    private final String url = "http://localhost:8080/api/user";

    @Autowired
    private RestTemplate restTemplate;


    @Override
    public void addUser(Users user) throws UserException{
        restTemplate.postForEntity(url, user, Users.class);
    }

    @Override
    public String getPassword(String game, String login) throws UserException{
        return restTemplate.getForEntity(url + "/" + game + "/" + login, String.class).getBody();
    }

    @Override
    public void reset() throws UserException{
        throw new UnsupportedOperationException("Not supported via web service");
    }
}
