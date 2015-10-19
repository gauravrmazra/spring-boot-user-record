package in.blogspot.javawithgaurav.service;

import in.blogspot.javawithgaurav.domain.User;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public interface UserService {
    
    public List<User> findAll();
    
    public User findById(final String id);
    
    public void remove(final User user);
    
    public User update(final User user);
    
    public User save(User user);
}
