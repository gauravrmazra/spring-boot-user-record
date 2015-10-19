package in.blogspot.javawithgaurav.controller;

import java.util.Collections;
import java.util.List;

import in.blogspot.javawithgaurav.domain.User;
import in.blogspot.javawithgaurav.service.UserService;
import in.blogspot.javawithgaurav.utility.RestPreConditions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController("/users")
public class UserController {
    @Autowired
    private UserService userService;

    public UserController() {

    }

    @RequestMapping(name="/", method=RequestMethod.GET, produces={"application/json"})
    public List<User> getAllUsers() {
        List<User> users = userService.findAll();
        return Collections.unmodifiableList(users);
    }

    @RequestMapping(name="/{id}", method=RequestMethod.DELETE)
    public void deleteUser(@PathVariable("id") String id) {
        final User user = userService.findById(id);
        RestPreConditions.requireNonNull(user);
        userService.remove(user);
    }

    @RequestMapping(name="/{id}", method=RequestMethod.PUT, produces="application/json")
    public User updateUser(@RequestBody User user) {
        final User tempUser = userService.findById(user.getId());
        RestPreConditions.requireNonNull(tempUser);
        return userService.update(user);
    }

    @RequestMapping(name="/", method=RequestMethod.POST, produces="application/json")
    public User createUser(@RequestBody User user) {
        final User tempUser = userService.findById(user.getId());
        RestPreConditions.requireNull(tempUser);
        return userService.save(user);
    }
}
