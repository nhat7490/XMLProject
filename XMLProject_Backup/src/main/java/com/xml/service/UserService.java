package com.xml.service;

import com.xml.model.User;
import com.xml.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    public final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findUser(String username, String password){
        return userRepository.findByUsernameAndPassword(username,password);
    }
}
