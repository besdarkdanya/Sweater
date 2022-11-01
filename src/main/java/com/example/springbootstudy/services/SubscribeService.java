package com.example.springbootstudy.services;

import com.example.springbootstudy.domain.User;
import com.example.springbootstudy.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SubscribeService {

    private final UserRepo userRepo;

    @Autowired
    public SubscribeService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    public void subscribe(User authUser, User user) {
        user.getSubscribers().add(authUser);

        userRepo.save(user);
    }

    public void unsubscribe(User authUser,User user) {
        user.getSubscribers().remove(authUser);
        userRepo.save(user);
    }
}
