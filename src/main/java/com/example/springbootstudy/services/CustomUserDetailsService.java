package com.example.springbootstudy.services;


import com.example.springbootstudy.domain.User;
import com.example.springbootstudy.exceptions.UserNotActiveException;
import com.example.springbootstudy.repos.UserRepo;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;



@Service
public class CustomUserDetailsService implements UserDetailsService {


    private final UserRepo userRepo;


    public CustomUserDetailsService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException{
        User activeUserInfo = userRepo.findByUsername(userName);
        if(activeUserInfo == null){
            throw new UsernameNotFoundException("User not authorized.");
        }

        return activeUserInfo;
    }
}
