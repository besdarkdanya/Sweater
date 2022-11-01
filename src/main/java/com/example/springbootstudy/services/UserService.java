package com.example.springbootstudy.services;


import com.example.springbootstudy.domain.Role;
import com.example.springbootstudy.domain.User;
import com.example.springbootstudy.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepo userRepo;

    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;
    private final FileService fileService;

    @Autowired
    public UserService(UserRepo userRepo, PasswordEncoder passwordEncoder, MailService mailService, FileService fileService) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.mailService = mailService;
        this.fileService = fileService;
    }

    @Value("${hostname}")
    private String hostname;

    public boolean createUser(User user) throws UsernameNotFoundException {

        if (userRepo.findByUsername(user.getUsername())!= null) {
            return false;
        } else {
            user.setActive(false);
            user.getRoles().add(Role.USER);
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setActivationCode(UUID.randomUUID().toString());
            userRepo.save(user);

            if (!user.getEmail().isEmpty()) {
                String message = String.format(
                        "Hello %s \n" +
                                "Welcome to Sweater. Please, visit next link: http://%s/activate/%s"
                        ,user.getUsername()
                        ,hostname
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
        user.setActive(true);
        userRepo.save(user);

        return true;
    }

    public void updateProfile(User currentUser, User editedUserInfo,MultipartFile file) throws IOException {

       if (editedUserInfo.getUsername() != null) {
           currentUser.setUsername(editedUserInfo.getUsername());
       } else {
           currentUser.setUsername(currentUser.getUsername());
       }

        if (editedUserInfo.getEmail() != null) {
            currentUser.setEmail(editedUserInfo.getEmail());
        } else {
            currentUser.setEmail(currentUser.getEmail());
        }

        if (editedUserInfo.getPassword() != null) {
            currentUser.setPassword(passwordEncoder.encode(editedUserInfo.getPassword()));
        } else {
            currentUser.setPassword(currentUser.getPassword());
        }

        currentUser.setAvatarFilename(fileService.getFilenameForUserAvatar(file));

        userRepo.save(currentUser);

    }

}
