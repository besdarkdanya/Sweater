package com.example.springbootstudy.services;


import com.example.springbootstudy.domain.Role;
import com.example.springbootstudy.domain.User;
import com.example.springbootstudy.repos.UserRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepo userRepo;

    private final PasswordEncoder passwordEncoder;
    private final SmtpMailSender mailService;


    public boolean createUser(User user) {
        if (userRepo.findByUsername(user.getUsername())!= null) {
            return false;
        } else {
            user.setActive(true);
            user.getRoles().add(Role.USER);
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setActivationCode(UUID.randomUUID().toString());
            log.info("Saving new User with username {}",user.getUsername());
            userRepo.save(user);

            if (!user.getEmail().isEmpty()) {
                String message = String.format(
                        "Hello %s \n" +
                                "Welcome to Sweater. Please, visit next link: http://localhost:8080/activate/%s"
                        ,user.getUsername()
                        ,user.getActivationCode()
                );

                mailService.send(user.getEmail(),"Activation Code",message);
            }
            return true;
        }
    }

    public boolean activateUser(String code) {

        User user = userRepo.findByActivationCode(code);

        if (user == null) {
            return false;
        }

        user.setActivationCode(null);
        userRepo.save(user);

        return true;
    }

    public void updateProfile(User currentUser, User editUser ) {

       String editUsername = editUser.getUsername();
       String editEmail = editUser.getEmail();
       String editPassword = editUser.getPassword();

       if (!editUsername.isEmpty()) {
           currentUser.setUsername(editUsername);
       }

        if (!editEmail.isEmpty()) {
            currentUser.setEmail(editEmail);
        }

        if (!editPassword.isEmpty()) {
            currentUser.setPassword(passwordEncoder.encode(editPassword));
        }

        userRepo.save(currentUser);

    }

}
