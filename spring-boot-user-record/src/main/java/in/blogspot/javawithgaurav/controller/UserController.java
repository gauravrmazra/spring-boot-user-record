package in.blogspot.javawithgaurav.controller;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import in.blogspot.javawithgaurav.domain.User;
import in.blogspot.javawithgaurav.service.UserService;
import in.blogspot.javawithgaurav.utility.RestPreConditions;

@RestController
@RequestMapping(value="/users")
public class UserController {
    @Autowired
    private UserService userService;

    public UserController() {

    }

    @RequestMapping(value="/", method=RequestMethod.GET, produces={"application/json"})
    public List<User> getAllUsers() {
        List<User> users = userService.findAll();
        return Collections.unmodifiableList(users);
    }
    
    @RequestMapping(value="/{id}", method=RequestMethod.GET, produces={"application/json"})
    public User getUser(@PathVariable("id") String id) {
    	final User user = userService.findById(id);
        RestPreConditions.requireNonNull(user);
        return user;
    }
    
    @RequestMapping(value="/{id}", method=RequestMethod.DELETE)
    public void deleteUser(@PathVariable("id") String id) {
        final User user = userService.findById(id);
        RestPreConditions.requireNonNull(user);
        userService.remove(user);
    }

    @RequestMapping(value="/{id}", method=RequestMethod.PUT, produces="application/json")
    public User updateUser(@PathVariable("id") String id, @RequestBody User user) {
        final User tempUser = userService.findById(id);
        RestPreConditions.requireNonNull(tempUser);
        user.setId(tempUser.getId());
        return userService.update(user);
    }

    @RequestMapping(value="/", method=RequestMethod.POST, produces="application/json")
    public User createUser(@RequestBody User user) {
        final User tempUser = userService.findById(user.getId());
        RestPreConditions.requireNull(tempUser);
        user.setId(UUID.randomUUID().toString());
        return userService.save(user);
    }
}
