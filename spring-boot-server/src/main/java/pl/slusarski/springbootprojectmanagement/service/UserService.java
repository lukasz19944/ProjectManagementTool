package pl.slusarski.springbootprojectmanagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import pl.slusarski.springbootprojectmanagement.domain.User;
import pl.slusarski.springbootprojectmanagement.exception.UsernameAlreadyExistsException;
import pl.slusarski.springbootprojectmanagement.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public User saveUser(User user) {
        try {
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));

            user.setUsername(user.getUsername());

            user.setConfirmPassword("");

            return userRepository.save(user);
        } catch (Exception e) {
            throw new UsernameAlreadyExistsException("Username " + user.getUsername() + " already exists");
        }

    }


}
