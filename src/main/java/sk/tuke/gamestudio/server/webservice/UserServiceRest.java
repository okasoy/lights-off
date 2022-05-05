package sk.tuke.gamestudio.server.webservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sk.tuke.gamestudio.entity.Users;
import sk.tuke.gamestudio.service.UserService;

@RestController
@RequestMapping("/api/user")
public class UserServiceRest {

    @Autowired
    private UserService userService;

    @GetMapping("/{game}/{player}")
    public String getPassword(@PathVariable String game, @PathVariable String player){
        return userService.getPassword(game, player);
    }

    @PostMapping
    public void addUser(@RequestBody Users user) {
        userService.addUser(user);
    }
}
